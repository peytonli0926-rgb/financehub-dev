package com.utfinancing.financehub.engine.finance.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class NonLeaseResultUploadDTO implements Serializable {
    @ApiModelProperty(value = "上传的文件")
    private MultipartFile file;

    @ApiModelProperty(value = "是否已经弹过警告框(0:表示未弹出过警告框， 1：表示已经弹出过警告框)")
    private String isWaring;
}
