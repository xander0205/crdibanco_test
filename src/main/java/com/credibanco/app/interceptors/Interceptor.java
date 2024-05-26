package com.credibanco.app.interceptors;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("interceptorGeneral")
public class Interceptor implements HandlerInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(Interceptor.class);
	
	@Autowired
	private Environment messages;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info(messages.getProperty("message.log.interceptor.pre")+" "+((HandlerMethod) handler).getMethod().getName()+" "+ new Date());
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info(messages.getProperty("message.log.interceptor.pos")+" "+((HandlerMethod) handler).getMethod().getName()+" "+  new Date());
	}

}
