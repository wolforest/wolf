package cn.coderule.common.lang.io.buffer;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * cn.coderule.common.lang.type.memory
 *
 * @author Wingle
 * @since 2026/8/5 10:00 上午
 **/
public class SegmentBuffer implements Buffer {
    private final MemorySegment segment;
    private final Arena arena;
    private final boolean direct;
    private ByteOrder order;
    private final ByteBuffer view;

    public SegmentBuffer(int capacity) {
        this.arena = Arena.ofShared();
        this.segment = arena.allocate(capacity);
        this.direct = true;
        this.order = ByteOrder.BIG_ENDIAN;
        this.view = segment.asByteBuffer().order(this.order);
    }

    private SegmentBuffer(MemorySegment segment, Arena arena, ByteOrder order, boolean direct) {
        this.segment = segment;
        this.arena = arena;
        this.order = order;
        this.direct = direct;
        this.view = segment.asByteBuffer().order(order);
    }

    public static SegmentBuffer allocate(int capacity) {
        return new SegmentBuffer(capacity);
    }

    public static SegmentBuffer allocateDirect(int capacity) {
        return new SegmentBuffer(capacity);
    }

    public static SegmentBuffer wrap(byte[] array) {
        MemorySegment seg = MemorySegment.ofArray(array);
        return new SegmentBuffer(seg, null, ByteOrder.BIG_ENDIAN, false);
    }

    public static SegmentBuffer wrap(byte[] array, int offset, int length) {
        MemorySegment seg = MemorySegment.ofArray(array).asSlice(offset, length);
        return new SegmentBuffer(seg, null, ByteOrder.BIG_ENDIAN, false);
    }

    public MemorySegment getMemorySegment() {
        return segment;
    }

    // ------------------------------------------------------------------------
    // Capacity
    // ------------------------------------------------------------------------
    @Override
    public int capacity() {
        return (int) segment.byteSize();
    }

    @Override
    public int position() {
        return view.position();
    }

    @Override
    public Buffer position(int newPosition) {
        view.position(newPosition);
        return this;
    }

    @Override
    public int limit() {
        return view.limit();
    }

    @Override
    public Buffer limit(int newLimit) {
        view.limit(newLimit);
        return this;
    }

    @Override
    public int remaining() {
        return view.remaining();
    }

    @Override
    public boolean hasRemaining() {
        return view.hasRemaining();
    }

    @Override
    public Buffer clear() {
        view.clear();
        return this;
    }

    @Override
    public Buffer flip() {
        view.flip();
        return this;
    }

    @Override
    public Buffer rewind() {
        view.rewind();
        return this;
    }

    // ------------------------------------------------------------------------
    // Byte
    // ------------------------------------------------------------------------
    @Override
    public byte getByte() {
        return view.get();
    }

    @Override
    public Buffer putByte(byte value) {
        view.put(value);
        return this;
    }

    @Override
    public byte getByte(long index) {
        return view.get((int) index);
    }

