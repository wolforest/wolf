package cn.coderule.common.util.net;

import cn.coderule.common.util.lang.SystemUtil;
import java.io.File;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkUtil {
    public static String getLocalAddress() {
        try {
            // Traversal Network interface to get the first non-loopBack and non-private address
            ArrayList<String> ipv4Result = new ArrayList<>();
            ArrayList<String> ipv6Result = new ArrayList<>();
            getAllAddresses(ipv4Result, ipv6Result);

            // prefer ipv4
            if (!ipv4Result.isEmpty()) {
                return selectIPV4(ipv4Result);
            }

            if (!ipv6Result.isEmpty()) {
                return ipv6Result.getFirst();
            }

            //If failed to find,fall back to localhost
            return getLocalHost();
        } catch (Exception e) {
            log.error("Failed to obtain local address", e);
        }

        return null;
    }

    public static String getLocalHost() throws UnknownHostException {
        final InetAddress localHost = InetAddress.getLocalHost();
        return normalizeHostAddress(localHost);
    }

    private static String selectIPV4(ArrayList<String> ipv4Result) {
        for (String ip : ipv4Result) {
            if (ip.startsWith("127.0")
                || ip.startsWith("192.168")
                || ip.startsWith("0.")) {
                continue;
            }

            return ip;
        }

        return ipv4Result.getLast();
    }

    private static void getAllAddresses(ArrayList<String> ipv4Result, ArrayList<String> ipv6Result) throws SocketException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            final NetworkInterface nif = enumeration.nextElement();
            if (isBridge(nif) || nif.isVirtual() || nif.isPointToPoint() || !nif.isUp()) {
                continue;
            }

            final Enumeration<InetAddress> en = nif.getInetAddresses();
            while (en.hasMoreElements()) {
                final InetAddress address = en.nextElement();
                if (address.isLoopbackAddress()) {
                    continue;
                }

                if (address instanceof Inet6Address) {
                    ipv6Result.add(normalizeHostAddress(address));
                } else {
                    ipv4Result.add(normalizeHostAddress(address));
                }
            }
        }
    }

    public static String normalizeHostAddress(final InetAddress localHost) {
        if (localHost instanceof Inet6Address) {
            return "[" + localHost.getHostAddress() + "]";
        } else {
            return localHost.getHostAddress();
        }
    }

    private static boolean isBridge(NetworkInterface networkInterface) {
        try {
            if (SystemUtil.isLinux()) {
                String interfaceName = networkInterface.getName();
                File file = new File("/sys/class/net/" + interfaceName + "/bridge");
                return file.exists();
            }
        } catch (SecurityException e) {
            //Ignore
        }
        return false;
    }
}
