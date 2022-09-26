package Atabana.Lib.Libs;


/**
 * “ип временной выборки аудиофайла (последовательности данных) дл€ агрегационного анализа
 * ONE_BYTE - выборка не требуетс€, используютс€ все данные аудиофайла
 */
public enum SoundChunk {
    ONE_BYTE (1), //Special chunk size for 1 byte time
    EXTRA_SHORT (10), //Shortest time limit of hearing any sound
    SHORT (100),
    MIDDLE (250), //limit to recognise sound parameters
    LARGE (500), //limit to understand pronunciation
    EXTRA_LARGE (1000), //limit to understand pronunciation
    LONG (2500) //limit to understand pronunciation
    ;
    private final int time;

    private SoundChunk(int time) {
        this.time = time;
    }

    /**
     * ѕолучить рекомендованное врем€ выборки аудиофайла дл€ выбранного типа агрегационного анализа
     * @return - врем€ отрезка, мсек
     */
    public int getTime(){
        return this.time;
    }

    /**
     * –асчет длины выборки из аудиоданных (количество байт) дл€ агрегационного анализа. „исло кратное степени двух.
     * @param chunk - тип временной выборки
     * @param frequency - частота дискретизации аудиофайла, √ц
     * @return - длина выборки аудиоданных (количество байт) наиболее близко соответствующее времени требуемой длины выборки
     */
    public static int getChunkSize(SoundChunk chunk, int frequency) {
        if (chunk == ONE_BYTE) {
            return 1;
        }
        int res = chunk.time * frequency / 1000;
        int base = (int) Math.round(Math.log(res)/Math.log(2));
        return (int) Math.pow(2, base);
    }
}
