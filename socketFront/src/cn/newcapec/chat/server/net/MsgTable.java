package cn.newcapec.chat.server.net;

import cn.newcapec.chat.server.net.c2s.*;

public class MsgTable {

	public static void init() {
		MsgFactory.add(new C2STest());
		MsgFactory.add(new C2S4001());
		MsgFactory.add(new C2S4002());
		MsgFactory.add(new C2S4003());
		MsgFactory.add(new C2S4004());
		MsgFactory.add(new C2S4005());
		MsgFactory.add(new C2S4006());
		MsgFactory.add(new C2S4007());
		MsgFactory.add(new C2S4008());
	}
}
