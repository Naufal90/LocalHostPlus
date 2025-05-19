package naufal90.localhostplus.util;

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

    public static String getBroadcastAddress() throws SocketException {
        for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            if (iface.isLoopback() || !iface.isUp()) continue;

            for (InterfaceAddress addr : iface.getInterfaceAddresses()) {
                InetAddress broadcast = addr.getBroadcast();
                if (broadcast != null) return broadcast.getHostAddress();
            }
        }
        return "255.255.255.255";
    }
}
