package cn.newcapec.tools.cache.map;

import cn.newcapec.tools.cache.Cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地缓存
 * @author WEIXING
 *
 */
public class MapCache implements Cache {

	private static MapCache mapCache;
	private final Map<String, Object> mMap = new HashMap<String, Object>();

	public static MapCache getInstance() {
		if(mapCache == null){
			mapCache = new MapCache();
		}
		return mapCache;
	}

	
	public void set(String key, Object value) {
		mMap.put(key, value);
	}

	
	public void set(String key, Object value, Date expiry) {
		mMap.put(key, value);
	}

	
	public void set(String key, Object value, int seconds) {
		mMap.put(key, value);
	}

	public void delete(String key) {
		mMap.remove(key);
	}

	public Object get(String key) {
		return mMap.get(key);
	}

	public Object get(String key, long waitms) {
		return mMap.get(key);
	}

	public void addLock(String key) {
		mMap.put(key + "_lock", 1l);
	}

	public void removeLock(String key) {
		mMap.remove(key + "_lock");
	}

	public long locktime(String key) {
		if (mMap.containsKey(key + "_lock")) {
			return Long.valueOf(mMap.get(key + "_lock").toString());
		}
		return 0;
	}

	public Object getClient() {
		return mMap;
	}

}
