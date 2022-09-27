package Atabana.Analysers;

import Atabana.Atabana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractAnalyser implements Analyser{
    /**
     * Источник получения данных для анализатора
     */
    protected final Atabana source; //Источник данных
    /**
     * Настройки окна для получения укрупненных графиков
     */
    protected int posStart; //Start position for wave window
    protected int posEnd; //Start position for wave window

    /**
     * Результаты работы анализатора
     */
    protected ArrayList<Object> output; //return data - list of Objects (type is indicates in Parameters), could be 2D (array) and 3D (array of arrays
    protected Map<String, Object> params = new HashMap<>();
    protected boolean isReady = false; //Flag that this Analyser have filled by necessary data

    public AbstractAnalyser(Atabana source) {
        this.source = source;
    }

    @Override
    public void setWindow(int start, int end) {
        posStart = start;
        posEnd = end;
    }

    @Override
    public ArrayList<Object> getArray() throws Exception {
        if (!isReady) {
            this.execute();
        }
        return output;
    }

    @Override
    public ArrayList<Object> getWindowArray() throws Exception {
        if (!isReady) {
            this.execute();
        }
        return getAnalyserWindowArray(posStart, posEnd);
    }

    @Override
    public Map<String, Object> getParameters() throws Exception {
        if (!isReady) {
            execute();
        }
        return params;
    }

    @Override
    public Map<String, Object> getWindowParameters() throws Exception {
        if (!isReady) {
            execute();
        }
        params.replace("WindowStart", posStart); //Position of 1st value
        params.replace("WindowEnd",posEnd); //Position of last value
        return params;
    }

    protected abstract void execute() throws Exception;
    protected abstract ArrayList<Object> getAnalyserWindowArray(int posStart, int posEnd) throws Exception;
}
