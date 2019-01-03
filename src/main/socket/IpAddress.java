package main.socket;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class IpAddress {

    //Runs windows script to retrieve ipAddress
    public static String getWindowsIP() {
        String result = "";
        // String windowsCommand = "cmd /C ipconfig | findstr /r \"[0-9][0-9]*\\.[0-9][0-9]*\\.[0-9][0-9]*\\.[0-9][0-9]*\"";
        String ethernetCommand = "cmd /C netsh interface ip show addresses \"Ethernet\" | findstr \"Ip Address\"";
        String wifiCommand = "cmd /C netsh interface ip show addresses \"Wi-Fi\" | findstr \"Ip Address\"";

        String ethernet = null;
        String wifi = null;
        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec(ethernetCommand);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s = null;
            if ((s = stdInput.readLine()) != null) {
                ethernet = s.replace("IP Address:", "").replace(" ", "");
            }
            proc = rt.exec(wifiCommand);
            stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            if ((s = stdInput.readLine()) != null) {
                wifi = s.replace("IP Address:", "").replace(" ", "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ethernet != null) return ethernet;
        if (wifi != null) return wifi;
        return "Unable to connect to the internet";
    }

    //Mac script to get ipaddress
    public static String getMacIP() {
        String result = "";
        String[] macCommand = {"/bin/sh", "-c", "ifconfig | grep 'inet ' | grep -Fv 127.0.0.1 | awk '{print $2}'"};

        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec(macCommand);
            // proc.waitFor();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                result+=s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getOS() {
        String os = System.getProperty("os.name");
        if (os.contains("Mac")) {
            os = "Mac";
        } else if (os.contains("Windows")) {
            os = "Windows";
        }
        return os;
    }

    public static String getIpAddress() {
        if (getOS().equals("Windows")) {
            return getWindowsIP();
        } else if (getOS().equals("Mac")) {
            return getMacIP();
        }
        return null;
    }


}
