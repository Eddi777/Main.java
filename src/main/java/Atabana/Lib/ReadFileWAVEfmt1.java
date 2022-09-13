package Atabana.Lib;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReadFileWAVEfmt1 implements ReadFile{

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
                Functions.intFromByteArray(Arrays.copyOfRange(header,20,22),Endian.LITTLE, true) == 1);
    }

    @Override
    public int[] getWaveArray() {
        int bytesPerSample = getBitsPerSample()/8;
        int step = bytesPerSample * getNumChannels();
        int posRowFile = HEADER_SIZE;
        int posWaveArray = 0;
        int[] waveArray = new int[rowFileByteArray.length / step];
        while (posRowFile < rowFileByteArray.length) {
            waveArray[posWaveArray] =
                    (Functions.intFromByteArray(
                            Arrays.copyOfRange(rowFileByteArray,posRowFile,posRowFile + bytesPerSample),
                            Endian.LITTLE, false));
            posWaveArray++;
            posRowFile += step;
        }
        return waveArray;
    }

    @Override
    public String getFormat() {
        return new String(Arrays.copyOfRange(rowFileByteArray,8,12), StandardCharsets.UTF_8);
    }

    @Override
    public int getNumChannels() {
        return Functions.intFromByteArray(
                Arrays.copyOfRange(rowFileByteArray,22,24),Endian.LITTLE, true);
    }

    @Override
    public int getSampleRate() {
        return Functions.intFromByteArray(
                Arrays.copyOfRange(rowFileByteArray,24,28),Endian.LITTLE, true);
    }

    @Override
    public int getBitsPerSample() {
        return Functions.intFromByteArray(
                Arrays.copyOfRange(rowFileByteArray,34,36),Endian.LITTLE, true);
    }
}
