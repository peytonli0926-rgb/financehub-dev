package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAsstaccountQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TBdAsstaccountDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TBdAsstaccountVO;
import com.utfinancing.financehub.etl.kingdee.entity.TBdAsstaccountEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TBdAsstaccountMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITBdAsstaccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TBdAsstaccount服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TBdAsstaccountServiceImpl extends ServiceImpl<TBdAsstaccountMapper, TBdAsstaccountEntity> implements ITBdAsstaccountService {

    private final TBdAsstaccountMapper tBdAsstaccountMapper;


}

