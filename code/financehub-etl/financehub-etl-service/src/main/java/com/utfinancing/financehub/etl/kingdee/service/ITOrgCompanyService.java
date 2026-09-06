package com.utfinancing.financehub.etl.kingdee.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.kingdee.model.dto.TOrgCompanyQueryDTO;
import com.utfinancing.financehub.etl.kingdee.model.dto.TOrgCompanyDTO;
import com.utfinancing.financehub.etl.kingdee.model.vo.TOrgCompanyVO;
import com.utfinancing.financehub.etl.kingdee.entity.TOrgCompanyEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : TOrgCompany服务类接口
 * @Modified :
 */
@DS("slave_kingdee")
public interface ITOrgCompanyService extends IService<TOrgCompanyEntity> {

}
