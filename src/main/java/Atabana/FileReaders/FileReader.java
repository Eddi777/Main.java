package Atabana.FileReaders;

import Atabana.Lib.Endian;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

/**
 * ���������� ������ ��� ��������� ������ ����������
 */
public interface FileReader {

    /**
     * ��������� ������������ ���������� ������� ����������
     * @param header - ������ ������������ ����� ������������ �����, ������ N (�� getHeaderSize()) ���� �����
     * @return
     * true - ��������� ����� ������������� ���������� � ������ ����� ���� ��������
     * false - ������ ��������� �� ����� �������� ������ �� �����
     */
    boolean isCorrectFormat(byte[] header);

    /**
     * ����� ��������� ����� ��� �������� �����������
     * @return - ���������� ���� � ��������� ������� ���� ����������
     */
    int getHeaderSize();

    /**
     * ����������
      * @param rowFileByteArray - ������ ���� �� ����������������� ����������
     */
    void setRowFileByteArray(byte[] rowFileByteArray);

    /**
     * �������� ������ ����������� �� ���������� ��� ����������� �������� � �������
     * @return - ������ int[] ���� ����������
     */
    int[] getWaveArray();

    /**
     * �������� ������ ����������
     * @return - �������� ������� ����������
     */
    String getFormat();

    /**
     * �������� ���������� ������� � ����������
     * @return - ����� ������� � ����������
     */
    int getNumChannels();

    /**
     * �������� ������� ������������� ����������
     * @return - ����� � ��
     */
    int getSampleRate();

    /**
     * �������� ������� ������������� ����������
     * @return - ������� ��������, ����� ���� ���������� ���� ����� ����������
     */
    int getBitsPerSample();

    /**
     * ����������� ����� ��� ��������� ���������� ������ "������� ��������" � ����� ����� ������� �����������
     *
     * @param bytes - ������ ������ ������ ������
     * @param endian - ������ ��������� ������ ������ ������ (BIG_ENDIAN ��� LOW_ENDIAN)
     * @param isUnsignedBytes - ��������� ����������� ������������� ��������
     *                      true - ������ ������������� ��������
     *                      false - �������� ������������� ��������
     *@return - ����� ����� ��������� �������
     */
    static int intFromByteArray(byte[] bytes, Endian endian, boolean isUnsignedBytes) {
        int res = 0;
        if (endian == Endian.LITTLE) {
            if (isUnsignedBytes) {
                for (int i = 0; i < bytes.length; i++) {
                    res += Byte.toUnsignedInt(bytes[i]) << i * 8;
                }
            } else {
                for (int i = 0; i < bytes.length; i++) {
                    res += bytes[i] << i * 8;
                }
            }
        } else {
            if (isUnsignedBytes) {
                for (int i = 0; i < bytes.length; i++) {
                    res = (res << 8) + Byte.toUnsignedInt(bytes[i]);
                }
            } else {
                for (int i = 0; i < bytes.length; i++) {
                    res = (res << 8) + bytes[i];
                }
            }
        }
        return res;
    }

    static int[] getWaveArrayFromByteArray(final byte[] rowByteArray,
                                           final int bytesPerSample,
                                           final int channels,
                                           Endian endian,
                                           boolean isUnsignedBytes) {
        int step = bytesPerSample * channels;
        int posRowFile = 0;
        int posWaveArray = 0;
        int[] waveArray = new int[rowByteArray.length / step];
        while (posWaveArray < waveArray.length) {
            waveArray[posWaveArray] = FileReader.intFromByteArray(
                    Arrays.copyOfRange(rowByteArray,posRowFile,posRowFile + bytesPerSample),
                    endian, isUnsignedBytes);
            posWaveArray++;
            posRowFile += step;
        }
        return waveArray;
    }

}
