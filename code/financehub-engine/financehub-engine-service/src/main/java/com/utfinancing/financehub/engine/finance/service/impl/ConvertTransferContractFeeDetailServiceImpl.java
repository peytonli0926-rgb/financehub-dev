package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferContractFeeDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferContractFeeDetailMapper;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferContractFeeDetailExcelVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferContractFeeDetailService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ConvertTransferContractFeeDetailServiceImpl extends ServiceImpl<ConvertTransferContractFeeDetailMapper, ConvertTransferContractFeeDetailEntity> implements IConvertTransferContractFeeDetailService {
    private final IOrgCompanyService orgCompanyService;

    public ConvertTransferContractFeeDetailServiceImpl(IOrgCompanyService orgCompanyService) {
        this.orgCompanyService = orgCompanyService;
    }

    @Override
    public List<ConvertTransferContractFeeDetailExcelVO> export(Long transferId, String transferFeeType, String contractCode) {
        List<ConvertTransferContractFeeDetailEntity> entities = lambdaQuery()
                .eq(Objects.nonNull(transferId), ConvertTransferContractFeeDetailEntity::getTransferId, transferId)
                .eq(Objects.nonNull(transferFeeType), ConvertTransferContractFeeDetailEntity::getTransferFeeType, transferFeeType)
                .like(StringUtils.isNotEmpty(contractCode), ConvertTransferContractFeeDetailEntity::getContractCode, contractCode)
                .list();
        List<ConvertTransferContractFeeDetailExcelVO> excelVOS = new ArrayList<>();
        Set<String> orgIds = new HashSet<>();
        for (ConvertTransferContractFeeDetailEntity entity : entities) {
            excelVOS.add(new ConvertTransferContractFeeDetailExcelVO(entity));
            orgIds.add(entity.getOrgId());
        }
        orgCompanyService.lambdaQuery()
                .in(OrgCompanyEntity::getOrgId, orgIds)
                .select(Arrays.asList(OrgCompanyEntity::getOrgId, OrgCompanyEntity::getOrgName))
                .list()
                .forEach(orgId -> {
                    for (ConvertTransferContractFeeDetailExcelVO vo : excelVOS) {
                        if (vo.getOrgId().equals(orgId.getOrgId())) {
                            vo.setOrgId(orgId.getOrgName());
                            break;
                        }
                    }
                });

        return excelVOS;
    }
}
