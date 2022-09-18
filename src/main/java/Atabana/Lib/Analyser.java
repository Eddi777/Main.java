package Atabana.Lib;

import java.util.ArrayList;
import java.util.Map;

/*
Sound analyser, gets data for all sound file
 */
public interface Analyser {

    void setAnalyser(int[] data, int sampleRate);
    Map<String, Object> getParameters() throws Exception;
    ArrayList<Object> getArray() throws Exception;
}
