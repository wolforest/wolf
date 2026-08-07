package cn.coderule.common.lang.io.buffer;
import java.io.Closeable;
import java.nio.ByteOrder;
public interface Buffer extends Closeable {
    // ------------------------------------------------------------------------
    // Capacity
    // ------------------------------------------------------------------------
    int capacity();
    int position();
    Buffer position(int newPosition);
    int limit();
    Buffer limit(int newLimit);
    int remaining();
    boolean hasRemaining();
    Buffer clear();
    Buffer flip();
    Buffer rewind();

    // ------------------------------------------------------------------------
    // Byte
    // ------------------------------------------------------------------------
    byte getByte();
    Buffer putByte(byte value);
    byte getByte(long index);
    Buffer putByte(long index, byte value);

    // ------------------------------------------------------------------------
    // Short
    // ------------------------------------------------------------------------
    short getShort();
    Buffer putShort(short value);
    short getShort(long index);
    Buffer putShort(long index, short value);

    // ------------------------------------------------------------------------
    // Int
    // ------------------------------------------------------------------------
    int getInt();
    Buffer putInt(int value);
    int getInt(long index);
    Buffer putInt(long index, int value);
    // ------------------------------------------------------------------------
    // Long
    // ------------------------------------------------------------------------
    long getLong();
    Buffer putLong(long value);
    long getLong(long index);
    Buffer putLong(long index, long value);
    // ------------------------------------------------------------------------
    // Float
    // ------------------------------------------------------------------------
    float getFloat();
    Buffer putFloat(float value);
    float getFloat(long index);
    Buffer putFloat(long index, float value);
    // ------------------------------------------------------------------------
    // Double
    // ------------------------------------------------------------------------
    double getDouble();
    Buffer putDouble(double value);
    double getDouble(long index);
    Buffer putDouble(long index, double value);
    // ------------------------------------------------------------------------
    // Bytes
    // ------------------------------------------------------------------------
    Buffer getBytes(byte[] dst);
    Buffer getBytes(byte[] dst, long offset, int length);
    Buffer putBytes(byte[] src);
    Buffer putBytes(byte[] src, long offset, int length);

    Buffer putBuffer(Buffer src);

    // ------------------------------------------------------------------------
    // Slice
    // ------------------------------------------------------------------------
    Buffer slice();
    Buffer slice(long index, int length);
    Buffer duplicate();
    // ------------------------------------------------------------------------
    // Order
    // ------------------------------------------------------------------------
    ByteOrder order();
    Buffer order(ByteOrder order);
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    boolean isDirect();
    boolean isReadOnly();
    // ------------------------------------------------------------------------
    // Copy
    // ------------------------------------------------------------------------
    Buffer copy();
    Buffer copy(long index, int length);
    // ------------------------------------------------------------------------
    // Misc
    // ------------------------------------------------------------------------
    Buffer compact();
    @Override
    void close();
}
