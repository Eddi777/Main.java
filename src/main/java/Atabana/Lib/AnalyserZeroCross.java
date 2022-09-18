package Atabana.Lib;

import Atabana.Lib.Libs.SoundChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyserZeroCross implements Analyser, AnalyserWindow{

        //Constants
    private static final SoundChunk SOUND_CHUNK = SoundChunk.MIDDLE;
        //Object variables
    private int[] input; //Integer array with row data (wave array)
    private int posStart; //Start position for wave window
    private int posEnd; //Start position for wave window
    private int chunkSize; //Chunk size in output array vs input array

    private int frequency; //Sound frequency (sample rate)
    private ArrayList<Object> output; //return data - list of Objects (type is indicates in Parameters), could be 2D (array) and 3D (array of arrays

    private boolean isReady = false; //Flag that this Analyser have filled by necessary data

    @Override
    public void setAnalyser(final int[] data, int sampleRate) {
        this.input = data;
        this.posStart = 0;
        this.posEnd = data.length;
        this.frequency = sampleRate;
        this.chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, frequency);
        isReady = false;
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
        res.put("Start", 0); //Position of 1st value
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
        output = new ArrayList<>();
        try {
            for (int i = 1; i < input.length / chunkSize; i++) {
                Object res = countZeroCross(i);
                output.add(res);
            }
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser - " + this.getClass());
        }
        this.isReady = true;
    }

    private Integer countZeroCross(int chunkNum) {
        int[] subArray = Arrays.copyOfRange(
                input,
                chunkSize * (chunkNum - 1),
                chunkSize * chunkNum);
        int count = 0;
        int prev = subArray[0];
        for (int i = 1; i < subArray.length; i++) {
            if ((prev > 0) && (subArray[i] < 0)) {
                count ++;
            } else if ((prev > 0) && (subArray[i] < 0)) {
                count ++;
            }
            prev = subArray[i];
        }
        return count * frequency / chunkSize;
    }
}
