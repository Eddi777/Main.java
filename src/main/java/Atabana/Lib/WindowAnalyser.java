package Atabana.Lib;

import java.util.ArrayList;
import java.util.Map;

/*
Sound analyser, gets data for some part of sound file (window) to show in zoomed graph
 */

public interface WindowAnalyser {
    void setWindow(int start, int end);
    Map<String, Object> getWindowParameters() throws Exception;
    ArrayList<Object> getWindowArray() throws Exception;
}
