package cn.coderule.common.util.test;

public class TestUtil {
    public String createString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(i % 10);
        }

        return sb.toString();
    }

    public byte[] createBytes(int length) {
        return createString(length).getBytes();
    }
}
