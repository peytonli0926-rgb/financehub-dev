package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.OrgClaimEbankNoQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.OrgClaimEbankNoDTO;
import com.utfinancing.financehub.engine.finance.model.vo.OrgClaimEbankNoVO;
import com.utfinancing.financehub.engine.finance.entity.OrgClaimEbankNoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author : robjiang
 * @Date : Create in 2025-06-06
 * @Description : OrgClaimEbankNo服务类接口
 * @Modified :
 */
public interface IOrgClaimEbankNoService extends IService<OrgClaimEbankNoEntity> {

    Long saveOrgClaimEbankNo(OrgClaimEbankNoDTO dto);

    Long updateOrgClaimEbankNo(Long id, OrgClaimEbankNoDTO dto);

    OrgClaimEbankNoDTO getOrgClaimEbankNoDTOById(Long id);

    IPage<OrgClaimEbankNoVO> selectPage(OrgClaimEbankNoQueryDTO queryDTO);

    /**
     * 同步机构认领的网银编号
     */
    public Map<String, Object> orgClaimEbankNoSync(Date date);
}
