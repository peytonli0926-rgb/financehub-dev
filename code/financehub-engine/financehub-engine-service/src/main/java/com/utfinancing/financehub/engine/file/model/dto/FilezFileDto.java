package com.utfinancing.financehub.engine.file.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 云盘返回对象
 */
@Data
public class FilezFileDto {


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
    private String neid;

    /**
     * 命名空间 id
     */
    private String nsid;

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