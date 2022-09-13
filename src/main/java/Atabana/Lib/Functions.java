package Atabana.Lib;

class Functions {

/*
    Calculator for integer from several bits
*/
    protected static int intFromByteArray(byte[] bytes, Endian endian, boolean unsignedBytes) {
        //Only little-endian
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
