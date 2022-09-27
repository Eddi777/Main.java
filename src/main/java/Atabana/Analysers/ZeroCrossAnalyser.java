package Atabana.Analysers;

import Atabana.Atabana;
import Atabana.Lib.SoundChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Анализатор числа пересечения звуковой волны через нулевую ось (Zero Cross)
 */
public class ZeroCrossAnalyser extends AbstractAnalyser implements Analyser {

        //Constants
    private static final SoundChunk SOUND_CHUNK = SoundChunk.LARGE;
    private int chunkSize; //Chunk size in output array vs input array

    public ZeroCrossAnalyser(Atabana source) {
        super(source);
    }

    protected void execute() throws Exception {
        if (chunkSize == 0)
            chunkSize = SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        output = new ArrayList<>();
        int i = chunkSize;
        try {
            while (i <= source.getWaveArray().length) {
                output.add(countZeroCross(i));
                i += chunkSize / source.getChunkDevider();
            }
        } catch (Exception e) {
            throw new Exception("Internal exception in the analyser - " + this.getClass());
        }

        //Set parameters
        params.put("Analyser", "ZeroCross"); // Name of Analyser
        params.put("Values", "int"); //Type of values in ArrayList
        params.put("Chunk size", chunkSize); //Chunk size in output array vs input array
        params.put("Graph", "SoundWave"); //Name of recommended graph image creator
        params.put("Average", output.stream().
                map(e -> (Integer) e).
                mapToInt(Integer::intValue).
                average().
                getAsDouble());
        params.put("GraphName", "Zero cross count, Hz"); //name for output graph
        params.put("Chunks", output.size()); //Position of last value

        // set ready flag
        isReady = true;
    }

    private Integer countZeroCross(int chunkLastPos) {
        int[] subArray = Arrays.copyOfRange(
                source.getWaveArray(),
                chunkLastPos - chunkSize,
                chunkLastPos);
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

    protected ArrayList<Object> getAnalyserWindowArray(int posStart, int posEnd) throws Exception {
        if (chunkSize == 0)
            SoundChunk.getChunkSize(SOUND_CHUNK, source.getSampleRate());
        int subChunkSize = chunkSize / source.getChunkDevider();
        return new ArrayList<Object>(output.subList(posStart / subChunkSize, posEnd / subChunkSize));
    }
}
