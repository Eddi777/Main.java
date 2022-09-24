package Atabana.Lib;

import Atabana.Atabana;
import Atabana.Lib.Libs.SoundChunk;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyserSpectrogram implements Analyser, AnalyserWindow{

        //Constants
    private static final SoundChunk SOUND_CHUNK = SoundChunk.EXTRA_SHORT;
    private final Atabana source;

        //Object variables
    private int posStart; //Start position for wave window
    private int posEnd; //Final position for wave window
    private int chunkSize; //Chunk size in output array vs input array

    private FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);


    //Outputs
    private ArrayList<Object> output; //return data - list of Objects (type is indicates in Parameters), could be 2D (array) and 3D (array of arrays
    private boolean isReady = false; //Flag that this Analyser have filled by necessary data

    public AnalyserSpectrogram(Atabana source) {
        this.source = source;
    }

    @Override
    public Map<String, Object> getParameters() throws Exception {
        if (!isReady) {
            execute();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("Analyser", "Spectrogram"); // Name of Analyser
        res.put("Values", "double[]"); //Type of values in ArrayList
        res.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        res.put("Graph", "GraphImageSpectrogram"); //Name of recommended graph image creator
        res.put("Average", 0.0);
        res.put("GraphName", "Spectrogram"); //name for output graph
        res.put("End", output.size()); //Position of last value
        return res;
    }

    @Override
    public ArrayList<Object> getArray() throws Exception {
        if (!isReady) {
            execute();
        }
        return output;
    }

    @Override
    public void setWindow(int start, int end) {
        posStart = start;
        posEnd = end;
    }

    @Override
    public Map<String, Object> getWindowParameters() throws Exception {
        Map<String, Object> res = getParameters();
        res.replace("Start", posStart); //Position of 1st value
        res.replace("End",posEnd); //Position of last value
        return res;
    }

    @Override
    public ArrayList<Object> getWindowArray() throws Exception {
        if (!isReady) {
            execute();
        }
        return (ArrayList<Object>) output.subList(posStart, posEnd);
    }

    private void execute() throws Exception {
        chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        output = new ArrayList<>();

        //Window normalization array
        double[] win = getWindowFunction(chunkSize);
        int k = 0;
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
                Complex[] spectr = fft.transform(chunkArray, TransformType.FORWARD);

                //get amplitude of spectr numbers and fill output
                double[] outputSpectr = new double[chunkSize/2];
                for (int j = 0; j < spectr.length/2; j++) {
                    outputSpectr[j] = spectr[0].getArgument() +
                            Math.sqrt(
                                    Math.pow(spectr[j+1].getArgument(), 2) +
                                            Math.pow(spectr[j+1].getImaginary(), 2));
                }
                output.add(outputSpectr);
                i += chunkSize / source.getChunkDevider();
                k++;
                if (k == 10 || k == 10) {
                    System.out.println(Arrays.toString(outputSpectr));
                    System.out.println(spectr.length);
                }

            }
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser - " + this.getClass());
        }
        this.posEnd = output.size();
        this.isReady = true;
    }

    private double[] getWindowFunction(int chunkSize) {
        double[] wf = new double[chunkSize];
        for (int i = 0; i < chunkSize; i++) {
            wf[i] = Math.sin(Math.PI * i / chunkSize);
        }
        return wf;

    }
}
