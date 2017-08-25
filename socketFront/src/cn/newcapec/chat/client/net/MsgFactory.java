package cn.newcapec.chat.client.net;


import cn.newcapec.chat.client.net.s2c.S2CMsg;

/**
 * 
 * @author WEIXING
 * @date 2014-8-24
 */
public class MsgFactory {

	private static S2CMsg[] msgs = new S2CMsg[65536];

	public static void add(S2CMsg msg) {
		msgs[msg.msgid] = msg;
	}

	public static S2CMsg create(int msgid) {
		if (msgs[msgid] == null) {
			return null;
		}
		return msgs[msgid].clone();
	}

}
