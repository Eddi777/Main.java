package Atabana.Lib;

import Atabana.Atabana;
import Atabana.Lib.Libs.SoundChunk;

import java.util.*;
import java.util.stream.Collectors;
/*
Sound analyser for wave data
 */

public class AnalyserWave implements Analyser, AnalyserWindow{

        //Constants
    private static final SoundChunk SOUND_CHUNK = SoundChunk.ONE_BYTE;
        //Variables
    private final Atabana source;
    private int posStart = 0; //Start position for wave window
    private int posEnd = 0; //Final position for wave window
    private int chunkSize; //Chunk size in output array vs input array
        //Output
    private ArrayList<Object> output; //return data - list of Objects (type is indicates in Parameters), could be 2D (array) and 3D (array of arrays
    private boolean isReady = false; //Flag that this Analyser have filled by necessary data

    public AnalyserWave(Atabana source) {
        this.source = source;
    }

    private void execute() throws Exception {
        this.chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        try {
            output = (ArrayList<Object>) Arrays.stream(source.getWaveArray()).
                    mapToObj(e -> (Object) e). //Transfer to Object type
                            collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser " + this.getClass());
        }
        this.isReady = true;
    }

    @Override
    public Map<String, Object> getParameters() throws Exception {
        if (!isReady) {
            execute();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("Analyser", "Wave"); // Name of Analyser
        res.put("Values", "int"); //Type of values in ArrayList
        res.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        res.put("Average", output.stream().
                map(e -> (Integer) e).
                mapToInt(Integer::intValue).
                average().
                getAsDouble());
        res.put("GraphName", "Sound wave graph"); //name for output graph
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
}
