package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAssistanthgQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAssistanthgDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAssistanthgVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAssistanthgEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TBdAssistanthgMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITBdAssistanthgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TBdAssistanthg服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TBdAssistanthgServiceImpl extends ServiceImpl<TBdAssistanthgMapper, TBdAssistanthgEntity> implements ITBdAssistanthgService {

    private final TBdAssistanthgMapper tBdAssistanthgMapper;


}

