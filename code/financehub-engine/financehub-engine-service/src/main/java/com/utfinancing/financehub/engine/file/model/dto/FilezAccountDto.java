package com.utfinancing.financehub.engine.file.model.dto;

import lombok.Data;

/**
 * @author iwen
 * 联想云盘账号信息
 * @date 2023/11/6 13:49
 */
@Data
public class FilezAccountDto {

	/**
	 * appKey
	 */
	private String appKey;

	/**
	 * 秘钥
	 */
	private String appSecret;

	/**
	 * 登录账号
	 */
	private String slug;
}
