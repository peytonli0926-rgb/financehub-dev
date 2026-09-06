package com.utfinancing.financehub.engine.file.model.dto;

import lombok.Data;

/**
 * @author iwen
 * @date 2023/11/7 14:41
 */
@Data
public class AttachmentPreviewUrlInfoDto {
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
	 * 文件预览Url 0代表不支持预览，为1代表获取预览地址时失败
	 */
	private String filePreview;

	/**
	 * 文件预览Url 0代表不支持预览，为1代表获取预览地址时失败
	 */
	private String mobileFilePreview;
}
