package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.DropDownDTO;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.finance.entity.ContractEntity;
import com.utfinancing.financehub.engine.finance.entity.OrgCompanyEntity;
import com.utfinancing.financehub.engine.finance.service.ExcelDropDownService;
import com.utfinancing.financehub.engine.finance.service.IContractService;
import com.utfinancing.financehub.engine.finance.service.IOrgCompanyService;
import com.utfinancing.financehub.engine.scene.entity.BusinessEntity;
import com.utfinancing.financehub.engine.scene.entity.SceneEntity;
import com.utfinancing.financehub.engine.scene.service.IBusinessService;
import com.utfinancing.financehub.engine.scene.service.ISceneService;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-engine</li>
 * <li>ClassName : com.utfinancing.financehub.engine.finance.service.impl.ExcelDropDownServiceImpl</li>
 * <li>CreateTime : 2024/02/04 12:01</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Slf4j
@Service
public class ExcelDropDownServiceImpl implements ExcelDropDownService {

    @Resource
    private IOrgCompanyService iOrgCompanyService;

    @Resource
    private IContractService iContractService;

    @Resource
    private IBusinessService businessService;

    @Resource
    private ISceneService iSceneService;

    @Resource
    private RemoteDictService remoteDictService;

    @Override
    public List<DropDownDTO> getManualExcelDropDown() {
        List<DropDownDTO> dropDownDTOList = Lists.newArrayList();
        DropDownDTO orgNames = new DropDownDTO();
        orgNames.setDataList(getAllOrgName());
        orgNames.setFirstRow(0);
        orgNames.setLastRow(0);
        if (CollectionUtils.isNotEmpty(orgNames.getDataList())) {
            dropDownDTOList.add(orgNames);
        }
        DropDownDTO businessCodes = new DropDownDTO();
        businessCodes.setDataList(getAllBusinessCode());
        businessCodes.setFirstRow(6);
        businessCodes.setLastRow(6);
        if (CollectionUtils.isNotEmpty(businessCodes.getDataList())) {
            dropDownDTOList.add(businessCodes);
        }
        DropDownDTO sceneNames = new DropDownDTO();
        sceneNames.setDataList(getAllSceneName());
        sceneNames.setFirstRow(7);
        sceneNames.setLastRow(7);
        if (CollectionUtils.isNotEmpty(sceneNames.getDataList())) {
            dropDownDTOList.add(sceneNames);
        }
        DropDownDTO subTypes = new DropDownDTO();
        subTypes.setDataList(getAllSubType());
        subTypes.setFirstRow(8);
        subTypes.setLastRow(8);
        if (CollectionUtils.isNotEmpty(subTypes.getDataList())) {
            dropDownDTOList.add(subTypes);
        }
        DropDownDTO clientTypes = new DropDownDTO();
        clientTypes.setDataList(getClientTypeList());
        clientTypes.setFirstRow(20);
        clientTypes.setLastRow(20);
        if (CollectionUtils.isNotEmpty(clientTypes.getDataList())) {
            dropDownDTOList.add(clientTypes);
        }
        return dropDownDTOList;
    }

    /**
     * 签约主体
     * @return
     */
    public List<String> getAllOrgName(){
        return iOrgCompanyService.list().stream().map(OrgCompanyEntity::getOrgName).distinct().collect(Collectors.toList());
    }

    /**
     * 合同编码
     * @return
     */
    public List<String> getAllContractCode() {
        return iContractService.list().stream().map(ContractEntity::getContractCode).distinct().collect(Collectors.toList());
    }

    /**
     * 业务编码
     * @return
     */
    public List<String> getAllBusinessCode(){
        return businessService.list().stream().map(BusinessEntity::getBusinessCode).distinct().collect(Collectors.toList());
    }

    /**
     * 场景编码
     * @return
     */
    public List<String> getAllSceneCode() {
        return iSceneService.list().stream().map(SceneEntity::getSceneCode).distinct().collect(Collectors.toList());
    }

    /**
     * 场景名称
     * @return
     */
    public List<String> getAllSceneName() {
        return iSceneService.list().stream().map(SceneEntity::getSceneName).distinct().collect(Collectors.toList());
    }


    /**
     * 细分场景编码
     * @return
     */
    public List<String> getAllSubType() {
        R<List<SysDictData>> leaseSubTypeR = remoteDictService.listDictData(DictTypeEnum.SYS_SUB_SCENE_TYPE.getCode());
        if (null != leaseSubTypeR) {
            return leaseSubTypeR.getData().stream().map(SysDictData::getDictLabel).distinct().collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public List<String> getClientTypeList() {
        R<List<SysDictData>> clientTypeR = remoteDictService.listDictData(DictTypeEnum.SYS_CLIENT_TYPE.getCode());
        if (null != clientTypeR) {
            return clientTypeR.getData().stream().map(SysDictData::getDictLabel).distinct().collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

}
