package cn.coderule.common.util.net;

import com.google.common.net.HostAndPort;
import cn.coderule.common.lang.enums.common.AddressSchemaEnum;
import java.io.Serializable;
import lombok.Getter;

public class Address implements Serializable {
    private static final AddressSchemaEnum DEFAULT_SCHEME = AddressSchemaEnum.IPv4;

    @Getter
    private final AddressSchemaEnum scheme;
    private final HostAndPort hostAndPort;

    public static Address of(String host, int port) {
        return new Address(DEFAULT_SCHEME, host, port);
    }

    public static Address of(String hostAndPort) {
        return new Address(DEFAULT_SCHEME, hostAndPort);
    }

    public Address(AddressSchemaEnum scheme, String hostAndPort) {
        this.scheme = scheme;
        this.hostAndPort = HostAndPort.fromString(hostAndPort);
    }

    public Address(AddressSchemaEnum scheme, String host, int port) {
        this.scheme = scheme;
        this.hostAndPort = HostAndPort.fromParts(host, port);
    }

    public String getHost() {
        return hostAndPort.getHost();
    }

    public String getHostAndPort() {
        return hostAndPort.getHost() + ":" + hostAndPort.getPort();
    }

    public int getPort() {
        return hostAndPort.getPort();
    }

    public int getPortOrDefault(int defaultPort) {
        return hostAndPort.getPortOrDefault(defaultPort);
    }

    public boolean hasPort() {
        return hostAndPort.hasPort();
    }

}
