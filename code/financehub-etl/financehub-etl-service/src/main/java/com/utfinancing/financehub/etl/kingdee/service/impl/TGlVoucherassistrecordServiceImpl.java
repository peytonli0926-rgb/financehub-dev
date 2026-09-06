package com.utfinancing.financehub.etl.kingdee.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGlVoucherassistrecordQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TGlVoucherassistrecordDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TGlVoucherassistrecordVO;
import com.utfinancing.financehub.etl.kingdee.entity.TGlVoucherassistrecordEntity;
import com.utfinancing.financehub.etl.kingdee.mapper.TGlVoucherassistrecordMapper;
import com.utfinancing.financehub.etl.kingdee.service.ITGlVoucherassistrecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description :  TGlVoucherassistrecord服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TGlVoucherassistrecordServiceImpl extends ServiceImpl<TGlVoucherassistrecordMapper, TGlVoucherassistrecordEntity> implements ITGlVoucherassistrecordService {

    private final TGlVoucherassistrecordMapper tGlVoucherassistrecordMapper;


}

