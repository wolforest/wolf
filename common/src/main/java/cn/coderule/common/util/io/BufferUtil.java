package cn.coderule.common.util.io;

import io.netty.util.internal.PlatformDependent;
import java.nio.ByteBuffer;

public class BufferUtil {
    public static void cleanBuffer(ByteBuffer buffer) {
        if (null == buffer || !buffer.isDirect() || buffer.capacity() == 0) {
            return;
        }

        PlatformDependent.freeDirectBuffer(buffer);
    }

    public static long directBufferAddress(ByteBuffer buffer) {
        return PlatformDependent.directBufferAddress(buffer);
    }
}
