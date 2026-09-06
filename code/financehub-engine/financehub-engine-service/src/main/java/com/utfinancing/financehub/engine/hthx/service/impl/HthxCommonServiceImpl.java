package com.utfinancing.financehub.engine.hthx.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.utfinancing.financehub.admin.api.RemoteDictService;
import com.utfinancing.financehub.admin.api.model.SysDictData;
import com.utfinancing.financehub.common.core.constant.HttpStatus;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.engine.api.HthxFundCoreService;
import com.utfinancing.financehub.engine.enums.DictTypeEnum;
import com.utfinancing.financehub.engine.hthx.common.enums.FinanceEngineEnum;
import com.utfinancing.financehub.engine.hthx.service.IHthxCommonService;
import com.utfinancing.financehub.engine.hthx.utils.HthxDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用模块名称:
 * 代码描述:
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class HthxCommonServiceImpl implements IHthxCommonService {


    @Resource
    private HthxFundCoreService hthxFundCoreService;

    private final RemoteDictService remoteDictService;

    /**
     * @description:判断当前时间是否可以执行调度任务
     * @author: zhangli.chen
     * @date 2025/02/25 11:31
     * @return boolean true:可以执行 false:不可以执行
     **/
    @Override
    public boolean checkTaskExecutionDate(){
        int startWorkDay = Integer.parseInt(FinanceEngineEnum.WorkDayParam.PARAM_WORK_DAY.getValue());
        log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>00==>>startWorkDay:{}",startWorkDay);
        // 判断是否启用
        List<SysDictData> dictEnableDataList = getDictTypeDataByRealTime(DictTypeEnum.WORK_DAY_CONFIG_FOR_JOB.getCode(),
                FinanceEngineEnum.WorkDayParam.PARAM_ENABLE.getKey());
        log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>01==>>dictEnableDataList:{}",dictEnableDataList);
        String enable = FinanceEngineEnum.ValidFlag.NO.getKey();
        boolean isExecutionTime = false;
        // 无配置-不执行 配置为N-放行不校验 配置为N-放行校验
        if(dictEnableDataList!=null && !dictEnableDataList.isEmpty()){
            Optional<SysDictData> firstEnableDataOptional = dictEnableDataList.stream().findFirst();
            if (firstEnableDataOptional.isPresent() && StringUtils.isNotEmpty(firstEnableDataOptional.get().getDictValue())) {
                enable = firstEnableDataOptional.get().getDictValue().trim().toUpperCase();
            }
            if(FinanceEngineEnum.ValidFlag.NO.getKey().equals(enable)){
                isExecutionTime = true;
            }
        }
        log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>02==>>enable:{}",enable);
        if(FinanceEngineEnum.ValidFlag.YES.getKey().equals(enable)){
            List<SysDictData> dictDataList = getDictTypeDataByRealTime(DictTypeEnum.WORK_DAY_CONFIG_FOR_JOB.getCode(),
                    FinanceEngineEnum.WorkDayParam.PARAM_WORK_DAY.getKey());
            log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>03==>>dictDataList:{}",dictDataList);
            if(dictDataList!=null && !dictDataList.isEmpty()){
                Optional<SysDictData> firstDataOptional = dictDataList.stream().findFirst();
                if (firstDataOptional.isPresent() && StringUtils.isNotEmpty(firstDataOptional.get().getDictValue())) {
                    startWorkDay = Integer.parseInt(firstDataOptional.get().getDictValue().trim());
                    log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>04==>>startWorkDay:{}",startWorkDay);
                }
            }
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 获取本月1号的日期
            LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
            // 生成从当前日期到本月1号的每一天日期
            List<LocalDate> betweenDateList = HthxDateUtils.generateDates(firstDayOfMonth, currentDate);
            log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>05==>>currentDate:{},firstDayOfMonth:{},betweenDateList:{}"
                    ,currentDate,firstDayOfMonth,betweenDateList);
            int number = 0;
            if(betweenDateList!=null && betweenDateList.size()>0){
                for(LocalDate betweenDate: betweenDateList){
                    R<String> queryResult = hthxFundCoreService.queryDayIsWorkday(HthxDateUtils.dateToShortStr(betweenDate));
                    log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>06==>>betweenDate:{},queryResult.getData():{}",betweenDate,queryResult.getData());
                    if (queryResult.getCode() == HttpStatus.SUCCESS && StringUtils.isNotEmpty(queryResult.getData())) {
                        if(FinanceEngineEnum.ValidFlag.YES.getKey().toLowerCase().equals(queryResult.getData())
                                ||  FinanceEngineEnum.ValidFlag.YES.getKey().equals(queryResult.getData())){
                            ++number;
                            if(number>startWorkDay){
                                break;
                            }
                        }
                    }
                }
            }
            log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>07==>>number:{},startWorkDay:{}",number,startWorkDay);
            if(number==startWorkDay){
                isExecutionTime = true;
            }
        }
        log.info("====>>HthxCommonServiceImpl.checkTaskExecutionDate==>>100==>>isExecutionTime:{}",isExecutionTime);
        return isExecutionTime;
    }

    /**
     * @param dictType
     * @description: 实时请求获取参数类型配置信息
     * @author: zhangli.chen
     */
    @Override
    public List<SysDictData> getDictTypeDataByRealTime(String dictType,String dictLabel) {
        List<SysDictData> filteredList = new ArrayList<>();
        if (ObjectUtil.isNotNull(dictType)) {
            R<List<SysDictData>> workDayListR =  remoteDictService.listDictData(dictType);
            if (workDayListR!=null && workDayListR.getCode() == HttpStatus.SUCCESS && CollectionUtil.isNotEmpty(workDayListR.getData())) {
                if(workDayListR.getData()!=null && workDayListR.getData().size()>0){
                    if(StringUtils.isNotEmpty(dictLabel)){
                        filteredList = workDayListR.getData().stream().filter(data->
                                FinanceEngineEnum.Numbers.ZERO.getValue().equals(data.getStatus())
                                        && dictLabel.equals(data.getDictLabel())).collect(Collectors.toList());
                    }else{
                        filteredList = workDayListR.getData().stream().filter(data->
                                FinanceEngineEnum.Numbers.ZERO.getValue().equals(data.getStatus())).collect(Collectors.toList());
                    }
                    if(filteredList!=null && filteredList.size()>0){
                        // 倒序排列
                        Collections.sort(filteredList, new Comparator<SysDictData>() {
                            @Override
                            public int compare(SysDictData data1, SysDictData data2) {
                                return Long.compare(data1.getDictSort(), data2.getDictSort());
                            }
                        });
                    }
                }
            }
        }
        return filteredList;
    }

}
