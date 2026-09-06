package com.utfinancing.financehub.engine.integration.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.integration.entity.ExternalVoucherEntryEntity;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherQueryDTO;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherRespDTO;
import com.utfinancing.financehub.engine.integration.model.vo.ExternalVoucherVO;
import com.utfinancing.financehub.engine.integration.entity.ExternalVoucherEntity;
import com.utfinancing.financehub.engine.integration.mapper.ExternalVoucherMapper;
import com.utfinancing.financehub.engine.integration.service.IEasIntegrationService;
import com.utfinancing.financehub.engine.integration.service.IExternalVoucherEntryService;
import com.utfinancing.financehub.engine.integration.service.IExternalVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lettuce.core.output.ValueOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-10-31
 * @Description :  ExternalVoucher服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ExternalVoucherServiceImpl extends ServiceImpl<ExternalVoucherMapper, ExternalVoucherEntity> implements IExternalVoucherService {

    private final ExternalVoucherMapper externalVoucherMapper;
    private final IEasIntegrationService easIntegrationService;
    private final IExternalVoucherEntryService externalVoucherEntryService;

    @Override
    public List<EasVoucherRespDTO> addVoucherToEas(String systemCode, List<EasVoucherDTO> voucherEntryList) {
        //同步到金蝶
//        List<EasVoucherRespDTO> easVoucherRespDTOList = easIntegrationService.addVouchers(voucherEntryList);
        //不同步到金蝶
        List<EasVoucherRespDTO> easVoucherRespDTOList = new ArrayList<>();
        //本地入库
//        Map<String, EasVoucherRespDTO> easVoucherRespDTOMap = easVoucherRespDTOList.stream().collect(Collectors.toMap(e->e.getVoucherNumber(), e->e));
        Map<String, List<EasVoucherDTO>> voucherDTOMap = voucherEntryList.stream().collect(Collectors.groupingBy(EasVoucherDTO::getVoucherNumber));
        Iterator<Map.Entry<String, List<EasVoucherDTO>>> it = voucherDTOMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, List<EasVoucherDTO>> entry = it.next();
            List<EasVoucherDTO> entryList = entry.getValue();
            //获取第一个，取凭证头字段
            EasVoucherDTO voucherHeader = entryList.get(0);
            ExternalVoucherEntity voucherEntity = BeanUtil.copyProperties(voucherHeader, ExternalVoucherEntity.class);
            //获取金蝶的响应
//            EasVoucherRespDTO easVoucherRespDTO = easVoucherRespDTOMap.get(voucherEntity.getVoucherNumber());
//            if (easVoucherRespDTO != null){
//                voucherEntity.setEasVoucherId(easVoucherRespDTO.getEasVoucherId());
//                voucherEntity.setEasVoucherNumber(easVoucherRespDTO.getEasVoucherNumber());
//                voucherEntity.setEasLog(easVoucherRespDTO.getLog());
//                voucherEntity.setEasVoucherType(easVoucherRespDTO.getVoucherType());
//                voucherEntity.setEasPeriodMonth(NumberUtil.parseInt(easVoucherRespDTO.getPeriodMonth()));
//                voucherEntity.setEasPeriodYear(NumberUtil.parseInt(easVoucherRespDTO.getPeriodYear()));
//                voucherEntity.setEasFlag(easVoucherRespDTO.getFlag());
//                voucherEntity.setEasStatus(easVoucherRespDTO.getStatus());
//            }
            //不同步到金蝶
            EasVoucherRespDTO easVoucherRespDTO = new EasVoucherRespDTO();
            easVoucherRespDTO.setVoucherType(voucherHeader.getVoucherType());
            easVoucherRespDTO.setSystem(systemCode);
            easVoucherRespDTO.setVoucherNumber(voucherHeader.getVoucherNumber());
            easVoucherRespDTO.setFlag("1");
            easVoucherRespDTO.setStatus("success");
            easVoucherRespDTOList.add(easVoucherRespDTO);

            //保存凭证头
            this.save(voucherEntity);
            List<ExternalVoucherEntryEntity> entryEntityList = ListBeanUtil.copyList(entryList, ExternalVoucherEntryEntity.class);
            entryEntityList.forEach(e->e.setExternalVoucherId(voucherEntity.getId()));
            externalVoucherEntryService.saveBatch(entryEntityList);
        }
        return easVoucherRespDTOList;
    }

}

