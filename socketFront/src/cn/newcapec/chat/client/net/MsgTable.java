package cn.newcapec.chat.client.net;

import cn.newcapec.chat.client.net.s2c.*;

public class MsgTable {

	public static void init() {
		MsgFactory.add(new S2CTest());
		MsgFactory.add(new S2C4003());
		MsgFactory.add(new S2C4004());
		MsgFactory.add(new S2C4005());
		MsgFactory.add(new S2C4006());
		MsgFactory.add(new S2C4007());
		MsgFactory.add(new S2C4008());
	}
}