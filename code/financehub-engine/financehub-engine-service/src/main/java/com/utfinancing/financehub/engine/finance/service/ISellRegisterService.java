package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.SellRegisterQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.SellRegisterDTO;
import com.utfinancing.financehub.engine.finance.model.vo.SellRegisterExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.SellRegisterVO;
import com.utfinancing.financehub.engine.finance.entity.SellRegisterEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-03
 * @Description : SellRegister服务类接口
 * @Modified :
 */
public interface ISellRegisterService extends IService<SellRegisterEntity> {

    Long saveSellRegister(SellRegisterDTO dto);

    Long updateSellRegister(Long id, SellRegisterDTO dto);

    SellRegisterDTO getSellRegisterDTOById(Long id);

    IPage<SellRegisterVO> selectPage(SellRegisterQueryDTO queryDTO);

    List<SellRegisterVO> selectList(SellRegisterQueryDTO queryDTO);

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
     * 根据资产编号查询出售登记
     * @param assetNumber
     * @return
     */
    SellRegisterVO getByAssetNumber(String assetNumber);

    void updateProcessStatus(CommonApproveDTO approveDTO);
}
