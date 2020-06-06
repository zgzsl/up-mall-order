package com.zsl.upmall.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContext {

	/**
	 * 请求对象
	 */
	private HttpServletRequest request;

	/**
	 * 响应对象
	 */
	private HttpServletResponse response;

	/**
	 *token
	 */
	private String token;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 微信登录sessionKey
	 */
	private String sessionKey;

	public RequestContext(HttpServletRequest request,HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}

