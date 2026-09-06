package com.utfinancing.financehub.engine.contractstatusupdate.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.contractstatusupdate.entity.SysDictTypeEntity;
import com.utfinancing.financehub.engine.contractstatusupdate.mapper.SysDictTypeMapper;
import com.utfinancing.financehub.engine.contractstatusupdate.service.ISysDictTypeService;

/**
 * 字典类型表Service实现类
 * @author CodeAutoGenerator
 *
 */
@Service("sysDictTypeService")
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictTypeEntity> implements ISysDictTypeService {

	@Autowired
	private SysDictTypeMapper sysDictTypeMapper;
	
}