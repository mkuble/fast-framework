package com.fast.framework.component.spring;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;

@EnableDiscoveryClient
@EnableFeignClients("com.fast.**.feignClient")
public class ServiceClientConfiguration {

	/**
	 * feignClient 拦截控制
	 * 
	 * @param manager
	 * @param config
	 * @param optionalArgs
	 * @param context
	 * @return
	 */
	@Bean(destroyMethod = "shutdown")
	@org.springframework.cloud.context.config.annotation.RefreshScope
	public EurekaClient eurekaClient(ApplicationInfoManager manager, EurekaClientConfig config, DiscoveryClient.DiscoveryClientOptionalArgs optionalArgs,
			ApplicationContext context) {

//		InstanceInfo info = manager.getInfo(); // force initialization

		return new CloudEurekaClient(manager, config, optionalArgs, context);
	}

}