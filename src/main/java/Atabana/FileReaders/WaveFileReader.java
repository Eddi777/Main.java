package Atabana.FileReaders;

import Atabana.Lib.Endian;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class WaveFileReader implements FileReader {

    private final int HEADER_SIZE = 44; // There are 44 bytes before the data section in WAV
    private byte[] rowFileByteArray;

    @Override
    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    @Override
    public void setRowFileByteArray(byte[] rowFileByteArray) {
        this.rowFileByteArray = rowFileByteArray;
    }

    @Override
    public boolean isCorrectFormat(byte[] header) {
        return (new String(Arrays.copyOfRange(header,0,4), StandardCharsets.UTF_8).equals("RIFF") &&
                new String(Arrays.copyOfRange(header,8,12), StandardCharsets.UTF_8).equals("WAVE") &&
                FileReader.intFromByteArray(
                        Arrays.copyOfRange(header,20,22),
                        Endian.LITTLE, true) == 1);
    }

    @Override
    public int[] getWaveArray() {
        int bytesPerSample = getBitsPerSample()/8;
        return FileReader.getWaveArrayFromByteArray(
                Arrays.copyOfRange(rowFileByteArray, HEADER_SIZE, rowFileByteArray.length),
                bytesPerSample,
                getNumChannels(),
                Endian.LITTLE,
                false);
    }

    @Override
    public String getFormat() {
        return new String(
                Arrays.copyOfRange(rowFileByteArray,8,12),
                StandardCharsets.UTF_8);
    }

    @Override
    public int getNumChannels() {
        return FileReader.intFromByteArray(
                Arrays.copyOfRange(rowFileByteArray,22,24), Endian.LITTLE, true);
    }

    @Override
    public int getSampleRate() {
        return FileReader.intFromByteArray(
                Arrays.copyOfRange(rowFileByteArray,24,28), Endian.LITTLE, true);
    }

    @Override
    public int getBitsPerSample() {
        return FileReader.intFromByteArray(
                Arrays.copyOfRange(rowFileByteArray,34,36), Endian.LITTLE, true);
    }
}
