package com.fast.framework.component.spring.config;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fast.framework.component.spring.bean.FastActionContext;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 设置json转化器
 * 
 * @author lion.chen
 * @version 1.0.0 2017年6月28日 下午5:22:10
 */
@Configuration
public class FastBeanConfig {

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		
		FastJsonHttpMessageConverter fastConvert = new FastJsonHttpMessageConverter();
		
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(
					SerializerFeature.PrettyFormat, 
					SerializerFeature.WriteNullStringAsEmpty,
					SerializerFeature.WriteNullNumberAsZero,
					SerializerFeature.WriteNullBooleanAsFalse);
		
		fastConvert.setFastJsonConfig(fastJsonConfig);
		
		return new HttpMessageConverters((HttpMessageConverter<?>) fastConvert);
	}
	
	/**
	 * Feign请求公共参数设置
	 * @return
	 */
	@Bean
	public RequestInterceptor getCommonFeignRequestInterceptor(){
		return new RequestInterceptor(){
			@Override
			public void apply(RequestTemplate template) {
				FastActionContext ctx = FastActionContext.getActionContext();
				if(ctx != null){
					ctx.setCommonRequestParam();
				}
			}
		};
	}

}
