package cn.coderule.common.util.lang;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemUtil {
    private static final int PROCESSOR_NUMBER = Runtime.getRuntime().availableProcessors();

    public static int getProcessorNumber() {
        return PROCESSOR_NUMBER;
    }

    public static long getPID() {
        return ProcessHandle.current().pid();
    }

    public static long getPageSize() {
        return 0;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix")
            || os.contains("nux")
            || os.indexOf("aix") > 0;
    }
}
