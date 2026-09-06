package com.utfinancing.financehub.etl.financial.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.etl.financial.model.dto.OrgCompanyQueryDTO;
import com.utfinancing.financehub.etl.financial.model.dto.OrgCompanyDTO;
import com.utfinancing.financehub.etl.financial.model.vo.OrgCompanyVO;
import com.utfinancing.financehub.etl.financial.entity.OrgCompanyEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-11-12
 * @Description : OrgCompany服务类接口
 * @Modified :
 */
@DS("master")
public interface IOrgCompanyService extends IService<OrgCompanyEntity> {


}
