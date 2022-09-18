package Atabana.Lib;

import Atabana.Lib.Libs.Endian;

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

    static int intFromByteArray(byte[] bytes, Endian endian, boolean unsignedBytes) {
        int res = 0;
        if (endian == Endian.LITTLE) {
            if (unsignedBytes) {
                for (int i = 0; i < bytes.length; i++) {
                    res += Byte.toUnsignedInt(bytes[i]) << i * 8;
                }
            } else {
                for (int i = 0; i < bytes.length; i++) {
                    res += bytes[i] << i * 8;
                }
            }
        } else {
            if (unsignedBytes) {
                for (int i = 0; i < bytes.length; i++) {
                    res += Byte.toUnsignedInt(bytes[i]) >> i * 8;
                }
            } else {
                for (int i = 0; i < bytes.length; i++) {
                    res += bytes[i] >> i * 8;
                }
            }
        }
        return res;
    }


}
