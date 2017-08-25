package cn.newcapec.chat.client.net.handler;

import cn.newcapec.chat.client.net.c2s.C2STest;
import cn.newcapec.chat.client.net.s2c.S2CMsg;
import cn.newcapec.chat.client.net.s2c.S2CTest;

public class S2CTestHandler implements S2CHandler {
	
	//4003
	public void handle(S2CMsg m) throws Exception {
		S2CTest msg = (S2CTest)m;
		/*C2STest c2sTest = new C2STest();
		c2sTest.msg = "01";
		msg.e.getChannel().write(c2sTest);*/
	}

}
