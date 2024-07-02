package com.flexwin.startup;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public class StartupManager {
    private static final String REGISTRY_KEY = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    private static final String APP_NAME = "FlexWindow";

    public void addAppToStartup(String path) {
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, REGISTRY_KEY, APP_NAME, path);
    }

    public void removeAppFromStartup() {
        Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, REGISTRY_KEY, APP_NAME);
    }
}
