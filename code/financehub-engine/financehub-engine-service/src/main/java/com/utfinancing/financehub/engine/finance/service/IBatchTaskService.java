package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.BatchTaskQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.BatchTaskDTO;
import com.utfinancing.financehub.engine.finance.model.vo.BatchTaskVO;
import com.utfinancing.financehub.engine.finance.entity.BatchTaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-06-25
 * @Description : BatchTask服务类接口
 * @Modified :
 */
public interface IBatchTaskService extends IService<BatchTaskEntity> {

    Long saveBatchTask(BatchTaskDTO dto);

    Long updateBatchTask(Long id, BatchTaskDTO dto);

    BatchTaskDTO getBatchTaskDTOById(Long id);

    IPage<BatchTaskVO> selectPage(BatchTaskQueryDTO queryDTO);

    /**
     * 判断任务是否存在
     * @param businessId
     * @param businessType
     * @return
     */
    Boolean isExistTask(Long businessId,String businessType);


    /**
     * 批量更新状态
     * @param idList 任务id
     * @param status
     * @return
     */
    void updateBatchTask(List<Long> idList,String status);

}
