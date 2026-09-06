package com.utfinancing.financehub.engine.file.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 云盘上传文件返回对象
 */
@Data
public class FilezUploadVo {
    /**
     * 错误码，成功：0
     */
    private String errCode;

    /**
     * 错误描述，成功：ok
     */
    private String errMsg;

    /**
     * 文件备注
     */
    private String desc;

    /**
     * 是否为文件夹
     */
    private boolean dir;

    /**
     * 最后修改时间
     */
    private LocalDateTime modified;

    /**
     * 文件 id
     */
    private Long neid;

    /**
     * 命名空间 id
     */
    private Integer nsid;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 路径类型: ent 企业空间，self 个人空间
     */
    private String pathType;

    /**
     * 文件版本
     */
    private String rev;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建者 id
     */
    private String creatorUid;

    /**
     * 更新者
     */
    private String updator;

    /**
     * 更新者 id
     */
    private String updatorUid;
}
