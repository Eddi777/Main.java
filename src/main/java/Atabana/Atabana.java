package Atabana;

import Atabana.Analysers.Analyser;
import Atabana.Analysers.SimpleSoundPowerAnalyser;
import Atabana.Analysers.SpectrogramAnalyser;
import Atabana.Analysers.WaveAnalyser;
import Atabana.Analysers.ZeroCrossAnalyser;
import Atabana.ChartCreators.AbstractChartCreator;
import Atabana.ChartCreators.SimpleSoundPowerChartCreator;
import Atabana.ChartCreators.WaveChartCreator;
import Atabana.ChartCreators.SpectrogramChartCreator;
import Atabana.FileReaders.FileReader;
import Atabana.FileReaders.MP3FileReader;
import Atabana.FileReaders.WaveFileReader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Atabana {



    //Basic settings
    private final String fileName;
    private boolean prepared = false; //Atabana object is prepared for usage mark
    private final Map<String, Analyser> analysers = new HashMap<>();
    private final FileReader fileReader;

    //AudioFile data
    private final int[] waveArray; //Arrray with music wave data bytes
    private final String audioFormat; //Audio filename extension
    private final int sampleRate;  //Sample rate (sound capture frequency) in kHz.
    private final int bitsPerSample; //Number of bits per sample - Sound altitude steps
    private final int numChannels; // Number of sound channels, mono -1, stereo - 2. (v1. works only in mono)

    //Window parameters (start & end) too look at charts deeper
    private int windowStart;
    private int windowEnd;
    //Graph image sizes
    private int graphHeight = 150; //Height of the simple graph
    private int graphWidth = 1000; //Height of the simple graph
    //Global settings
    private final int chunkDevider = 4; //делитель блоков для Chunks 2, 4, 8
    private final int maxFrequency = 16000; // максимальная частота спектра для вывода на график

    public Atabana(String filename, byte[] rowFileByteArray) throws Exception {
        this.fileName = filename;
        try {
            fileReader = getFileReader(rowFileByteArray);
            this.audioFormat = fileReader.getFormat();
            this.numChannels = fileReader.getNumChannels();
            this.sampleRate = fileReader.getSampleRate();
            this.bitsPerSample = fileReader.getBitsPerSample();
            this.waveArray = fileReader.getWaveArray();
            this.prepared = true;
        } catch (Exception e) {
            throw new Exception("File format is not supported: " + filename);
        }
    }

    public int[] getWaveArray() {
        return waveArray;
    }
    public int getSampleRate() {
        return sampleRate;
    }
    public int getBitsPerSample() {
        return bitsPerSample;
    }
    public int getNumChannels() {
        return numChannels;
    }
    public int getChunkDevider() {
        return chunkDevider;
    }
    public int getMaxFrequency() {
        return maxFrequency;
    }

    public String getFileData() {
        return "FileName" + fileName + "\n" +
                "AudioFormat " + audioFormat + "\n" +
                "Number of channels " + numChannels + "\n" +
                "Sample rate " + sampleRate + ",Hz\n" +
                "Bits per sample " + bitsPerSample + ",bits\n" +
                "Wave array size " + waveArray.length + ",bytes\n";
    }

    public Map<String, Object> getAnalyserParameters(String analyserName) throws Exception {
        if (analysers.isEmpty())
            fillAnalysersMap();
        return analysers.get(analyserName).getParameters();
    }

    public ArrayList<?> getAnalyserArray (String analyserName) throws Exception {
        if (analysers.isEmpty())
            fillAnalysersMap();
        return analysers.get(analyserName).getArray();
    }

    public Map<String, Object> getAnalyserWindowParameters(String analyserName) throws Exception {
        if (analysers.isEmpty())
            fillAnalysersMap();
        return analysers.get(analyserName).getWindowParameters();
    }

    public ArrayList<?> getAnalyserWindowArray (String analyserName) throws Exception {
        if (analysers.isEmpty())
            fillAnalysersMap();
        return analysers.get(analyserName).getWindowArray();
    }

    public Analyser getAnalyser (String analyserName) throws Exception {
        if (analysers.isEmpty())
            fillAnalysersMap();
        return analysers.get(analyserName);
    }

    private void fillAnalysersMap() throws Exception {
        if (analysers.isEmpty()) {
            analysers.put("Wave", new WaveAnalyser(this));
            analysers.put("ZeroCross", new ZeroCrossAnalyser(this));
            analysers.put("SimpleSoundPower", new SimpleSoundPowerAnalyser(this));
            analysers.put("Spectrogram", new SpectrogramAnalyser(this));


                /*
                    Add future sound analysers
                 */

        }
    }

    public void setWindow(int posStart, int posEnd) throws Exception {
        windowStart = posStart;
        windowEnd = posEnd;
        if (analysers.isEmpty())
            fillAnalysersMap();
        for (Analyser item: analysers.values()) {
            item.setWindow(windowStart, windowEnd);
        }
    }

    public FileReader getFileReader(byte[] rowFileByteArray) {
        if (fileReader != null) {
            return fileReader;
        }
        FileReader fr;
        if (fileName.toLowerCase().endsWith(".wav")) {
            fr = new WaveFileReader();
        } else if (fileName.toLowerCase().endsWith(".mp3")) {
            fr = new MP3FileReader();
        } else {
            throw new IllegalArgumentException("Not available file driver " + fileName);
        }
        int headerSize = fr.getHeaderSize();
        if (headerSize == -1)
            headerSize = rowFileByteArray.length;
        if (fr.isCorrectFormat(Arrays.copyOf(rowFileByteArray,headerSize))) {
            fr.setRowFileByteArray(rowFileByteArray);
            return fr;
        }
        throw new IllegalArgumentException("File format is not supported " + fileName);
    }

    public void setGraphSize (int height, int width) {
        this.graphHeight = height;
        this.graphWidth = width;
    }

    private AbstractChartCreator getChartCreator(String chartName) throws IllegalStateException {
        AbstractChartCreator graph;
        switch (chartName){
            case "SoundWave":
                graph = new WaveChartCreator();
                break;
            case "SimpleSoundPower":
                graph = new SimpleSoundPowerChartCreator();
                break;
            case "Spectrogram":
                graph = new SpectrogramChartCreator();
                break;
            default:
                throw new IllegalStateException("Graph image is not supported " + chartName);
        }
        return graph;
    }

    public BufferedImage getChartImage(String chartName, Analyser analyser) throws Exception {
        AbstractChartCreator chart = getChartCreator(chartName);
        chart.setChartCreator(analyser.getArray(), analyser.getParameters(), graphWidth, graphHeight);
        return chart.getChart();
    }

    public BufferedImage getWindowChartImage(String chartName, Analyser analyser) throws Exception {
        AbstractChartCreator chart = getChartCreator(chartName);
        analyser.setWindow(windowStart, windowEnd);
        chart.setChartCreator(analyser.getWindowArray(), analyser.getWindowParameters(), graphWidth, graphHeight);
        return chart.getChart();
    }
    public BufferedImage getChartImage(Analyser analyser) throws Exception {
        return getChartImage(
                (String) analyser.getParameters().get("Graph"),
                analyser);
    }


}

