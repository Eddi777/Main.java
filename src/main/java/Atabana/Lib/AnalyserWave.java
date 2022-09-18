package Atabana.Lib;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/*
Sound analyser for wave data
 */

public class AnalyserWave implements Analyser, AnalyserWindow{

    private int[] input; //Integer array with row data (wave array)
    private int posStart; //Start position for wave window
    private int posEnd; //Start position for wave window
    private ArrayList<Object> output; //return data - list of Objects (type is indicates in Parameters), could be 2D (array) and 3D (array of arrays

    private boolean isSynchronized = false; //Flag of synchronisation data array and parameters map
    private boolean isReady = false; //Flag that this Analyser have filled by necessary data

    @Override
    public void setAnalyser(int[] data) {
        this.input = data;
        this.posStart = 0;
        this.posEnd = data.length;
        isReady = true;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    private void execute() {
        output = (ArrayList<Object>) Arrays.stream(input).mapToObj(e -> (Object) e).collect(Collectors.toList());
        isSynchronized = true;
    }

    @Override
    public Map<String, Object> getParameters() throws Exception {
        checkReadiness();
        Map<String, Object> res = new HashMap<>();
        res.put("Analyser", "Wave"); // Name of Analyser
        res.put("Values", "int"); //Type of values in ArrayList
        res.put("isReady", isReady); //Readiness of the analyser
        res.put("Start", 0); //Position of 1st value
        res.put("End", output.size()); //Position of last value
        return res;
    }

    @Override
    public ArrayList<Object> getArray() throws Exception {
        checkReadiness();
        return output;
    }

    @Override
    public void setWindow(int start, int end) {
        posStart = start;
        posEnd = end;
    }

    @Override
    public Map<String, Object> getWindowParameters() throws Exception {
        checkReadiness();
        Map<String, Object> res = new HashMap<>();
        res.put("Analyser", "Wave"); // Name of Analyser
        res.put("Values", "int"); //Type of values in ArrayList
        res.put("Start", posStart); //Position of 1st value
        res.put("End",posEnd); //Position of last value
        return res;
    }

    @Override
    public ArrayList<Object> getWindowArray() throws Exception {
        checkReadiness();
        return (ArrayList<Object>) output.subList(posStart, posEnd);
    }

    private void checkReadiness() throws Exception {
        if (!isReady) {
            throw new Exception("Analyser is not ready");
        }
        if (!isSynchronized) {
            execute();
        }
    }
}
