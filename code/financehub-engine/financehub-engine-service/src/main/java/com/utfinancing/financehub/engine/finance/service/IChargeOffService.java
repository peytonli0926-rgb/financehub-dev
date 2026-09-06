package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.ChargeOffVO;
import com.utfinancing.financehub.engine.finance.entity.ChargeOffEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author : bruyang
 * @Date : Create in 2024-02-27
 * @Description : ChargeOff服务类接口
 * @Modified :
 */
public interface IChargeOffService extends IService<ChargeOffEntity> {

    Long saveChargeOff(ChargeOffDTO dto);

    Long updateChargeOff(Long id, ChargeOffDTO dto);

    ChargeOffDTO getChargeOffDTOById(Long id);

    IPage<ChargeOffVO> selectPage(ChargeOffQueryDTO queryDTO);

    Boolean importTemplate(MultipartFile file, String type);

    Boolean deleteByIds(List<Long> idList);

    Boolean submit(List<Long> idList);

    Boolean withdraw(List<Long> idList);

    Boolean updateProcessStatus(CommonApproveDTO approveDTO);

    IPage<ChargeOffVO> summaryPage(ChargeOffQueryDTO queryDTO);

    IPage<ChargeOffVO> summaryDetailPage(ChargeOffDetailQueryDTO queryDTO);

    Map<String, String> summaryList(ChargeOffQueryDTO queryDTO);

    /**
     * 数仓chargeoff固化
     * @param queryDTO
     * @return
     */
    void DwChargeoffSolidified(ChargeOffQueryDTO queryDTO);

    void summaryReportByPeriodCode(int periodCode);

    boolean isGenerateBalanceMonth(int periodCode);
}
