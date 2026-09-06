package com.utfinancing.financehub.engine.file.model.vo;

import com.utfinancing.financehub.engine.file.model.dto.FilezNameDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author liuyouyuan
 * @date 2023/12/12
 * @desc 云盘文件列表以及文件总数返回对象
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilezFileListVo {

    private List<FilezNameDto> fileInfoList;

    private Integer total;

}
