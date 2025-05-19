package naufal90.localhostplus.utils;

import java.net.*;
import java.util.*;

public class NetworkUtils {
    public static String getLocalIp() throws SocketException {
        for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            if (iface.isLoopback() || !iface.isUp()) continue;

            for (InetAddress addr : Collections.list(iface.getInetAddresses())) {
                if (addr instanceof Inet4Address) return addr.getHostAddress();
            }
        }
        return "127.0.0.1";
    }

    public static InetAddress getBroadcastAddress() throws SocketException {
    for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
        if (iface.isLoopback() || !iface.isUp()) continue;

        for (InterfaceAddress addr : iface.getInterfaceAddresses()) {
            InetAddress broadcast = addr.getBroadcast();
            if (broadcast != null) return broadcast;
        }
    }
    try {
        return InetAddress.getByName("255.255.255.255");
    } catch (Exception e) {
        return null;
    }
    }
}
