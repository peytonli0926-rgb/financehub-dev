package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferThirdPartEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferThirdPartDetailMapper;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferThirdPartMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferThirdPartDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferThirdPartDetailVO;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferThirdPartDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConvertTransferThirdPartDetailServiceImpl extends ServiceImpl<ConvertTransferThirdPartDetailMapper, ConvertTransferThirdPartDetailEntity> implements IConvertTransferThirdPartDetailService {

    @Resource
    private ConvertTransferThirdPartMapper convertTransferThirdPartMapper;

    @Override
    public IPage<ConvertTransferThirdPartCheckVO> check(ConvertTransferThirdPartDetailQueryDTO dto) {
        Long transferId = dto.getTransferId();
        ConvertTransferThirdPartEntity transferThirdPart = convertTransferThirdPartMapper.selectById(transferId);

        List<ConvertTransferThirdPartDetailEntity> entities = lambdaQuery().eq(ConvertTransferThirdPartDetailEntity::getTransferId, transferId)
                .list();

        Map<String, List<String>> transferContractCodeMap = entities
                .stream()
                .collect(Collectors.groupingBy(detail -> transferThirdPart.getTransferParty(), Collectors.mapping(ConvertTransferThirdPartDetailEntity::getContractCode, Collectors.toList())));

        IPage<ConvertTransferThirdPartCheckVO> page = getBaseMapper().selectCheckPage(new PageDTO<>(dto.getPageNum(), dto.getPageSize()), transferContractCodeMap);
        for (ConvertTransferThirdPartCheckVO record : page.getRecords()) {
            // set client code
            for (ConvertTransferThirdPartDetailEntity detail : entities) {
                if (record.getContractCode().equals(detail.getContractCode())) {
                    record.setClientCode(detail.getClientCode());
                    record.setClientName(detail.getClientName());
                }
            }
        }
        return page;
    }
}
