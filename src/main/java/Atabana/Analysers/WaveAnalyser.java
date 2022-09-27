package Atabana.Analysers;

import Atabana.Atabana;
import Atabana.Lib.SoundChunk;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Анализатор звуковой волны
 *
 */

public class WaveAnalyser extends AbstractAnalyser implements Analyser {
    private final SoundChunk SOUND_CHUNK = SoundChunk.ONE_BYTE;
    private int chunkSize; //Chunk size in output array vs input array

    public WaveAnalyser(Atabana source) {
        super(source);
    }

    protected void execute() throws Exception {
        if (chunkSize == 0)
            chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        try {
            output = (ArrayList<Object>) Arrays.stream(source.getWaveArray()).
                    mapToObj(e -> (Object) e). //Transfer to Object type
                            collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser " + this.getClass());
        }

        //Set parameters
        params.put("Analyser", "Wave"); // Name of Analyser
        params.put("Values", "int"); //Type of values in ArrayList
        params.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        params.put("Graph", "SoundWave"); //Name of recommended graph image creator
        params.put("Average", output.stream().
                map(e -> (Integer) e).
                mapToInt(Integer::intValue).
                average().
                getAsDouble());
        params.put("GraphName", "Sound wave graph"); //name for output graph
        params.put("Chunks", output.size()); //Position of last value

        // set ready flag
        this.isReady = true;
    }

    protected ArrayList<Object> getAnalyserWindowArray(int posStart, int posEnd) throws Exception {
        return new ArrayList<>(output.subList(posStart, posEnd));
    }
}
