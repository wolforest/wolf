package cn.coderule.common.util.net;

import cn.coderule.common.util.lang.SystemUtil;
import io.netty.channel.Channel;
import java.io.File;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
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
                return ipv6Result.get(0);
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

        int lastIndex = ipv4Result.size() - 1;
        if (lastIndex == -1) {
            return null;
        }
        return ipv4Result.get(lastIndex);
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

    public static SocketAddress toSocketAddress(final String addr) {
        int split = addr.lastIndexOf(":");
        String host = addr.substring(0, split);
        String port = addr.substring(split + 1);
        return new InetSocketAddress(host, Integer.parseInt(port));
    }

    public static String toString(final SocketAddress addr) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;
        return inetSocketAddress.getAddress().getHostAddress() +
            ":" +
            inetSocketAddress.getPort();
    }

    public static String toIpString(final String addr) {
        return toString(toSocketAddress(addr));
    }

    public static int ipToInt(String ip) {
        String[] ips = ip.split("\\.");
        return (Integer.parseInt(ips[0]) << 24)
            | (Integer.parseInt(ips[1]) << 16)
            | (Integer.parseInt(ips[2]) << 8)
            | Integer.parseInt(ips[3]);
    }

    public static boolean ipInCIDR(String ip, String cidr) {
        int ipAddr = ipToInt(ip);
        String[] cidrArr = cidr.split("/");
        int netId = Integer.parseInt(cidrArr[1]);
        int mask = 0xFFFFFFFF << (32 - netId);
        int cidrIpAddr = ipToInt(cidrArr[0]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

    public static String getAddress(SocketAddress socketAddress) {
        if (socketAddress == null) {
            return "";
        }

        // Default toString of InetSocketAddress is "hostName/IP:port"
        final String addr = socketAddress.toString();
        int index = addr.lastIndexOf("/");
        return (index != -1) ? addr.substring(index + 1) : addr;
    }

    public static Integer getPort(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            return ((InetSocketAddress) socketAddress).getPort();
        }
        return -1;
    }

    public static String getHost(String address) {
        if (address == null) {
            return "";
        }

        String[] addressSplits = address.split(":");
        if (addressSplits.length < 1) {
            return "";
        }

        return addressSplits[0];
    }

    public static String[] getHostAndPort(String address) {
        int split = address.lastIndexOf(":");
        return split < 0
            ? new String[]{address}
            : new String[]{address.substring(0, split), address.substring(split + 1)};
    }

    public static String getLocalAddr(final Channel channel) {
        SocketAddress remote = channel.localAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.isEmpty()) {
            return "";
        }

        int index = addr.lastIndexOf("/");
        if (index >= 0) {
            return addr.substring(index + 1);
        }

        return addr;
    }

}
