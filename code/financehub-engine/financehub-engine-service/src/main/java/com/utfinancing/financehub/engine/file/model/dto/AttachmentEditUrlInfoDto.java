package com.utfinancing.financehub.engine.file.model.dto;

import lombok.Data;

/**
 * @author iwen
 * @date 2023/11/7 14:29
 */
@Data
public class AttachmentEditUrlInfoDto {
	/**
	 * 附件id
	 */
	private Long id;

	/**
	 * 附件编号
	 */
	private String fileCode;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 在线编辑url，0代表不支持在线编辑，为1代表获取在线编辑地址时失败
	 */
	private String fileEditUrl;
}
