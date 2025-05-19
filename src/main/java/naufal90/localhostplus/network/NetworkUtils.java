package naufal90.localhostplus.util;

import java.net.*;
import java.util.Enumeration;

public class NetworkUtils {
    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("[LocalHostPlus] Failed to get local IP: " + ex.getMessage());
        }
        return "127.0.0.1"; // fallback
    }
}
