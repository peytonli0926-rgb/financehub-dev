package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.StringUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.ProcessStatusEnum;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.entity.VoucherEntity;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInDetailVO;
import com.utfinancing.financehub.engine.finance.entity.RecyclingEquipmentInDetailEntity;
import com.utfinancing.financehub.engine.finance.mapper.RecyclingEquipmentInDetailMapper;
import com.utfinancing.financehub.engine.finance.model.vo.RecyclingEquipmentInVO;
import com.utfinancing.financehub.engine.finance.service.IClientService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.finance.service.IRecyclingEquipmentInDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.service.IVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author : jnc
 * @Date : Create in 2024-03-07
 * @Description :  RecyclingEquipmentInDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RecyclingEquipmentInDetailServiceImpl extends ServiceImpl<RecyclingEquipmentInDetailMapper, RecyclingEquipmentInDetailEntity> implements IRecyclingEquipmentInDetailService {

    private final RecyclingEquipmentInDetailMapper recyclingEquipmentInDetailMapper;

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Resource
    IClientService iClientService;

    @Resource
    private IVoucherService voucherService;

    @Override
    public Long saveRecyclingEquipmentInDetail(RecyclingEquipmentInDetailDTO dto) {
        RecyclingEquipmentInDetailEntity entity = BeanUtil.copyProperties(dto, RecyclingEquipmentInDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateRecyclingEquipmentInDetail(Long id, RecyclingEquipmentInDetailDTO dto) {
        RecyclingEquipmentInDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public RecyclingEquipmentInDetailDTO getRecyclingEquipmentInDetailDTOById(Long id) {
        RecyclingEquipmentInDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, RecyclingEquipmentInDetailDTO.class);
    }

    @Override
    public IPage<RecyclingEquipmentInDetailVO> selectPage(RecyclingEquipmentInDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<RecyclingEquipmentInDetailEntity> queryWrapper = Wrappers.<RecyclingEquipmentInDetailEntity>lambdaQuery();
        if(StringUtils.isNotEmpty(queryDTO.getContractCode())){
            queryWrapper.like(RecyclingEquipmentInDetailEntity::getContractCode, queryDTO.getContractCode());
        }
        if(StringUtils.isNotEmpty(queryDTO.getClientName())){
            queryWrapper.like(RecyclingEquipmentInDetailEntity::getClientName, queryDTO.getClientName());
        }
        if(StringUtils.isNotEmpty(queryDTO.getOrgId())){
            queryWrapper.eq(RecyclingEquipmentInDetailEntity::getOrgId, queryDTO.getOrgId());
        }
        if(StringUtils.isNotEmpty(queryDTO.getInboundDate())){
            queryWrapper.eq(RecyclingEquipmentInDetailEntity::getInboundDate, queryDTO.getInboundDate());
        }
        // 新增汇总表ID筛选条件
        if(StringUtils.isNotEmpty(queryDTO.getId())){
            queryWrapper.eq(RecyclingEquipmentInDetailEntity::getRecycleId, Long.valueOf(queryDTO.getId()));
        }
        //这里注入查询条件
        IPage<RecyclingEquipmentInDetailEntity> entityIPage = recyclingEquipmentInDetailMapper.selectPage(new Page<RecyclingEquipmentInDetailEntity>(queryDTO.getPageNum(),queryDTO.getPageSize()), queryWrapper);
        IPage<RecyclingEquipmentInDetailVO> page = ListBeanUtil.copyPage(entityIPage, RecyclingEquipmentInDetailVO.class);
        List<RecyclingEquipmentInDetailVO> records = page.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            // add by zhangli.chen for 新增会计期间字段，代入查询凭证页面优化查询效率 on 20250318
            final Optional<Integer> periodCodeOptional = records.stream()
                    .filter(e -> StringUtils.isNotEmpty(e.getVoucherId()))
                    .findFirst()
                    .flatMap(detailVO -> {
                        String voucherId = detailVO.getVoucherId();
                        if (StringUtils.isNotEmpty(voucherId)) {
                            List<String> voucherIdArray = Pattern.compile(",")
                                    .splitAsStream(voucherId)
                                    .collect(Collectors.toList());
                            if (CollectionUtil.isNotEmpty(voucherIdArray)) {
                                LambdaQueryWrapper<VoucherEntity> voucherEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
                                voucherEntityLambdaQueryWrapper.eq(VoucherEntity::getId, voucherIdArray.get(0));
                                List<VoucherEntity> voucherEntityList = voucherService.list(voucherEntityLambdaQueryWrapper);
                                if (voucherEntityList != null && voucherEntityList.size() > 0) {
                                    return Optional.of(voucherEntityList.get(0).getPeriodCode());
                                }
                            }
                        }
                        return Optional.empty();
                    });
            Map<String, String> orgMap = getOrgNameOrgId();
            records.stream().forEach(v -> {
                if (orgMap.containsKey(v.getOrgId())) {
                    v.setOrgName(orgMap.get(v.getOrgId()));
                }
                periodCodeOptional.ifPresent(v::setPeriodCode);
            });
        }
        page.setRecords(records);
        return page;
    }

    @Override
    public List<RecyclingEquipmentInCheckVO> getCheckData(String inboundDate, String orgId, Long id) {
        String periodCode = inboundDate.replaceAll("-","").substring(0,6);
        return recyclingEquipmentInDetailMapper.getCheckData(inboundDate, periodCode, orgId, id);
    }

    /**
     * @description: 债务重组业务-回收设备-财务入库-查看详情-导出
     * @author: zhangli.chen
     **/
    @Override
    public List<RecyclingEquipmentInDetailExportExcelDTO> listByCondition(RecyclingEquipmentInDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<RecyclingEquipmentInDetailEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecyclingEquipmentInDetailEntity::getDelFlag, "0");
        wrapper.eq(RecyclingEquipmentInDetailEntity::getInboundDate, queryDTO.getInboundDate());
        if (StringUtils.isNotEmpty(queryDTO.getId())) {
            wrapper.eq(RecyclingEquipmentInDetailEntity::getRecycleId,Long.valueOf(queryDTO.getId()));
        }
        wrapper.eq(RecyclingEquipmentInDetailEntity::getOrgId, queryDTO.getOrgId());
        if (StringUtils.isNotEmpty(queryDTO.getContractCode())) {
            wrapper.like(RecyclingEquipmentInDetailEntity::getContractCode, queryDTO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientName())) {
            wrapper.like(RecyclingEquipmentInDetailEntity::getClientName, queryDTO.getClientName());
        }
        List<RecyclingEquipmentInDetailExportExcelDTO> inVOList = BeanUtil.copyToList(this.list(wrapper), RecyclingEquipmentInDetailExportExcelDTO.class);
        fillVoList(inVOList);
        return inVOList;
    }

    private void fillVoList(List<RecyclingEquipmentInDetailExportExcelDTO> inVOList) {
        if(CollectionUtils.isEmpty(inVOList)){
            return;
        }
        Map<String,String> orgNameByOrgIdMap = getOrgNameOrgId();
        List<String> clientCodes = inVOList.stream().map(RecyclingEquipmentInDetailExportExcelDTO::getClientCode).distinct().collect(Collectors.toList());
        Map<String, String> clientMap = getClientNameClientCode(clientCodes);
        inVOList.stream().forEach(v -> {
            if (orgNameByOrgIdMap.containsKey(v.getOrgId())) {
                v.setOrgName(orgNameByOrgIdMap.get(v.getOrgId()));
            }
            if (clientMap.containsKey(v.getClientCode())) {
                v.setClientName(clientMap.get(v.getClientCode()));
            }
        });
    }

    private Map<String, String> getClientNameClientCode(List<String> clientCodeList) {
        Map<String,String> clientMap = new HashMap<>();
        if (com.alibaba.nacos.common.utils.CollectionUtils.isNotEmpty(clientCodeList)) {
            List<ClientEntity> clientEntityList = iClientService.lambdaQuery().in(ClientEntity::getClientCode,clientCodeList).list();
            if (com.alibaba.nacos.common.utils.CollectionUtils.isNotEmpty(clientEntityList)) {
                clientMap = clientEntityList.stream().collect(HashMap::new,(map,item)->map.put(item.getClientCode(),item.getClientName()),HashMap::putAll);
            }
        }
        return clientMap;
    }

    private Map<String,String> getOrgNameOrgId(){
        Map<String,String> orgNameAndIdMap = Maps.newHashMap();
        List<OrgCompanyVO> orgCompanyVOList =  iOrgCompanyService.selectByCondition(new OrgCompanyQueryDTO());
        if (CollectionUtils.isNotEmpty(orgCompanyVOList)) {
            orgNameAndIdMap = orgCompanyVOList.stream().collect(Collectors.toMap(OrgCompanyVO::getOrgId,OrgCompanyVO::getOrgName, (k1, k2)->k2));
        }
        return orgNameAndIdMap;
    }

    @Override
    public RecyclingEquipmentInDetailVO getProvision(String contractCode, String orgId) {
        List<String> processStatusList = ProcessStatusEnum.getCannotModifyCode();
        return baseMapper.getProvision(contractCode,orgId,processStatusList);
    }


}

