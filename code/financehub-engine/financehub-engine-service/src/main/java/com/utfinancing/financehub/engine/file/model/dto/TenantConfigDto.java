package com.utfinancing.financehub.engine.file.model.dto;

import lombok.Data;

/**
 * @author iwen
 * @date 2023/11/6 13:46
 */
@Data
public class TenantConfigDto {

	/**
	 * 租户编号
	 */
	private String tenantCode;

	/**
	 * 联想云盘根路径
	 */
	private String rootPath;

	/**
	 * nas根路径
	 */
	private String nasRootPath;

	/**
	 * 联想云盘账号信息
	 */
	private FilezAccountDto secret;


	private String downloadOrder;

	private String uploadOrder;
}