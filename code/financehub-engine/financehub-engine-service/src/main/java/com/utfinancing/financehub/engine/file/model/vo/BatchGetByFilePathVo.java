package com.utfinancing.financehub.engine.file.model.vo;

import com.utfinancing.financehub.engine.file.model.dto.FilezFileDto;
import lombok.Data;

import java.util.List;
/**
 *
 * @author ex-dingjie
 * @date 2023/12/7
 * @desc 封装通过path获取的文件列表以及文件总数
 *
 */
@Data
public class BatchGetByFilePathVo {

    private List<FilezFileDto> fileInfoList;

    private Integer total;

    public BatchGetByFilePathVo(List<FilezFileDto> fileInfoList, Integer total) {
        this.fileInfoList = fileInfoList;
        this.total = total;
    }

    public BatchGetByFilePathVo() {
    }
}
