package com.utfinancing.financehub.engine.file.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentVo {

    // 文件ID
    private Long id;

    // 文件名
    private String fileName;

    // 上传时间
    private LocalDateTime uploadTime;

    /**
     * 文件预览Url 0代表不支持预览，为1代表获取预览地址时失败
     */
    private String filePreview;

    /**
     * 文件缩略图预览Url 0代表不支持预览，为1代表获取预览地址时失败
     */
    private String filePreviewThumbtail;

    public AttachmentVo(Long id, String fileName, LocalDateTime uploadTime, String filePreview, String filePreviewThumbtail) {
        this.id = id;
        this.fileName = fileName;
        this.uploadTime = uploadTime;
        this.filePreview = filePreview;
        this.filePreviewThumbtail = filePreviewThumbtail;
    }
}
