package cn.newcapec.tools.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class SysEnv {
    public static boolean MODE_DEBUG = false;
    public static String CACHE_URL = "cache.url";

    private static String CONFIG_FILE = "conf/config_service.properties";

    private static Properties config;
    private static SysEnv instatnce = new SysEnv();

    protected SysEnv() {
        load();
    }

    public static SysEnv getInstance() {
        return instatnce;
    }

    private void load() {
        try { // 加载配置
            Properties properties = new Properties();
            properties.load(new InputStreamReader(SysEnv.class.getResourceAsStream("/" + CONFIG_FILE), "UTF-8"));
            Properties config2 = config;
            config = properties;
            if (config2 != null) {
                config2.clear();
            }
        } catch (IOException e) {
            Log.error(e);
        }
        // 初始化静态变量
        MODE_DEBUG = this.getBoolean("mode.debug", false);
    }

    public void reload() {
        load();
    }

    public String getString(String key) {
        return config.getProperty(key);
    }

    public String getString(String key, String def) {
        String value = config.getProperty(key);
        return value == null ? def : value;
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int def) {
        int value = def;
        try {
            value = Integer.parseInt(config.getProperty(key));
        } catch (Exception e) {
            Log.error(e);
        }
        return value;
    }

    public long getLong(String key) {
        return getLong(key, -1);
    }

    public long getLong(String key, long def) {
        long value = def;
        try {
            value = Long.parseLong(config.getProperty(key));
        } catch (Exception e) {
            Log.error(e);
        }
        return value;
    }

    public double getDouble(String key) {
        return getDouble(key, -1);
    }

    public double getDouble(String key, double def) {
        double value = def;
        try {
            value = Double.parseDouble(config.getProperty(key));
        } catch (Exception e) {
            Log.error(e);
        }
        return value;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        boolean value = def;
        try {
            value = Boolean.parseBoolean(config.getProperty(key));
        } catch (Exception e) {
        }
        return value;
    }

}
