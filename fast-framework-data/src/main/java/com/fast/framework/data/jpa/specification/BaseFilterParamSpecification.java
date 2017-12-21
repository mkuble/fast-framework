package com.fast.framework.data.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

public interface BaseFilterParamSpecification<T> extends Specification<T>, BasePageParam{

	String key_id = "id";
	String key_status = "status";
	String key_name = "name";
	
	String key_loginName = "loginName";
	String key_userName = "userName";
	String key_password = "password";
	
	String key_createTime = "createTime";
	String key_updateTime = "updateTime";

	String key_createBy = "createBy";
	String key_updateBy = "updateBy";

	String key_adminId = "adminId";
	String key_roleId = "roleId";
	
	String key_userId = "userId";
	String key_storeId = "storeId";
	
	String key_orderId = "orderId";
	String key_platformId = "platformId";
	
}
