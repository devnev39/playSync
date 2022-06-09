package Converter;
import java.nio.ByteBuffer;

public class Converter {
    public static byte[] LongToBytes(long l){
        ByteBuffer buff = ByteBuffer.allocate(Long.BYTES);
        buff.putLong(l);
        return buff.array();
    }

    public static long BytesToLong(byte[] buff){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(buff);
        buffer.flip();
        return buffer.getLong();
    }
}
