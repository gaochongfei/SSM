package cn.newcapec.chat.server.utils;

import cn.newcapec.chat.server.base.ConfigManager;

/**
 * 记录日志
 * @author WEIXING
 *
 */
public abstract class Log {

	private static boolean isDebug = ConfigManager.getInstance().getBoolean("mode.debug");

//	public static Logger logger = LoggerFactory.getLogger(Log.class);

	public static void debug(Object message){
		if(isDebug){
			if(message instanceof Exception){
				((Exception)message).printStackTrace();
			}else System.out.println(message);
		}else{
//			if(message instanceof Exception){
//				logger.debug(getStackTrace((Exception)message));
//			}else logger.debug(message.toString());
		}
	}
	public static void info(Object message){
		if(isDebug){
			if(message instanceof Exception){
				((Exception)message).printStackTrace();
			}else System.out.println(message);
		}else{
//			if(message instanceof Exception){
//				logger.info(getStackTrace((Exception)message));
//			}else logger.info(message.toString());
		}
	}
	public static void warn(Object message){
		if(isDebug){
			if(message instanceof Exception){
				((Exception)message).printStackTrace();
			}else System.out.println(message);
		}else{
//			if(message instanceof Exception){
//				logger.warn(getStackTrace((Exception)message));
//			}else logger.warn(message.toString());
		}
	}
	public static void error(Object message){
		if(isDebug){
			if(message instanceof Exception){
				((Exception)message).printStackTrace();
			}else System.out.println(message);
		}else{
//			if(message instanceof Exception){
//				logger.error(getStackTrace((Exception)message));
//			}else logger.error(message.toString());
		}
	}
	
	private static String getStackTrace(Exception exception){
		StringBuffer st = new StringBuffer();
		StackTraceElement[] stack = exception.getStackTrace();
		st.append(exception.getMessage());
		for (int i = 0; i < stack.length; i++) {
			st.append("\n	");
			st.append(stack[i].toString());
		}
		return st.toString();
	}
}
