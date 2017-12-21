package com.fast.framework.component.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fast.framework.component.spring.bean.FastActionContext;

/**
 * 参数过滤拦截
 * 
 * @author lion.chen
 * @version 1.0.0 2017年7月4日 上午11:22:51
 */
public class FastActionParamInterceptor extends HandlerInterceptorAdapter{

	/**
	 * 处理之前
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		FastActionContext.createActionContext(request, response);
		
		return super.preHandle(request, response, handler);
	}
	
	/**
	 * 处理之后
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler, ModelAndView modelAndView) throws Exception {

		FastActionContext.destroyActionContext();
		
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
	
}
