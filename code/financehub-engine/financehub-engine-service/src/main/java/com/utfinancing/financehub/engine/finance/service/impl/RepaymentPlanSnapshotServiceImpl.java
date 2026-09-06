package com.utfinancing.financehub.engine.finance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.common.core.constant.GenConstants;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.DateUtils;
import com.utfinancing.financehub.common.mybatis.util.ListBeanUtil;
import com.utfinancing.financehub.engine.claim.model.dto.ClaimOrderSpecialQueryDTO;
import com.utfinancing.financehub.engine.claim.model.vo.ClaimOrderCostVo;
import com.utfinancing.financehub.engine.claim.service.IClaimOrderSpecialService;
import com.utfinancing.financehub.engine.constants.Constants;
import com.utfinancing.financehub.engine.enums.*;
import com.utfinancing.financehub.engine.finance.entity.*;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanHisMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanMapper;
import com.utfinancing.financehub.engine.finance.mapper.RepaymentPlanSnapshotMapper;
import com.utfinancing.financehub.engine.finance.model.dto.*;
import com.utfinancing.financehub.engine.finance.model.vo.OfflineContractRepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.model.vo.RepaymentPlanVO;
import com.utfinancing.financehub.engine.finance.service.*;
import com.utfinancing.financehub.engine.rule.constant.RuleConstant;
import com.utfinancing.financehub.engine.rule.entity.InterfaceDataEntity;
import com.utfinancing.financehub.engine.rule.service.IInterfaceDataService;
import com.utfinancing.financehub.engine.utils.IRRUtils;
import com.utfinancing.financehub.engine.utils.XirrUtils;
import com.utfinancing.financehub.etl.api.*;
import com.utfinancing.financehub.etl.model.dto.QueryRepaymentPlanDTO;
import com.utfinancing.financehub.etl.model.dto.SelectReceiveRepaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * @Author : hzhao
 * @Date : Create in 2023-09-12
 * @Description :  RepaymentPlan服务实现类
 * @Modified :
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class RepaymentPlanSnapshotServiceImpl extends ServiceImpl<RepaymentPlanSnapshotMapper, RepaymentPlanSnapshotEntity>
        implements IRepaymentPlanSnapshotService {

    private final RepaymentPlanSnapshotMapper repaymentPlanSnapshotMapper;


}
