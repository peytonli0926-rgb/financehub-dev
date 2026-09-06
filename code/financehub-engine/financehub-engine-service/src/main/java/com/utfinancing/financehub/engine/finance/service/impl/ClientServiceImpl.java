package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.engine.enums.YesOrNoEnum;
import com.utfinancing.financehub.engine.finance.model.dto.ClientQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ClientDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImportClientExcel;
import com.utfinancing.financehub.engine.finance.model.vo.ClientVO;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.utfinancing.financehub.engine.finance.mapper.ClientMapper;
import com.utfinancing.financehub.engine.finance.service.IClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description :  Client服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientEntity> implements IClientService {

    private final ClientMapper clientMapper;

    @Override
    public Long saveClient(ClientDTO dto) {
        ClientEntity entity = BeanUtil.copyProperties(dto, ClientEntity.class);
        this.save(entity);
        return entity.getId();
    }

    @Override
    public Long updateClient(Long id, ClientDTO dto) {
        ClientEntity entity = this.getById(id);
        BeanUtil.copyProperties(dto, entity);
        entity.updateById();
        return id;
    }

    @Override
    public ClientDTO getClientDTOById(Long id) {
        ClientEntity entity = this.getById(id);
        if (entity == null) return null;
        return BeanUtil.copyProperties(entity, ClientDTO.class);
    }

    @Override
    public IPage<ClientVO> selectPage(ClientQueryDTO queryDTO) {
        LambdaQueryWrapper<ClientEntity> queryWrapper = Wrappers.<ClientEntity>lambdaQuery();
        //这里注入查询条件
        if (StringUtils.isNotEmpty(queryDTO.getSearchKey())) {
              queryWrapper.or().like(ClientEntity::getClientCode,queryDTO.getSearchKey()).or().like(ClientEntity::getClientName,queryDTO.getSearchKey());
        }
        if (StringUtils.isNotEmpty(queryDTO.getClientCodeOrName())) {
            queryWrapper.or().like(ClientEntity::getClientCode,queryDTO.getClientCodeOrName()).or().like(ClientEntity::getClientName,queryDTO.getClientCodeOrName());
        }
        IPage<ClientEntity> entityIPage = clientMapper.selectPage(new Page<ClientEntity>(queryDTO.getPageNum(), queryDTO.getPageSize()), queryWrapper);
        return ListBeanUtil.copyPage(entityIPage, ClientVO.class);
    }

    @Override
    public String saveOrUpdateClient(Map<String, Object> interfaceDataMap) {
        String clientCode = MapUtil.getStr(interfaceDataMap, RuleConstant.FIELD_CLIENT_CODE);
        if (StrUtil.isBlank(clientCode)) {
            return null;
        }
        ClientEntity entity = this.getOne(Wrappers.<ClientEntity>lambdaQuery()
                .eq(ClientEntity::getClientCode, clientCode), false);
        if (entity != null) {
            BeanUtil.copyProperties(interfaceDataMap, entity);
            this.updateById(entity);
        } else {
            entity = BeanUtil.copyProperties(interfaceDataMap, ClientEntity.class);
            /**
             * @description: modify by zhangli.chen for 解决多线程批量生成凭证的情况下主键生成冲突问题，清空id，确保由 MyBatis Plus 自动生成 on 20250428
             **/
            entity.setId(null);
            this.save(entity);
        }
        return clientCode;
    }

    @Override
    public String importData(List<ImportClientExcel> list) {
        List<String> codeList = list.stream().map(e -> e.getClientCode()).collect(Collectors.toList());
        List<ClientEntity> clientEntities = this.getBaseMapper().selectList(Wrappers.<ClientEntity>lambdaQuery()
                .in(ClientEntity::getClientCode, codeList));
        Map<String, ClientEntity> codeMap = clientEntities.stream().collect(Collectors.toMap(e -> e.getClientCode(), e -> e));
        List<String> existCodeList = clientEntities.stream().map(e -> e.getClientCode()).collect(Collectors.toList());
        List<ClientEntity> insertList = new ArrayList<>();
        list.forEach(e -> {
            if (existCodeList.contains(e.getClientCode())) {
                ClientEntity clientEntity = codeMap.get(e.getClientCode());
                BeanUtil.copyProperties(e, clientEntity);
                insertList.add(clientEntity);
            } else {
                insertList.add(BeanUtil.copyProperties(e, ClientEntity.class));
            }
        });
        this.saveOrUpdateBatch(insertList);
        return null;
    }

    @Override
    public Map<String, Object> getClientMap(String clientCode) {
        LambdaQueryWrapper<ClientEntity> lambdaQueryWrapper = Wrappers.<ClientEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ClientEntity::getClientCode, clientCode);
        List<ClientEntity> entityList = this.list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        ClientEntity entity = entityList.get(0);
        Map<String, Object> oldMap = JSONObject.parseObject(JSONObject.toJSONString(entity), new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> newMap = new HashMap<>();
        Iterator<Map.Entry<String, Object>> it = oldMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            newMap.put("client_" + entry.getKey(), entry.getValue());
        }
        return newMap;
    }

    public Map<String, ClientEntity> selectClientMap(List<String> clientCodeList) {
        LambdaQueryWrapper<ClientEntity> lambdaQueryWrapper = Wrappers.<ClientEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ClientEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        lambdaQueryWrapper.in(ClientEntity::getClientCode, clientCodeList);
        List<ClientEntity> clientEntityList = this.getBaseMapper().selectList(lambdaQueryWrapper);
        if (clientEntityList == null || clientEntityList.isEmpty()) {
            return new HashMap<>();
        }

        return clientEntityList.stream().collect(Collectors.toMap(e->e.getClientCode(), (e)->e, (a,b)->b));
    }

    @Override
    public List<ClientEntity> selectByClientCodeList(List<String> clientCodeList) {
        if(CollectionUtils.isEmpty(clientCodeList)){
            return Lists.newArrayList();
        }
        List<ClientEntity> clientEntityList = this.list(new LambdaQueryWrapper<ClientEntity>().in(CollectionUtils.isNotEmpty(clientCodeList),ClientEntity::getClientCode, clientCodeList));
        return clientEntityList;
    }

    public ClientEntity selectClientByCode(String clientCode) {
        LambdaQueryWrapper<ClientEntity> lambdaQueryWrapper = Wrappers.<ClientEntity>lambdaQuery();
        lambdaQueryWrapper.eq(ClientEntity::getClientCode, clientCode);
        lambdaQueryWrapper.eq(ClientEntity::getDelFlag, YesOrNoEnum.NO.getCode());
        return clientMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public String selectClientCodeByName(String clientName) {
        return clientMapper.selectClientCodeByName(clientName);
    }
}

