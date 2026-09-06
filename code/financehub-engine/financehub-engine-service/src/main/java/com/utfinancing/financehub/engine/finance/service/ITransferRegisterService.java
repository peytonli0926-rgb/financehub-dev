package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.TransferRegisterDTO;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.TransferRegisterVO;
import com.utfinancing.financehub.engine.finance.entity.TransferRegisterEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description : TransferRegister服务类接口
 * @Modified :
 */
public interface ITransferRegisterService extends IService<TransferRegisterEntity> {

    Long saveTransferRegister(TransferRegisterDTO dto);

    Long updateTransferRegister(Long id, TransferRegisterDTO dto);

    TransferRegisterDTO getTransferRegisterDTOById(Long id);

    IPage<TransferRegisterVO> selectPage(TransferRegisterQueryDTO queryDTO);

    /**
     * 查询数据
     *
     * @param queryDTO
     * @return
     */
    List<TransferRegisterExcelVO> selectList(TransferRegisterQueryDTO queryDTO);

    /**
     * 上传
     *
     * @param file
     * @return
     */
    Boolean importFile(MultipartFile file);

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    Boolean submit(List<Long> ids);

    /**
     * 批量撤回
     *
     * @param ids
     * @return
     */
    Boolean withdraw(List<Long> ids);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    Boolean delete(List<Long> ids);

    /**
     * 根据资产编号查询转入登记
     *
     * @param assetNumber
     * @return
     */
    TransferRegisterVO getByAssetNumber(String assetNumber);

    /**
     * 根据资产编号list 查询
     * @param assetNumberList
     * @return
     */
    List<TransferRegisterVO> getByAssetNumberList(List<String> assetNumberList);

    /**
     * 审批修改单据状态
     *
     * @param approveDTO
     */
    void updateProcessStatus(CommonApproveDTO approveDTO);
}
