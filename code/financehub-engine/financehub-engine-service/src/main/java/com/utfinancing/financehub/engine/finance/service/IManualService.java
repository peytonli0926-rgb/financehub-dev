package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ManualQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ManualDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVO;
import com.utfinancing.financehub.engine.finance.entity.ManualEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utfinancing.financehub.engine.finance.model.vo.ManualVoucherVO;
import com.utfinancing.financehub.engine.model.dto.CommonApproveDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author : bruyang
 * @Date : Create in 2024-01-10
 * @Description : Manual服务类接口
 * @Modified :
 */
public interface IManualService extends IService<ManualEntity> {

    Long saveManual(ManualDTO dto);

    Long updateManual(Long id, ManualDTO dto);

    ManualDTO getManualDTOById(Long id);

    IPage<ManualVO> selectPage(ManualQueryDTO queryDTO);

    Boolean withdraw(List<Long> idList);

    Boolean submit(List<Long> idList);

    Boolean updateStatus(CommonApproveDTO approveDTO);

    String importTemplate(MultipartFile file);

    Boolean deleteByIds(List<Long> idList);

    List<ManualVoucherVO> getManualVoucherById(Long id);

    Boolean writeOff(List<Long> idList);

    Boolean copy(List<Long> idList);

    String extenalDataCheck(ManualDTO dto);


    /**
     * 批量更新手工凭证的审核状态
     */
    public void updateStatusByids(List<String> ids, String status, String recheckUserNo, String recheckUserName);

    /**
     * 取得凭证号
     */
    public long generateVoucherNum(String voucherType, LocalDateTime dateTime);

    /**
     * 生成正式凭证
     */
    public void generateVoucher(Long id,String approveName,String approveNum,Boolean isNegative,String businessScence);

    String importTemplateCheck(MultipartFile file);
}
