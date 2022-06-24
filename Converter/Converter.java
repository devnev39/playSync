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

    public static int BytesToInt(byte[] buff){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(buff);
        buffer.flip();
        return buffer.getInt();
    }

    public static byte[] IntToBytes(int i){
        ByteBuffer buff = ByteBuffer.allocate(Integer.BYTES);
        buff.putInt(i);
        return buff.array();
    }

    public static int getAverage(long[] diff){
        long add = 0L;
        for (long l : diff) {
            add += l;
        }
        return (int)add/diff.length;
    }
    
    public static String[] pop(String[] arr){
        String[] newArr = new String[arr.length-1];
        for(int i=0;i<arr.length-1;i++){
            newArr[i] = arr[i];
        }
        return newArr;
    }
}
