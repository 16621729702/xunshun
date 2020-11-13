package com.wink.livemall.admin.util;

/**
 * 响应体response
 */
public class JsonResult {
	/** 操作成功 */
	public static final String SUCCESS = "200";
	/** 操作失败 */
	public static final String ERROR = "500";


	/** 状态 */
	private String code;
	/** 对应状态的消息 */
	private String msg;
	/** 具体业务数据 */
	private Object data;

	public JsonResult(String code,String message) {
		this.code = code;
		this.msg = message;
		this.data = "";
	}

	/** 此构造方法应用于data为null的场景 */
	public JsonResult() {
		this.code = SUCCESS;
		this.msg = "OK";
		this.data = "";
	}

	/** 有具体业务数据返回时,使用此构造方法 */
	public JsonResult(Object data) {
		this();
		this.data = data!=null?data:"";
	}

	/** 出现异常以后要调用此方法封装异常信息 */
	public JsonResult(Throwable t) {
		this.code = ERROR;
		this.msg = t.getMessage();
		this.data = "";
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public String getCode() {
		return code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
