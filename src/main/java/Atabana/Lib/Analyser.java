package Atabana.Lib;

import Atabana.Atabana;

import java.util.ArrayList;
import java.util.Map;

/*
Sound analyser, gets data for all sound file
 */
public interface Analyser {

    Map<String, Object> getParameters() throws Exception;
    ArrayList<Object> getArray() throws Exception;
}
