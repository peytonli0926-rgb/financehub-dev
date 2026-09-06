package com.utfinancing.financehub.engine.utils;

import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ContractStatusUpdateUtils {

    /**
     * 检查参数设置是否开启修改合同月表合同状态
     * @param remoteDictService
     * @param key
     * @return
     */
    public static boolean isUpdateContractStatusEnable(RemoteDictService remoteDictService, String key) {
        if (key == null) {
            return false;
        }
        try {
            R<List<SysDictData>> statusR = remoteDictService.listDictData(DictTypeEnum.UPDATE_FINANCIAL_CONTRACT_STATUS.getCode());
            if (statusR == null || statusR.getData() == null) {
                return false;
            }
            Map<String, String> statusMap = statusR.getData().stream()
                    .filter(e -> e.getDictValue() != null) // 过滤掉dictValue为null的条目
                    .collect(Collectors.toMap(e -> e.getDictValue(), e -> e.getDictLabel()));
            return "Y".equals(statusMap.get(key));
        } catch (Exception e) {
            // 处理异常，如日志记录
            log.error("检查系统是否开启失败", e);
            return false;
        }
    }
}
