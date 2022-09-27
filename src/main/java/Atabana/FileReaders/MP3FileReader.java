package Atabana.FileReaders;

import Atabana.Lib.Endian;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MP3FileReader implements FileReader {

    private final int HEADER_SIZE = -1; // Need all file data to check MP3 file
    private byte[] rowFileByteArray;
    private AudioFormat audioFormat;


    @Override
    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    @Override
    public void setRowFileByteArray(byte[] rowFileByteArray) {
        this.rowFileByteArray = rowFileByteArray;
        final ByteArrayInputStream bais = new ByteArrayInputStream(rowFileByteArray);
        try {
            this.audioFormat = AudioSystem.getAudioInputStream(bais).getFormat();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException("File is not supported as MP3 audio" + e);
        }
    }

    /**
     * Проверка возможности конвертации файла данным конвертером
     *
     * @param header - массив заголовочной части проверяемого файла, первые N (см getHeaderSize()) байт файла
     * @return true - формат файл поддерживается, false - формат файла не поддерживается
     */
    @Override
    public boolean isCorrectFormat(final byte[] header) {
        final List<String> supportedFormats = Arrays.asList(
                "MPEG1L3");
        final ByteArrayInputStream bais = new ByteArrayInputStream(header);
        final AudioFormat af;
        try {
            af = AudioSystem.getAudioInputStream(bais).getFormat();
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
        return supportedFormats.contains(af.getEncoding().toString());
    }

    /**
     * Конвертация MP3 файла в массив байт. Производится конвертация аудиоданных в формат PCM
     * с последующим извлечение данных
     * Алгоритм конвертации с сайта https://stackoverflow.com/questions/938304/how-to-get-audio-data-from-a-mp3
     *
     * @return массив c аудиоданными
     */
    @Override
    public int[] getWaveArray() {
        int[] waveArray;
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(rowFileByteArray);
             final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bais)) {


            AudioFormat baseFormat = audioInputStream.getFormat();
            int bps = baseFormat.getSampleSizeInBits();
            bps = (bps < 1) ? 16: bps;

            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    bps,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    baseFormat.isBigEndian());
            AudioInputStream decodedInputStream = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);

            waveArray = FileReader.getWaveArrayFromByteArray(
                    decodedInputStream.readAllBytes(),
                    bps / 8,
                    baseFormat.getChannels(),
                    (baseFormat.isBigEndian()) ? Endian.BIG: Endian.LITTLE,
                    false);
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException("Ошибка конвертации файла " + e);
        }
        return waveArray;
    }


        @Override
    public String getFormat() {
        return audioFormat.getEncoding().toString();
    }

    @Override
    public int getNumChannels() {
        return audioFormat.getChannels();
    }

    @Override
    public int getSampleRate() {
        return (int) audioFormat.getSampleRate();
    }

    @Override
    public int getBitsPerSample() {
        return (audioFormat.getSampleSizeInBits() < 1) ? 16 : audioFormat.getSampleSizeInBits();
    }
}
