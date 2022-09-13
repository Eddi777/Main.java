package Atabana.Lib;

import java.util.Map;

public interface ReadFile {

    // Check Audio File format
    boolean isCorrectFormat(byte[] header);
    int getHeaderSize();

    // get necessary parameters
    void setRowFileByteArray(byte[] rowFileByteArray);
    int[] getWaveArray();
    String getFormat();
    int getNumChannels();
    int getSampleRate();
    int getBitsPerSample();
}
