package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.entity.FileRecordEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.dto.FileRecordQueryDTO;

import java.util.List;

/**
 * @Author : jnc
 * @Date : Create in 2024-04-29
 * @Description : FileRecord服务类接口
 * @Modified :
 */
public interface IFileRecordService extends IService<FileRecordEntity> {

    IPage<FileRecordEntity> selectFileListByModuleAndBusiness(FileRecordQueryDTO queryDTO);
}
