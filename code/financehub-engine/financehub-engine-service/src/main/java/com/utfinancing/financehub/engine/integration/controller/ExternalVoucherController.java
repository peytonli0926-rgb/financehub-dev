package com.utfinancing.financehub.engine.integration.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.utfinancing.financehub.common.core.dto.R;
import com.utfinancing.financehub.common.core.exception.ServiceException;
import com.utfinancing.financehub.common.core.utils.SecureUtil;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherQueryDTO;
import com.utfinancing.financehub.engine.integration.model.dto.ExternalVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherDTO;
import com.utfinancing.financehub.engine.integration.model.eas.dto.EasVoucherRespDTO;
import com.utfinancing.financehub.engine.integration.model.vo.ExternalVoucherVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.utfinancing.financehub.engine.integration.service.IExternalVoucherService;


/**
 * @Author : lixin
 * @Date : Create in 2023-10-31
 * @Description :   ExternalVoucher控制器实现类
 * @Modified :
 */
@Api(tags = "外部业务系统凭证接口")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/integration/voucher")
public class ExternalVoucherController {

    private final IExternalVoucherService  externalVoucherService;

    @Value("${spring.profiles.active}")
    private String env;

    @PostMapping("/add")
    @ApiOperation(value = "添加凭证")
    public R<List<EasVoucherRespDTO>> save(
            @RequestHeader Map<String, String> headers,
            @Valid @RequestBody List<EasVoucherDTO> easVoucherEntryList) {
        if (!headers.containsKey("system-code")){
            throw new ServiceException("header[system-code]不能为空");
        }
        if (!headers.containsKey("system-secret")){
            throw new ServiceException("header[system-secret]不能为空");
        }
        String systemCode = headers.get("system-code");
        String systemSecret = headers.get("system-secret");
        //解密
        String key = "finhub" + env;
        if (!StrUtil.equals(SecureUtil.desDecrypt(key, systemSecret), systemCode)){
            throw new ServiceException("系统编码[system-code]或密钥[system-secret]错误");
        }
        if (CollectionUtil.isEmpty(easVoucherEntryList)){
            throw new ServiceException("凭证数据不能为空");
        }
        if (CollectionUtil.size(easVoucherEntryList) < 2){
            throw new ServiceException("凭证分录应至少包含两行");
        }
        return R.ok(externalVoucherService.addVoucherToEas(systemCode, easVoucherEntryList));
    }

}



