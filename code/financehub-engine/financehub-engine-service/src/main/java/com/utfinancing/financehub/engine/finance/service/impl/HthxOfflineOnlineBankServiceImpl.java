package com.utfinancing.financehub.engine.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utfinancing.financehub.engine.finance.entity.HthxOfflineOnlineBankDataEntity;
import com.utfinancing.financehub.engine.finance.mapper.HthxOfflineOnlineBankDataMapper;
import com.utfinancing.financehub.engine.finance.service.IHthxOfflineOnlineBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 应用模块名称:
 * 代码描述:
 *
 * @author zhangli.chen
 * @Version: 1.0
 * @since 2025/8/12 20:32
 */

@Service
@Transactional
@Slf4j
public class HthxOfflineOnlineBankServiceImpl extends ServiceImpl<HthxOfflineOnlineBankDataMapper,
        HthxOfflineOnlineBankDataEntity> implements IHthxOfflineOnlineBankService {

}
