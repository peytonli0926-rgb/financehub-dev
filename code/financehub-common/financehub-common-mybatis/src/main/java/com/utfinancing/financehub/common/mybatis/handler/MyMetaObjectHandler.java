package com.utfinancing.financehub.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.utfinancing.financehub.admin.api.model.LoginUser;
import com.utfinancing.financehub.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		String staffCode = "system";
		if (SecurityUtils.getLoginUser() != null){
			staffCode = SecurityUtils.getLoginUser().getStaffCode();
		}
		this.strictInsertFill(metaObject, "createBy", String.class, staffCode);
		this.strictInsertFill(metaObject, "updateBy", String.class, staffCode);
		this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, "delFlag", String.class, "0");
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		String staffCode = "system";
		if (SecurityUtils.getLoginUser() != null){
			staffCode = SecurityUtils.getLoginUser().getStaffCode();
		}
		this.strictInsertFill(metaObject, "updateBy", String.class, staffCode);
		this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
	}
}
