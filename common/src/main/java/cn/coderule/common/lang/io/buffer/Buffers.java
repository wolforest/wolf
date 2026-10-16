package cn.coderule.common.lang.io.buffer;

/**
 * cn.coderule.common.lang.type.memory
 *
 * @author Wingle
 * @since 2026/8/5 10:00 上午
 **/
public class Buffers {
    private static final int JDK_MAJOR = Runtime.version().feature();
    private static final boolean SEGMENT_SUPPORTED = JDK_MAJOR > 21;

    public static Buffer allocate(int capacity) {
        if (SEGMENT_SUPPORTED) {
            return SegmentBuffer.allocate(capacity);
        }
        return NioBuffer.allocate(capacity);
    }

    public static Buffer allocateDirect(int capacity) {
        if (SEGMENT_SUPPORTED) {
            return SegmentBuffer.allocateDirect(capacity);
        }
        return NioBuffer.allocateDirect(capacity);
    }

    public static Buffer wrap(byte[] array) {
        if (SEGMENT_SUPPORTED) {
            return SegmentBuffer.wrap(array);
        }
        return NioBuffer.wrap(array);
    }

    public static Buffer wrap(byte[] array, int offset, int length) {
        if (SEGMENT_SUPPORTED) {
            return SegmentBuffer.wrap(array, offset, length);
        }
        return NioBuffer.wrap(array, offset, length);
    }

    public static boolean isSegmentSupported() {
        return SEGMENT_SUPPORTED;
    }

    public static int jdkMajor() {
        return JDK_MAJOR;
    }
}
