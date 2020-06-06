package com.zsl.upmall.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.zsl.upmall.context.RequestContext;
import com.zsl.upmall.context.RequestContextMgr;
import com.zsl.upmall.service.RedisService;
import com.zsl.upmall.util.MStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CenterAdminLoginInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CenterAdminLoginInterceptor.class);

	@Autowired
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURL().toString();
		String token = request.getHeader("token");
		if(StringUtils.isEmpty(token)) {//尝试从查询参数获取
			token = request.getParameter("token");
			if(StringUtils.isEmpty(token)) {
				response.setHeader("Content-Type", "application/json;charset=utf-8");
				response.getWriter().print("{\"code\":401,\"message\":\""+"请先登录"+"\"}");
				response.getWriter().flush();
				return false;
			}
		}
		String s = redisService.get(token);
		if(StringUtils.isEmpty(redisService.get(token))){
			response.setHeader("Content-Type", "application/json;charset=utf-8");
			response.getWriter().print("{\"code\":401,\"message\":\""+"请先登录"+"\"}");
			response.getWriter().flush();
			return false;
		}
		//校验token
		return doSetContext(request,response,token);
	}

	/**
	 * 
	 * doSetContext:(设置上下文). <br/>
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @since
	 */
	private boolean doSetContext(HttpServletRequest request, HttpServletResponse response,String token) throws IOException {
		RequestContext requestContext = RequestContextMgr.getLocalContext();
		if (null == requestContext) {
			requestContext = new RequestContext(request, response);
			RequestContextMgr.setLocalContext(requestContext);
		}
		LOGGER.info("令牌："+token);
		String jsonStr = (String) redisService.get(token);
		String tokenTmp = StringEscapeUtils.unescapeEcmaScript(jsonStr);
		String targetStr =  MStringUtils.trimFirstAndLastChar(tokenTmp,'"');
		LOGGER.info("登录字符串："+targetStr);
		JSONObject jsonObject = JSONObject.parseObject(targetStr);
        Integer userId = Integer.parseInt(jsonObject.get("userId").toString());
        String sessionKey = jsonObject.get("sessionKey").toString();

		requestContext.setToken(token);
		requestContext.setRequest(request);
		requestContext.setResponse(response);
		requestContext.setUserId(userId);
		requestContext.setSessionKey(sessionKey);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}

