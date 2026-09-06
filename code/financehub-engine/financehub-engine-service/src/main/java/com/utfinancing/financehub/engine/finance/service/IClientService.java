package com.utfinancing.financehub.engine.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.utfinancing.financehub.engine.finance.model.dto.ClientQueryDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ClientDTO;
import com.utfinancing.financehub.engine.finance.model.dto.ImportClientExcel;
import com.utfinancing.financehub.engine.finance.model.vo.ClientVO;
import com.utfinancing.financehub.engine.finance.entity.ClientEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Author : lixin
 * @Date : Create in 2023-09-13
 * @Description : Client服务类接口
 * @Modified :
 */
public interface IClientService extends IService<ClientEntity> {

    Long saveClient(ClientDTO dto);

    Long updateClient(Long id, ClientDTO dto);

    ClientDTO getClientDTOById(Long id);

    IPage<ClientVO> selectPage(ClientQueryDTO queryDTO);

    /**
     *根据接口数据保存客户数据
     * @param interfaceDataMap 接口数据
     * @return
     */
    String saveOrUpdateClient(Map<String, Object> interfaceDataMap);


    String importData(List<ImportClientExcel> list);

    Map<String, Object> getClientMap(String clientCode);

    public Map<String, ClientEntity> selectClientMap(List<String> clientCodeList);

    List<ClientEntity> selectByClientCodeList(List<String> clientCodeList);

    ClientEntity selectClientByCode(String clientName);

    String selectClientCodeByName(String clientName);
}
