package Atabana.Lib.Libs;

public enum SoundChunk {
    ONE_BYTE (1), //Special chunk size for 1 byte time
    EXTRA_SHORT (10), //Shortest limit of hearing any sound
    SHORT (100),
    MIDDLE (250), //limit to recognise sound parameters
    LARGE (500) //limit to understand pronunciation
    ;
    private final int time;

    private SoundChunk(int time) {
        this.time = time;
    }

    public int getTime(){
        return this.time;
    }

    public static int getChunkSize(SoundChunk chunk, int frequency) {
        if (chunk == ONE_BYTE) {
            return 1;
        }
        int res = chunk.time * frequency / 1000;
        int base = (int) Math.round(Math.log(res)/Math.log(2));
        return (int) Math.pow(2, base);
    }


}
