package cn.newcapec.tools.model;

import java.io.Serializable;

/**
 * json头部封装
 * 
 * @author WEIXING
 *
 */
public class JsonHead implements Serializable {

	private static final long serialVersionUID = 1L;
	private String respcode;
	private String respinfo;
	private String key;
	private Object objson;

	public String getRespcode() {
		return respcode;
	}

	public void setRespcode(String respcode) {
		this.respcode = respcode;
	}

	public String getRespinfo() {
		return respinfo;
	}

	public void setRespinfo(String respinfo) {
		this.respinfo = respinfo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getObjson() {
		return objson;
	}

	public void setObjson(Object objson) {
		this.objson = objson;
	}

}
