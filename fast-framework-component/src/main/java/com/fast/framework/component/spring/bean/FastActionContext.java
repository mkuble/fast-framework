package com.fast.framework.component.spring.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 当前线程存储数据ActionContext
 * 
 * @author lion.chen
 * @version 1.0.0 2017年7月4日 上午11:21:24
 */
public class FastActionContext {

	private static final ThreadLocal<FastActionContext> actionContextThreadLocal = new ThreadLocal<FastActionContext>();
	
	/**
     * 当前对象只给自己实例
     */
    private FastActionContext(){}
	
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    public static FastActionContext getActionContext() {
        return actionContextThreadLocal.get();
    }
    
    public static void createActionContext(HttpServletRequest request, HttpServletResponse response){
    	
    	FastActionContext ctx = new FastActionContext();
        
    	ctx.request = request;
        ctx.response = response;
        
        ctx.setCommonRequestParam();
        
        actionContextThreadLocal.set(ctx);
    }
    
    /**
     * 设置一些公共参数
     * 	-通常的做法（把数据放置requestHead）
     */
    public void setCommonRequestParam(){
    	
    }
    
    public static void destroyActionContext() {
        actionContextThreadLocal.remove();
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
	
	public HttpSession getHttpSession() {
        return request == null ? null : request.getSession();
    }
	
}
