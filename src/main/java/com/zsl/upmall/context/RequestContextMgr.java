package com.zsl.upmall.context;

public class RequestContextMgr {

	protected static final ThreadLocal<RequestContext> LOCAL_CONTEXT = new ThreadLocal<RequestContext>();
	
	/**
	 * 
	 * setLocalContext:(设置请求上下文环境). <br/>
	 *
	 * @author cwb
	 * @param context
	 * @since
	 */
	public static void setLocalContext(RequestContext context) {
		LOCAL_CONTEXT.set(context);
	}

	/**
	 * 
	 * getLocalContext:(获取请求上下文环境). <br/>
	 *
	 * @author cwb
	 * @return
	 * @since
	 */
	public static RequestContext getLocalContext() {
		return LOCAL_CONTEXT.get();
	}
	
	/**
	 * 
	 * clearContext:(清空请求上下文环境). <br/>
	 *
	 * @author cwb
	 * @since
	 */
	public static void clearContext() {
		LOCAL_CONTEXT.remove();
	}
}

