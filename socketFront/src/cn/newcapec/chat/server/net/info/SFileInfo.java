package cn.newcapec.chat.server.net.info;

public class SFileInfo extends INetInfo {
	public SFileInfo() {
	}

	public String orgName;
	public String orgCode;
	public String reserveDomain4001;//保留域
	public String accountDate;
	public String reserveDomain4002;//保留域

	public String fileName;//文件名
	public String fileMark;//文件摘要
	public String fileSize;//文件大小
	public String reserveDomain4003;//保留域

	public String flag;//标识位
	public byte[] dataBlock;//数据块

	public String fileSize4005;//已接收的文件大小
	public String resultCode4005;//应答码

	public String fileCount;//需下载的文件个数
	public String resultCode4006;//应答码

	public String endFlag;//结束标识符

	public String resultCode4008;//应答码

	public byte[] totalBlock;//总数据块
	public int restFileCount;//剩余的文件个数
	public int restFileSize;//剩余的文件大小

}
