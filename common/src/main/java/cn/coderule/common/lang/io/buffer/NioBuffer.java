package cn.coderule.common.lang.io.buffer;

import cn.coderule.common.util.lang.ByteUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * cn.coderule.common.lang.type.memory
 *
 * @author Wingle
 * @since 2026/8/5 10:00 上午
 **/
public class NioBuffer implements Buffer {
    private final ByteBuffer buffer;

    public NioBuffer(int capacity) {
        this(ByteBuffer.allocate(capacity));
    }

    public NioBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public static NioBuffer allocate(int capacity) {
        return new NioBuffer(ByteBuffer.allocate(capacity));
    }

    public static NioBuffer allocateDirect(int capacity) {
        return new NioBuffer(ByteBuffer.allocateDirect(capacity));
    }

    public static NioBuffer wrap(byte[] array) {
        return new NioBuffer(ByteBuffer.wrap(array));
    }

    public static NioBuffer wrap(byte[] array, int offset, int length) {
        return new NioBuffer(ByteBuffer.wrap(array, offset, length));
    }

    public ByteBuffer getByteBuffer() {
        return buffer;
    }

    // ------------------------------------------------------------------------
    // Capacity
    // ------------------------------------------------------------------------
    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public int position() {
        return buffer.position();
    }

    @Override
    public Buffer position(int newPosition) {
        buffer.position(newPosition);
        return this;
    }

    @Override
    public int limit() {
        return buffer.limit();
    }

    @Override
    public Buffer limit(int newLimit) {
        buffer.limit(newLimit);
        return this;
    }

    @Override
    public int remaining() {
        return buffer.remaining();
    }

    @Override
    public boolean hasRemaining() {
        return buffer.hasRemaining();
    }

    @Override
    public Buffer clear() {
        buffer.clear();
        return this;
    }

    @Override
    public Buffer flip() {
        buffer.flip();
        return this;
    }

    @Override
    public Buffer rewind() {
        buffer.rewind();
        return this;
    }

    // ------------------------------------------------------------------------
    // Byte
    // ------------------------------------------------------------------------
    @Override
    public byte getByte() {
        return buffer.get();
    }

    @Override
    public Buffer putByte(byte value) {
        buffer.put(value);
        return this;
    }

    @Override
    public byte getByte(long index) {
        return buffer.get((int) index);
    }

    @Override
    public Buffer putByte(long index, byte value) {
        buffer.put((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Short
    // ------------------------------------------------------------------------
    @Override
    public short getShort() {
        return buffer.getShort();
    }

    @Override
    public Buffer putShort(short value) {
        buffer.putShort(value);
        return this;
    }

    @Override
    public short getShort(long index) {
        return buffer.getShort((int) index);
    }

    @Override
    public Buffer putShort(long index, short value) {
        buffer.putShort((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Int
    // ------------------------------------------------------------------------
    @Override
    public int getInt() {
        return buffer.getInt();
    }

    @Override
    public Buffer putInt(int value) {
        buffer.putInt(value);
        return this;
    }

    @Override
    public int getInt(long index) {
        return buffer.getInt((int) index);
    }

    @Override
    public Buffer putInt(long index, int value) {
        buffer.putInt((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Long
    // ------------------------------------------------------------------------
    @Override
    public long getLong() {
        return buffer.getLong();
    }

    @Override
    public Buffer putLong(long value) {
        buffer.putLong(value);
        return this;
    }

    @Override
    public long getLong(long index) {
        return buffer.getLong((int) index);
    }

    @Override
    public Buffer putLong(long index, long value) {
        buffer.putLong((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Float
    // ------------------------------------------------------------------------
    @Override
    public float getFloat() {
        return buffer.getFloat();
    }

    @Override
    public Buffer putFloat(float value) {
        buffer.putFloat(value);
        return this;
    }

    @Override
    public float getFloat(long index) {
        return buffer.getFloat((int) index);
    }

    @Override
    public Buffer putFloat(long index, float value) {
        buffer.putFloat((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Double
    // ------------------------------------------------------------------------
    @Override
    public double getDouble() {
        return buffer.getDouble();
    }

    @Override
    public Buffer putDouble(double value) {
        buffer.putDouble(value);
        return this;
    }

    @Override
    public double getDouble(long index) {
        return buffer.getDouble((int) index);
    }

    @Override
    public Buffer putDouble(long index, double value) {
        buffer.putDouble((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Bytes
    // ------------------------------------------------------------------------
    @Override
    public Buffer getBytes(byte[] dst) {
        buffer.get(dst);
        return this;
    }

    @Override
    public Buffer getBytes(byte[] dst, long offset, int length) {
        buffer.get(dst, (int) offset, length);
        return this;
    }

    @Override
    public Buffer putBytes(byte[] src) {
        buffer.put(src);
        return this;
    }

    @Override
    public Buffer putBytes(byte[] src, long offset, int length) {
        buffer.put(src, (int) offset, length);
        return this;
    }

    @Override
    public Buffer putBuffer(Buffer src) {
        if (src == null) {
            return this;
        }
        if (src instanceof NioBuffer) {
            buffer.put(((NioBuffer) src).buffer);
            return this;
        }
        int rem = src.remaining();
        if (rem > 0) {
            byte[] tmp = new byte[rem];
            src.getBytes(tmp);
            buffer.put(tmp);
        }
        return this;
    }

    // ------------------------------------------------------------------------
    // Slice
    // ------------------------------------------------------------------------
    @Override
    public Buffer slice() {
        return new NioBuffer(buffer.slice());
    }

    @Override
    public Buffer slice(long index, int length) {
        int oldPosition = buffer.position();
        int oldLimit = buffer.limit();
        try {
            buffer.limit((int) index + length);
            buffer.position((int) index);
            return new NioBuffer(buffer.slice());
        } finally {
            buffer.limit(oldLimit);
            buffer.position(oldPosition);
        }
    }

    @Override
    public Buffer duplicate() {
        return new NioBuffer(buffer.duplicate());
    }

    // ------------------------------------------------------------------------
    // Order
    // ------------------------------------------------------------------------
    @Override
    public ByteOrder order() {
        return buffer.order();
    }

    @Override
    public Buffer order(ByteOrder order) {
        buffer.order(order);
        return this;
    }

    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    @Override
    public boolean isDirect() {
        return buffer.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return buffer.isReadOnly();
    }

    // ------------------------------------------------------------------------
    // Copy
    // ------------------------------------------------------------------------
    @Override
    public Buffer copy() {
        int rem = buffer.remaining();
        ByteBuffer dup = buffer.duplicate();
        ByteBuffer copy = ByteBuffer.allocate(rem);
        copy.order(buffer.order());
        copy.put(dup);
        copy.flip();
        return new NioBuffer(copy);
    }

    @Override
    public Buffer copy(long index, int length) {
        ByteBuffer dup = buffer.duplicate();
        dup.limit((int) index + length);
        dup.position((int) index);
        ByteBuffer copy = ByteBuffer.allocate(length);
        copy.order(buffer.order());
        copy.put(dup);
        copy.flip();
        return new NioBuffer(copy);
    }

    // ------------------------------------------------------------------------
    // Misc
    // ------------------------------------------------------------------------
    @Override
    public Buffer compact() {
        buffer.compact();
        return this;
    }

    /**
     * Heap buffers are released by GC, so nothing to close here.
     * For direct buffers we rely on the JVM's Cleaner; explicitly invoking
     * sun.misc.Cleaner is JDK-specific and unstable, so we intentionally keep this a no-op.
     */
    @Override
    public void close() {
        ByteUtil.cleanBuffer(this.buffer);
    }
}
