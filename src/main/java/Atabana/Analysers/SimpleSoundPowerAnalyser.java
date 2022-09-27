package Atabana.Analysers;

import Atabana.Atabana;
import Atabana.Lib.SoundChunk;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Анализатор значения звуковой мощности по среднему значению амплитуды звуковой волны (простой)
 */
public class SimpleSoundPowerAnalyser extends AbstractAnalyser implements Analyser  {

    private static final SoundChunk SOUND_CHUNK = SoundChunk.SHORT;
    private int chunkSize; //Chunk size in output array vs input array

    public SimpleSoundPowerAnalyser(Atabana source) {
        super(source);
    }

    protected void execute() throws Exception {
        if (chunkSize == 0)
            chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        int maxPower = Arrays.stream(source.getWaveArray()).
                map(e -> Math.abs(e)).
                max().getAsInt();
        output = new ArrayList<>();
        int i = chunkSize;
        try {
            while (i <= source.getWaveArray().length) {
                output.add(countZeroCross(i, maxPower));
                i += chunkSize / source.getChunkDevider();
            }
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser - " + this.getClass());
        }

        //Set parameters
        params.put("Analyser", "SimpleSoundPower"); // Name of Analyser
        params.put("Values", "double"); //Type of values in ArrayList
        params.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        params.put("Graph", "SimpleSoundPower"); //Name of recommended graph image creator
        params.put("Average", output.stream().
                map(e -> (Double) e).
                mapToDouble(Double::new).
                average().
                getAsDouble());
        params.put("GraphName", "Simple sound power, dB"); //name for output graph
        params.put("Chunks", output.size()); //Position of last value

        // set ready flag
        this.isReady = true;
    }

    private double countZeroCross(int chunkLast, int maxPower) {
        int[] subArray = Arrays.copyOfRange(
                source.getWaveArray(),
                chunkLast - chunkSize,
                chunkLast);
        double average = (Arrays.stream(subArray).map(e -> Math.abs(e)).average().getAsDouble());
        Double res = 20 * Math.log10(average/maxPower);
        return (Double.isInfinite(res) || res < -60) ? -60.0 : res;
    }

    protected ArrayList<Object> getAnalyserWindowArray(int posStart, int posEnd) throws Exception {
        if (chunkSize == 0)
            SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        int subChunkSize = chunkSize / source.getChunkDevider();
        return new ArrayList<Object>(output.subList(posStart / subChunkSize, posEnd / subChunkSize));
    }
}