package Atabana.Lib;

import Atabana.Atabana;
import Atabana.Lib.Libs.SoundChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnalyserZeroCross implements Analyser, AnalyserWindow{

        //Constants
    private static final SoundChunk SOUND_CHUNK = SoundChunk.LARGE;
    private final Atabana source;

        //Object variables
    private int posStart; //Start position for wave window
    private int posEnd; //Final position for wave window
    private int chunkSize; //Chunk size in output array vs input array

        //Outputs
    private ArrayList<Object> output; //return data - list of Objects (type is indicates in Parameters), could be 2D (array) and 3D (array of arrays
    private boolean isReady = false; //Flag that this Analyser have filled by necessary data

    public AnalyserZeroCross(Atabana source) {
        this.source = source;
    }

    @Override
    public Map<String, Object> getParameters() throws Exception {
        if (!isReady) {
            execute();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("Analyser", "ZeroCross"); // Name of Analyser
        res.put("Values", "int"); //Type of values in ArrayList
        res.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        res.put("Average", output.stream().
                map(e -> (Integer) e).
                mapToInt(Integer::intValue).
                average().
                getAsDouble());
        res.put("GraphName", "Zero cross frequency, Hz"); //name for output graph
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
        output = new ArrayList<>();
        int i = chunkSize;
        try {
            while (i <= source.getWaveArray().length) {
                output.add(countZeroCross(i));
                i += chunkSize / 2;
            }
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser - " + this.getClass());
        }
        this.posEnd = output.size();
        this.isReady = true;
    }

    private Integer countZeroCross(int chunkLast) {
        int[] subArray = Arrays.copyOfRange(
                source.getWaveArray(),
                chunkLast - chunkSize,
                chunkLast);
        int count = 0;
        int prev = subArray[0];
        for (int i = 1; i < subArray.length; i++) {
            if ((prev > 0) && (subArray[i] < 0)) {
                count ++;
            } else if ((prev < 0) && (subArray[i] > 0)) {
                count ++;
            } else if ((prev !=0) && (subArray[i] == 0)) {
                count ++;
            }
            prev = subArray[i];
        }
        return count * source.getSampleRate() / chunkSize;
    }
}
