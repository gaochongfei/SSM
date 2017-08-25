package cn.newcapec.chat.server.net;

import cn.newcapec.chat.server.net.c2s.C2SMsg;

/**
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class MsgFactory {

	private static C2SMsg[] msgs = new C2SMsg[65536];

	public static void add(C2SMsg msg) {
		msgs[msg.msgid] = msg;
	}

	public static C2SMsg create(int msgid) {
		if (msgs[msgid] == null) {
			return null;
		}
		return msgs[msgid].clone();
	}

}
