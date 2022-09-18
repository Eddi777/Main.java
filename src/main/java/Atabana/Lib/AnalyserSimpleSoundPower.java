package Atabana.Lib;

import Atabana.Atabana;
import Atabana.Lib.Analyser;
import Atabana.Lib.AnalyserWindow;
import Atabana.Lib.Libs.SoundChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnalyserSimpleSoundPower implements Analyser, AnalyserWindow {

        //Constants
    private static final SoundChunk SOUND_CHUNK = SoundChunk.LARGE;
    private final Atabana source; //Источник данных

        //Variables
    private int posStart; //Start position for wave window
    private int posEnd; //Start position for wave window
    private int chunkSize; //Chunk size in output array vs input array

        //Outputs
    private ArrayList<Object> output; //return data - list of Objects (type is indicates in Parameters), could be 2D (array) and 3D (array of arrays
    private boolean isReady = false; //Flag that this Analyser have filled by necessary data

    public AnalyserSimpleSoundPower(Atabana source) {
        this.source = source;
    }

    @Override
    public Map<String, Object> getParameters() throws Exception {
        if (!isReady) {
            execute();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("Analyser", "SimpleSoundPower"); // Name of Analyser
        res.put("Values", "double"); //Type of values in ArrayList
        res.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        res.put("Average", output.stream().
                map(e -> (Double) e).
                mapToDouble(Double::new).
                average().
                getAsDouble());
        res.put("GraphName", "Simple sound power, dB"); //name for output graph
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
        this.chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        int maxPower = Arrays.stream(source.getWaveArray()).
                map(e -> Math.abs(e)).
                max().getAsInt();
        System.out.println(maxPower);
        output = new ArrayList<>();
        int i = chunkSize;
        try {
            while (i <= source.getWaveArray().length) {
                output.add(countZeroCross(i, maxPower));
                i += chunkSize / 2;
            }
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser - " + this.getClass());
        }
        this.isReady = true;
    }

    private double countZeroCross(int chunkLast, int maxPower) {
        int[] subArray = Arrays.copyOfRange(
                source.getWaveArray(),
                chunkLast - chunkSize,
                chunkLast);
        double average = ((double) Arrays.stream(subArray).map(e -> Math.abs(e)).sum()) / ((double) chunkSize);
        Double res = 10 * Math.log10(average/maxPower);
        return (Double.isInfinite(res) || res < -30) ? -30.0 : res;
    }
}