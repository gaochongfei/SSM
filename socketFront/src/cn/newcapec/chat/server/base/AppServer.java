package cn.newcapec.chat.server.base;

import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.handler.C2SHandler;

import java.util.List;
import java.util.Vector;

/**
 * 应用管理服务
 * @author WEIXING
 * @date 2014-8-24
 */
public class AppServer extends Thread {

	public static final AppServer instance = new AppServer();

	private Vector<C2SMsg> c2s_msgs = new Vector<C2SMsg>();
	private Object c2s_lock = new Object();

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
		while (true) {
			try {
				if (!runC2SMsg()) {
					waitSleep(1); // 睡眠1毫秒
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean runC2SMsg() {
		List<C2SMsg> msgs = getC2S();
		if (msgs == null) {
			return false;
		}
		C2SMsg msg;
		C2SHandler handler;
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

	public List<C2SMsg> getC2S() {
		List<C2SMsg> list = null;
		synchronized (c2s_lock) {
			if (c2s_msgs.size() > 0) {
				list = c2s_msgs;
				c2s_msgs = new Vector<C2SMsg>();
			}
		}
		return list;
	}

	public void pushC2S(C2SMsg msg) {
		synchronized (c2s_lock) {
			c2s_msgs.add(msg);
		}
	}

}
