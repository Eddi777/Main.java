package Atabana.Lib;

import java.util.ArrayList;
import java.util.Map;

public class AnalyserZeroCross implements Analyser, AnalyserWindow{

    @Override
    public void setWindow(int start, int end) {

    }

    @Override
    public Map<String, Object> getWindowParameters() throws Exception {
        return null;
    }

    @Override
    public ArrayList<Object> getWindowArray() throws Exception {
        return null;
    }

    @Override
    public void setAnalyser(int[] data) {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public Map<String, Object> getParameters() throws Exception {
        return null;
    }

    @Override
    public ArrayList<Object> getArray() throws Exception {
        return null;
    }
}
