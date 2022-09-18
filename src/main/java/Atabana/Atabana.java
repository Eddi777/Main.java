package Atabana;

import Atabana.Lib.*;

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
    private final int sampleRate;  //Sample rate (sound frequency) in kHz.
    private final int bitsPerSample; //Number of bits per sample - Sound altitude steps
    private final int numChannels; // Number of sound channels, mono -1, stereo - 2. (v1. works only in mono)


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

    //Расчет продолжительности звуковых байт - Сhunks
    private int SoundtimeToChunkConvert(int soundTime) {
        int res = soundTime * this.sampleRate / 1000;
        int base = (int) Math.round(Math.log(res)/Math.log(2));
        return (int) Math.pow(2, base);
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
        Analyser analyser = getAnalyser(analyserName);
        return analyser.getParameters();
    }

    public ArrayList<?> getAnalyserArray (String analyserName) throws Exception {
        Analyser analyser = getAnalyser(analyserName);
        return analyser.getArray();
    }

    public Analyser getAnalyser(String analyserName) throws Exception {
            //Fulfill list of analysers
        if (analysers.isEmpty()) {
            analysers.put("Wave", new AnalyserWave());
            analysers.put("ZeroCross", new AnalyserZeroCross());

            /*
                Add future sound analysers
             */

            //Analysers initialization
            for (String name: analysers.keySet()) {
                if (analysers.get(name) == null) {
                    throw new Exception("Analyser is not serviceable: " + name);
                }
                analysers.get(name).setAnalyser(waveArray, sampleRate);
            }
        }
            //Get requested analyser
        if (!analysers.containsKey(analyserName)) {
            throw new Exception("Analyser name is not correct: " + analyserName);
        }
        return analysers.get(analyserName);
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
        list.add(new ReadFileWAVEfmt1());

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
}

