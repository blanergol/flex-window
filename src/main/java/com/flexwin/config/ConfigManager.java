package com.flexwin.config;

import org.ini4j.Ini;
import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.ini";
    private Ini ini;

    public ConfigManager() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            ini = new Ini(new File(CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            ini = new Ini();
        }
    }

    public void saveConfig() {
        try {
            ini.store(new File(CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConfigValue(String section, String key, String defaultValue) {
        Ini.Section iniSection = ini.get(section);
        if (iniSection == null) {
            iniSection = ini.add(section);
        }
        return iniSection.get(key, defaultValue);
    }

    public void setConfigValue(String section, String key, String value) {
        Ini.Section iniSection = ini.get(section);
        if (iniSection == null) {
            iniSection = ini.add(section);
        }
        iniSection.put(key, value);
    }
}
