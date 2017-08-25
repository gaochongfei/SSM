package cn.newcapec.tools.cache;

import java.util.Date;

public interface Cache {
	public static String SERVER_URL_SPLITOR = ",";
	public static final String LOCK_PREFIX = "CACHELOCK";
	public static final String KEY_LOCK_SPLITOR = "@SPLITOR@";
	public static final long THREAD_SLEEP = 100;
	public static final int DEFAULT_EXPIRE_SECONDS = 60;
	void set(String key, Object value);
	void set(String key, Object value, Date expiry);
	void set(String key, Object value, int seconds);
	void delete(String key);
	Object get(String key);
	Object get(String key, long waitms);
	void addLock(String key);
	void removeLock(String key);
	long locktime(String key);
	Object getClient();
}
