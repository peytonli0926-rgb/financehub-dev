package com.utfinancing.financehub.engine.rule.service;

import com.alibaba.fastjson2.JSONObject;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.finance.entity.FundBusinessSystemEbankMappingEntity;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDTO;
import com.utfinancing.financehub.engine.finance.model.dto.VoucherDetailDTO;
import com.utfinancing.financehub.engine.rule.model.dto.InterfaceDataDTO;
import com.utfinancing.financehub.engine.rule.model.vo.VoucherInfoVO;
import com.utfinancing.financehub.engine.scene.model.dto.BusinessDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneDTO;
import com.utfinancing.financehub.engine.scene.model.dto.SceneRuleDTO;
import com.utfinancing.financehub.engine.scene.model.dto.TaxRateDTO;
import com.utfinancing.financehub.engine.scene.model.vo.BusinessVO;

import java.util.List;
import java.util.Map;

public interface IRuleService {

    /**
     * 获取翻译后的规则
     * @param sceneCode
     * @return
     */
    List<SceneRuleDTO> getTranslateRule(String sceneCode, InterfaceDataDTO interfaceDataDTO, Map<String, Object> dataMap);


    List<VoucherDTO> executeRule(Map<String, Object> dataMap);

    /**
     * 参数校验
     */
    public void validateRequiredField(Map<String, Object> dataMap);

    /**
     * 获取场景信息
     */
    public SceneDTO getRedisSceneDTOByCode(String sceneCode);

    /**
     * 从接口数据中提取并保存相关联的数据，数据包括：
     * 1. 接口数据
     * 2. 合同
     * 3. 客户
     * 4. 业务交易信息
     * 5. 供应商
     * 6. 交叉销售分成
     * @param dataMap
     */
    InterfaceDataDTO saveInterfaceData(Map<String, Object> dataMap);


    void execute(String messageId, JSONObject jsonData);

    List<VoucherDetailDTO> generateVoucher(JSONObject jsonObject);

    /**
     * 校验表达式语法
     * @param script
     * @return 成功：true， 失败：false
     */
    Boolean validateSyntax(String script);

    /**
     * 批量执行凭证
     * @param dataMapList 参数数组
     * @return 返回值，key = orderId,value = 凭证信息
     */
    List<VoucherInfoVO> batchExecuteRule(List<Map<String, Object>> dataMapList);

    void getExecuteRuleResult(Map<String, Object> dataMap,VoucherInfoVO voucherInfoVO);

    public BusinessDTO getRedisBusinessByCode(String businessCode);

    public void injectDefaultNumberFields(String sceneCode, Map<String, Object> dataMap);

    public void cleanStringEmptyFields(Map<String, Object> dataMap);

    public List<BusinessVO> selectRedisBusinessAll();

    public List<TaxRateDTO> queryRedisAllForEditor();

    public List<SceneRuleDTO> getSceneRuleDTOByCode(String sceneCode);

    public R<List<SysDictData>> listDictTypeData (String dictType);

    FundBusinessSystemEbankMappingEntity bankExistFlag (String ebankSerialNumber);
}
