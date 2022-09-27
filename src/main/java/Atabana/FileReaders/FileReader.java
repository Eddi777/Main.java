package Atabana.FileReaders;

import Atabana.Lib.Endian;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

/**
 * Конверторы файлов для получения данных аудиофайла
 */
public interface FileReader {

    /**
     * Проверить соответствие аудиофайла данному конвертеру
     * @param header - массив заголовочной части проверяемого файла, первые N (см getHeaderSize()) байт файла
     * @return
     * true - заголовок файла соответствует конвертеру и данные могут быть получены
     * false - данный конвертер не может получить данные из файла
     */
    boolean isCorrectFormat(byte[] header);

    /**
     * Длина заголовка файла для проверки конвертером
     * @return - количество байт в заголовке данного типа аудиофайла
     */
    int getHeaderSize();

    /**
     * Установить
      * @param rowFileByteArray - массив байт не конвертированного аудиофайла
     */
    void setRowFileByteArray(byte[] rowFileByteArray);

    /**
     * Получить массив аудиоданных из аудиофайла для дальнейшего хранения и анализа
     * @return - массив int[] байт аудиофайла
     */
    int[] getWaveArray();

    /**
     * Получить формат айдиофайла
     * @return - название формата аудиофайла
     */
    String getFormat();

    /**
     * Получить количество каналов в аудиофайле
     * @return - число каналов в аудиофайле
     */
    int getNumChannels();

    /**
     * Получить частоту дискретизации аудиофайла
     * @return - число в Гц
     */
    int getSampleRate();

    /**
     * Получить глубину дискретизации аудиофайла
     * @return - глубина звучания, число байт кодирующих один сэмпл аудиофайла
     */
    int getBitsPerSample();

    /**
     * Статический метод для пересчета нескольких байтов "глубины звучания" в целое число массива аудиоданных
     *
     * @param bytes - массив байтов одного сэмпла
     * @param endian - формат кодировки байтов одного сэмпла (BIG_ENDIAN или LOW_ENDIAN)
     * @param isUnsignedBytes - указатель возможности отрицательной величины
     *                      true - только положительные величины
     *                      false - возможны отрицательные величины
     *@return - целое число результат расчета
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
