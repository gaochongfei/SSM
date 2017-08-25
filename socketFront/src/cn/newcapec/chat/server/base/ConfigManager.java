package cn.newcapec.chat.server.base;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * 配置管理器
 *
 * @author WEIXING
 * @date 2016.06.12
 */
public class ConfigManager {

    private static ConfigManager instance;

    /**
     * 获得配置管理器
     *
     * @return
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private Properties config;

    private ConfigManager() {
        config = new Properties();
        try {
            //读取内部配置文件
            /*config.load(this.getClass().getResourceAsStream("/conf/config_service.properties"));*/
            //读取外部部配置文件
            String filePath = URLDecoder.decode(ConfigManager.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
            filePath = filePath.substring(0, filePath.lastIndexOf('/'));
            filePath = filePath + "/conf/config_service.properties";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
