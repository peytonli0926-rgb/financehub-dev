package com.utfinancing.financehub.admin.api;

import com.utfinancing.financehub.admin.api.factory.RemoteOrgFallbackFactory;
import com.utfinancing.financehub.admin.api.model.SysInternalUser;
import com.utfinancing.financehub.common.core.constant.ServiceNameConstants;
import com.utfinancing.financehub.common.core.dto.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-admin</li>
 * <li>ClassName : com.utfinancing.financehub.admin.api.RemoteOrgService</li>
 * <li>CreateTime : 2023/11/20 09:16</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@FeignClient(contextId = "remoteOrgService",value = ServiceNameConstants.ADMIN_SERVICE,
        fallbackFactory = RemoteOrgFallbackFactory.class)
public interface RemoteOrgService {
    @GetMapping("/sys-internal-role-org/getOrgByUserCode/{userCode}")
    R<List<String>> getOrgByUserCode(@PathVariable("userCode") String userCode);

    @ApiOperation(value = "根据用户编码获取复核角色下下级人员信息")
    @GetMapping("/sys-internal-user/getReviewSubUserByUserCode/{userCode}")
    R<List<SysInternalUser>> getReviewSubUserByUserCode(@PathVariable("userCode") @Valid @NotNull String userCode);
}
