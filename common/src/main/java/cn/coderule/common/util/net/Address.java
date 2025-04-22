/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