    @Override
    public Buffer putByte(long index, byte value) {
        view.put((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Short
    // ------------------------------------------------------------------------
    @Override
    public short getShort() {
        return view.getShort();
    }

    @Override
    public Buffer putShort(short value) {
        view.putShort(value);
        return this;
    }

    @Override
    public short getShort(long index) {
        return view.getShort((int) index);
    }

    @Override
    public Buffer putShort(long index, short value) {
        view.putShort((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Int
    // ------------------------------------------------------------------------
    @Override
    public int getInt() {
        return view.getInt();
    }

    @Override
    public Buffer putInt(int value) {
        view.putInt(value);
        return this;
    }

    @Override
    public int getInt(long index) {
        return view.getInt((int) index);
    }

    @Override
    public Buffer putInt(long index, int value) {
        view.putInt((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Long
    // ------------------------------------------------------------------------
    @Override
    public long getLong() {
        return view.getLong();
    }

    @Override
    public Buffer putLong(long value) {
        view.putLong(value);
        return this;
    }

    @Override
    public long getLong(long index) {
        return view.getLong((int) index);
    }

    @Override
    public Buffer putLong(long index, long value) {
        view.putLong((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Float
    // ------------------------------------------------------------------------
    @Override
    public float getFloat() {
        return view.getFloat();
    }

    @Override
    public Buffer putFloat(float value) {
        view.putFloat(value);
        return this;
    }

    @Override
    public float getFloat(long index) {
        return view.getFloat((int) index);
    }

    @Override
    public Buffer putFloat(long index, float value) {
        view.putFloat((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Double
    // ------------------------------------------------------------------------
    @Override
    public double getDouble() {
        return view.getDouble();
    }

    @Override
    public Buffer putDouble(double value) {
        view.putDouble(value);
        return this;
    }

    @Override
    public double getDouble(long index) {
        return view.getDouble((int) index);
    }

    @Override
    public Buffer putDouble(long index, double value) {
        view.putDouble((int) index, value);
        return this;
    }

    // ------------------------------------------------------------------------
    // Bytes
    // ------------------------------------------------------------------------
    @Override
    public Buffer getBytes(byte[] dst) {
        view.get(dst);
        return this;
    }

    @Override
    public Buffer getBytes(byte[] dst, long offset, int length) {
        view.get(dst, (int) offset, length);
        return this;
    }

    @Override
    public Buffer putBytes(byte[] src) {
        view.put(src);
        return this;
    }

    @Override
    public Buffer putBytes(byte[] src, long offset, int length) {
        view.put(src, (int) offset, length);
        return this;
    }

    @Override
    public Buffer putBuffer(Buffer src) {
        if (src == null) {
            return this;
        }
        if (src instanceof SegmentBuffer) {
            view.put(((SegmentBuffer) src).view);
            return this;
        }
        int rem = src.remaining();
        if (rem > 0) {
            byte[] tmp = new byte[rem];
            src.getBytes(tmp);
            view.put(tmp);
        }
        return this;
    }

    // ------------------------------------------------------------------------
    // Slice
    // ------------------------------------------------------------------------
    @Override
    public Buffer slice() {
        MemorySegment sub = segment.asSlice(view.position(), view.remaining());
        SegmentBuffer sb = new SegmentBuffer(sub, null, order, direct);
        return sb;
    }

    @Override
    public Buffer slice(long index, int length) {
        MemorySegment sub = segment.asSlice(index, length);
        return new SegmentBuffer(sub, null, order, direct);
    }

    @Override
    public Buffer duplicate() {
        SegmentBuffer dup = new SegmentBuffer(segment, null, order, direct);
        dup.view.position(view.position()).limit(view.limit());
        return dup;
    }

    // ------------------------------------------------------------------------
    // Order
    // ------------------------------------------------------------------------
    @Override
    public ByteOrder order() {
        return order;
    }

    @Override
    public Buffer order(ByteOrder order) {
        this.order = order;
        view.order(order);
        return this;
    }

    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    @Override
    public boolean isDirect() {
        return direct;
    }

    @Override
    public boolean isReadOnly() {
        return segment.isReadOnly();
    }

    // ------------------------------------------------------------------------
    // Copy
    // ------------------------------------------------------------------------
    @Override
    public Buffer copy() {
        int rem = view.remaining();
        SegmentBuffer copy = new SegmentBuffer(rem);
        copy.order(order);
        ByteBuffer dup = view.duplicate();
        copy.view.put(dup);
        copy.view.flip();
        return copy;
    }

    @Override
    public Buffer copy(long index, int length) {
        SegmentBuffer copy = new SegmentBuffer(length);
        copy.order(order);
        ByteBuffer dup = view.duplicate();
        dup.limit((int) index + length);
        dup.position((int) index);
        copy.view.put(dup);
        copy.view.flip();
        return copy;
    }

    // ------------------------------------------------------------------------
    // Misc
    // ------------------------------------------------------------------------
    @Override
    public Buffer compact() {
        view.compact();
        return this;
    }

    @Override
    public void close() {
        if (arena != null && arena.scope().isAlive()) {
            arena.close();
        }
    }
}
