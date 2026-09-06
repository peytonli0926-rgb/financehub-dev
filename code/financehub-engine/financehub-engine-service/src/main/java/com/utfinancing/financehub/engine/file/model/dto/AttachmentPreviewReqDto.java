package com.utfinancing.financehub.engine.file.model.dto;

import lombok.Data;

/**
 * @author iwen
 * @date 2023/7/11 14:11
 */
@Data
public class AttachmentPreviewReqDto {
    private Long neid;

    private Integer nsid;

    private String rev;
}
