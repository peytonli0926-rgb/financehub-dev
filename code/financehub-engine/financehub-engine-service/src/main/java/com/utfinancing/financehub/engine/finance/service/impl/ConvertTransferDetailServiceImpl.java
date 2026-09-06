package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferDetailEntity;
import com.utfinancing.financehub.engine.finance.entity.ConvertTransferEntity;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferDetailMapper;
import com.utfinancing.financehub.engine.finance.mapper.ConvertTransferMapper;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ConvertTransferDetailQueryDTO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferCheckVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferDetailExcelVO;
import com.utfinancing.financehub.engine.finance.model.vo.ConvertTransferDetailVO;
import com.utfinancing.financehub.engine.finance.service.IClientService;
import com.utfinancing.financehub.engine.finance.service.IContractBalanceService;
import com.utfinancing.financehub.engine.finance.service.IConvertTransferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : wenbin
 * @Date : Create in 2024-04-22
 * @Description :  ConvertTransferDetail服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ConvertTransferDetailServiceImpl extends ServiceImpl<ConvertTransferDetailMapper, ConvertTransferDetailEntity> implements IConvertTransferDetailService {

    private final ConvertTransferMapper convertTransferMapper;
    private final IContractBalanceService iContractBalanceService;
    private final IClientService clientService;

    @Override
    public Long saveConvertTransferDetail(ConvertTransferDetailDTO dto) {
        ConvertTransferDetailEntity entity = BeanUtil.copyProperties(dto, ConvertTransferDetailEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateConvertTransferDetail(Long id, ConvertTransferDetailDTO dto) {
        ConvertTransferDetailEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ConvertTransferDetailDTO getConvertTransferDetailDTOById(Long id) {
        ConvertTransferDetailEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ConvertTransferDetailDTO.class);
    }

    @Override
    public IPage<ConvertTransferDetailVO> selectPage(ConvertTransferDetailQueryDTO queryDTO) {
        LambdaQueryWrapper<ConvertTransferDetailEntity> queryWrapper = Wrappers.<ConvertTransferDetailEntity>lambdaQuery();
        queryWrapper.eq(ConvertTransferDetailEntity::getConvertTransferId, queryDTO.getConvertTransferId());
        IPage<ConvertTransferDetailEntity> entityIPage = getBaseMapper().selectPage(new Page<ConvertTransferDetailEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        IPage<ConvertTransferDetailVO> detailVOIPage = ListBeanUtil.copyPage(entityIPage, ConvertTransferDetailVO.class);
        List<ConvertTransferDetailVO> detailVOList = detailVOIPage.getRecords();
        setCLientName(detailVOList);
        return detailVOIPage;
    }

    private void setQueryCondition(ConvertTransferDetailQueryDTO queryDTO, LambdaQueryWrapper<ConvertTransferDetailEntity> queryWrapper) {
        if (ObjectUtil.isNotEmpty(queryDTO.getConvertTransferId())) {
            queryWrapper.eq(ConvertTransferDetailEntity::getConvertTransferId, queryDTO.getConvertTransferId());
        }
    }

    @Override
    public List<ConvertTransferDetailExcelVO> selectList(List<Long> transferIdList) {
        LambdaQueryWrapper<ConvertTransferDetailEntity> queryWrapper = Wrappers.<ConvertTransferDetailEntity>lambdaQuery();
        queryWrapper.in(ConvertTransferDetailEntity::getConvertTransferId, transferIdList);
        List<ConvertTransferDetailEntity> entityList = getBaseMapper().selectList(queryWrapper);
        List<ConvertTransferDetailVO> list = ListBeanUtil.copyList(entityList, ConvertTransferDetailVO.class);
        setCLientName(list);
        return ListBeanUtil.copyList(list, ConvertTransferDetailExcelVO.class);
    }

    /**
     * 根据折价转让id 删除详情
     */
    @Override
    public void deleteByConvertTransferId(List<Long> convertTransferIds) {
        remove(new LambdaQueryWrapper<ConvertTransferDetailEntity>().in(ConvertTransferDetailEntity::getConvertTransferId, convertTransferIds));
    }

    @Override
    public IPage<ConvertTransferCheckVO> check(ConvertTransferDetailQueryDTO queryDTO) {

        ConvertTransferEntity transfer = convertTransferMapper.selectById(queryDTO.getConvertTransferId());
        List<ConvertTransferDetailEntity> transferDetails = lambdaQuery().in(ConvertTransferDetailEntity::getConvertTransferId, queryDTO.getConvertTransferId())
                .orderByAsc(ConvertTransferDetailEntity::getConvertTransferId)
                .select(Arrays.asList(ConvertTransferDetailEntity::getConvertTransferId, ConvertTransferDetailEntity::getContractCode, ConvertTransferDetailEntity::getClientCode))
                .list();
        Map<String, List<String>> transferContractCodeMap = transferDetails
                .stream()
                .collect(Collectors.groupingBy(detail -> transfer.getTransferParty(), Collectors.mapping(ConvertTransferDetailEntity::getContractCode, Collectors.toList())));

        IPage<ConvertTransferCheckVO> page = getBaseMapper().selectCheckPage(new PageDTO<>(queryDTO.getPageNum(), queryDTO.getPageSize()), transferContractCodeMap);
        for (ConvertTransferCheckVO record : page.getRecords()) {
            // set client code
            for (ConvertTransferDetailEntity detail : transferDetails) {
                if (record.getContractCode().equals(detail.getContractCode())) {
                    record.setClientCode(detail.getClientCode());
                    record.setClientName(detail.getClientName());
                }
            }
        }
        return page;

    }

    /**
     * 设置数据 导入时已经计算，不在处理其他数据
     *
     * @param list
     */
    private void setCLientName(List<ConvertTransferDetailVO> list) {
//        if (CollectionUtils.isEmpty(list)) {
//            return;
//        }

//        List<String> clientCodeList = list.stream().map(ConvertTransferDetailVO::getClientCode).distinct().collect(Collectors.toList());
//        Map<String, ClientEntity> entityMap = clientService.selectClientMap(clientCodeList);
//        ClientEntity empty = new ClientEntity();
//        for (ConvertTransferDetailVO convertTransferDetailVO : list) {
//            convertTransferDetailVO.setClientName(entityMap.getOrDefault(convertTransferDetailVO.getClientCode(), empty).getClientName());
//        }
    }
}

