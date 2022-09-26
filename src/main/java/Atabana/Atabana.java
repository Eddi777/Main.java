package Atabana;

import Atabana.Lib.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Atabana {



    //Basic settings
    private final String fileName;
    private boolean prepared = false; //Atabana object is prepared for usage mark
    private final Map<String, Analyser> analysers = new HashMap<>();
    private final ReadFile readFile;

    //AudioFile data
    private final int[] waveArray; //Arrray with music wave data bytes
    private final String audioFormat; //Audio filename extension
    private final int sampleRate;  //Sample rate (sound capture frequency) in kHz.
    private final int bitsPerSample; //Number of bits per sample - Sound altitude steps
    private final int numChannels; // Number of sound channels, mono -1, stereo - 2. (v1. works only in mono)

    //Graph image sizes
    private int graphHeight = 150; //Height of the simple graph
    private int graphWidth = 1000; //Height of the simple graph
    //Global settings
    private final int chunkDevider = 4; //делитель блоков для Chunks 2, 4, 8
    private final int maxFrequency = 16000; // максимальная частота спектра для вывода на график

    public Atabana(String filename, byte[] rowFileByteArray) throws Exception {
        this.fileName = filename;
        try {
            readFile = getReadFile(rowFileByteArray);
            this.audioFormat = readFile.getFormat();
            this.numChannels = readFile.getNumChannels();
            this.sampleRate = readFile.getSampleRate();
            this.bitsPerSample = readFile.getBitsPerSample();
            this.waveArray = readFile.getWaveArray();
            this.prepared = true;
        } catch (Exception e) {
            throw new Exception(filename + " //File format is not supported");
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
        fillAnalysersMap();
        return analysers.get(analyserName).getParameters();
    }

    public ArrayList<?> getAnalyserArray (String analyserName) throws Exception {
        fillAnalysersMap();
        return analysers.get(analyserName).getArray();
    }

    public Analyser getAnalyser (String analyserName) throws Exception {
        fillAnalysersMap();
        return analysers.get(analyserName);
    }

    private void fillAnalysersMap() throws Exception {
        if (analysers.isEmpty()) {
            analysers.put("Wave", new WaveAnalyser(this));
            analysers.put("ZeroCross", new ZeroCrossAnalyser(this));
            analysers.put("SimpleSoundPower", new SimpleSoundPowerAnalyser(this));
            analysers.put("AnalyserSpectrogram", new SpectrogramAnalyser(this));


                /*
                    Add future sound analysers
                 */

        }
    }


    public void setWindow(int posStart, int posEnd) {
        //Next step, need to be able to set window of Analysers
    }

    public ReadFile getReadFile(byte[] rowFileByteArray) throws Exception {
        if (readFile != null) {
            return readFile;
        }
        List<ReadFile> list = new ArrayList<>();
        list.add(new ReadFileWAVEfmt2());
        list.add(new ReadFileWAVE());
        list.add(new ReadFileMP3());

        /*
            Add file conveter to the list
        */

        for (ReadFile item : list) {
            if (item.isCorrectFormat(Arrays.copyOf(rowFileByteArray, item.getHeaderSize()))) {
                item.setRowFileByteArray(rowFileByteArray);
                return item;
            }
        }
        throw new Exception("File format is not supported");
    }

    public void setGraphSize (int height, int width) {
        this.graphHeight = height;
        this.graphWidth = width;
    }

    public BufferedImage getGraphImage (String graphImageName, Analyser analyser) throws Exception {
        GraphImage graph;
        switch (graphImageName){
            case "GraphImageSoundWave":
                graph = new GraphImageSoundWave();
                break;
            case "GraphImageSimpleSoundPower":
                graph = new GraphImageSimpleSoundPower();
                break;
            case "GraphImageSpectrogram":
                graph = new GraphImageSpectrogram();
                break;
            default:
                throw new Exception("Graph image is not supported " + graphImageName);
        }
        graph.setGraphImage(analyser.getArray(), analyser.getParameters(), graphWidth, graphHeight);
        return graph.getGraph();
    }

    public BufferedImage getGraphImage (Analyser analyser) throws Exception {
        return getGraphImage(
                (String) analyser.getParameters().get("Graph"),
                analyser);
    }


}

