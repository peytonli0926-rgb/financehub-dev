package com.utfinancing.financehub.etl.financial.service;


import com.utfinancing.financehub.etl.financial.entity.ClientEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author : lixin
 * @Date : Create in 2023-12-14
 * @Description : Client服务类接口
 * @Modified :
 */
public interface IClientService extends IService<ClientEntity> {

    List<ClientEntity> selectAllClient();
}
