package Atabana.Analysers;

import Atabana.Atabana;
import Atabana.Lib.SoundChunk;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.Arrays;

public class SpectrogramAnalyser extends AbstractAnalyser implements Analyser {

        //Constants
    private static final SoundChunk SOUND_CHUNK = SoundChunk.EXTRA_SHORT;
    private int chunkSize; //Chunk size in output array vs input array
    int maxFreqArrayItems; //Max quantity of frequencies in output list
    private FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

    public SpectrogramAnalyser(Atabana source) {
        super(source);
    }

    protected void execute() throws Exception {
        if (chunkSize == 0)
            chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());

        //set max quantity of frequencies in output list
        maxFreqArrayItems = chunkSize * source.getMaxFrequency()/source.getSampleRate();
        output = new ArrayList<>();

        //Window normalization array
        double[] win = getWindowFunction(chunkSize);
        int i = chunkSize;
        try {
            while (i <= source.getWaveArray().length) {
                double[] chunkArray = Arrays.stream(
                        Arrays.copyOfRange(source.getWaveArray(), i, i+chunkSize)).
                        mapToDouble(Double::valueOf).
                        toArray();
                //window normalization function
                for (int j = 0; j < chunkSize; j++) {
                    chunkArray[j] *= win[j];
                }
                //FFT Calculation
                Complex[] spectre = fft.transform(chunkArray, TransformType.FORWARD);

                //get absolute amplitude of spectre numbers and fill output
                double[] outputSpectre = new double[maxFreqArrayItems];
                for (int j = 1; j <= maxFreqArrayItems; j++) {
                    outputSpectre[j-1] = Math.sqrt(
                            Math.pow(spectre[j+1].getArgument(), 2) + Math.pow(spectre[j+1].getImaginary(), 2));
                }
                output.add(outputSpectre);
                i += chunkSize / source.getChunkDevider();
            }
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser - " + this.getClass());
        }

        //Set parameters
        params.put("Analyser", "Spectrogram"); // Name of Analyser
        params.put("Values", "double[]"); //Type of values in ArrayList
        params.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        params.put("Graph", "Spectrogram"); //Name of recommended graph image creator
        params.put("Average", 0.0);
        params.put("FrequencyArray", getMaxFrequency());
        params.put("MaxFrequency", source.getMaxFrequency());
        params.put("GraphName", "Spectrogram"); //name for output graph
        params.put("Chunks", output.size()); //Position of last value

        // set ready flag
        this.isReady = true;
    }

    private double[] getWindowFunction(int chunkSize) {
        //realization of Sin type window function
        double[] wf = new double[chunkSize];
        for (int i = 0; i < chunkSize; i++) {
            wf[i] = Math.sin(Math.PI * i / chunkSize);
        }
        return wf;
    }

    private int[] getMaxFrequency() {
        int[] res = new int[maxFreqArrayItems];
        for (int i = 1; i <= maxFreqArrayItems; i++) {
            res[i-1] = source.getSampleRate() * i / chunkSize;
        }
        return res;
    }
    protected ArrayList<Object> getAnalyserWindowArray(int posStart, int posEnd) throws Exception {
        if (chunkSize == 0)
            SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        int subChunkSize = chunkSize / source.getChunkDevider();
        return new ArrayList<Object>(output.subList(posStart / subChunkSize, posEnd / subChunkSize));
    }
}
