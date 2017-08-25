package cn.newcapec.chat.server.net.handler;

import cn.newcapec.chat.server.net.c2s.C2SMsg;
import cn.newcapec.chat.server.net.c2s.C2STest;
import cn.newcapec.chat.server.net.s2c.S2CTest;

public class C2STestHandler implements C2SHandler {
	
	//4001
	public void handle(C2SMsg m) throws Exception {
		C2STest msg = (C2STest)m;
		S2CTest s2cTest = new S2CTest();
		s2cTest.msg = "01";
		s2cTest.len = 36 + s2cTest.msg.length();
		msg.e.getChannel().write(s2cTest);
	}

}
