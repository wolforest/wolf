package cn.coderule.common.util.lang;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;
import org.apache.commons.codec.binary.Hex;

public class ByteUtil {

    public static String encodeHexString(byte[] data) {
        return Hex.encodeHexString(data);
    }

    public static String encodeHexString(byte[] data, boolean toLowerCase) {
        return Hex.encodeHexString(data, toLowerCase);
    }

    public static void cleanBuffer(ByteBuffer buffer) {
        if (null == buffer || !buffer.isDirect() || buffer.capacity() == 0) {
            return;
        }


        PlatformDependent.freeDirectBuffer(buffer);
    }

    public static long directBufferAddress(ByteBuffer buffer) {
        return PlatformDependent.directBufferAddress(buffer);
    }

    public static byte[] toByte(Long v) {
        ByteBuffer tmp = ByteBuffer.allocate(8);
        tmp.putLong(v);
        return tmp.array();
    }

    public static int setBit(int value, int index, boolean flag) {
        if (flag) {
            return (int) (value | (1L << index));
        } else {
            return (int) (value & ~(1L << index));
        }
    }

    public static boolean getBit(int value, int index) {
        return (value & (1L << index)) != 0;
    }
}
