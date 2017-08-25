package cn.newcapec.chat.client.base;

import cn.newcapec.chat.client.net.handler.S2CHandler;
import cn.newcapec.chat.client.net.s2c.S2CMsg;

import java.util.List;
import java.util.Vector;

/**
 * 应用管理服务
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class AppServer extends Thread {

	public static AppServer instance = new AppServer();

	private Vector<S2CMsg> c2s_msgs = new Vector<S2CMsg>();
	private Object c2s_lock = new Object();
	public static boolean isAppRun = true;

	private AppServer() {
	}

	public static void init() {
		instance.start();
	}

	private void waitSleep(long s) {
		try {
			Thread.sleep(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (isAppRun) {
			try {
				if (!runS2CMsg()) {
					waitSleep(1); // 睡眠1秒
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean runS2CMsg() {
		List<S2CMsg> msgs = getC2S();
		if (msgs == null) {
			return false;
		}
		S2CMsg msg;
		S2CHandler handler;
		int size = msgs.size();
		if (size == 0) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			msg = msgs.get(i);
			handler = msg.getHandler();
			if (handler == null) {
				continue;
			}
			try {
				handler.handle(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		msgs.clear();
		msgs = null;
		return true;
	}

	public List<S2CMsg> getC2S() {
		List<S2CMsg> list = null;
		synchronized (c2s_lock) {
			if (c2s_msgs.size() > 0) {
				list = c2s_msgs;
				c2s_msgs = new Vector<S2CMsg>();
			}
		}
		return list;
	}

	public void pushS2C(S2CMsg msg) {
		synchronized (c2s_lock) {
			c2s_msgs.add(msg);
		}
	}
}
