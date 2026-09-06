package com.utfinancing.financehub.admin.api.factory;

import com.utfinancing.financehub.admin.api.RemoteOrgService;
import com.utfinancing.financehub.admin.api.model.SysInternalUser;
import com.utfinancing.financehub.common.core.dto.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <ul>
 * <li>Project : FAW-PRIME-financehub-admin</li>
 * <li>ClassName : com.utfinancing.financehub.admin.api.factory.RemoteOrgFallbackFactory</li>
 * <li>CreateTime : 2023/11/20 09:15</li>
 * <li>Description :
 * <p>
 * </ul>
 *
 * @author bruce
 * @since 1.0.0
 */
@Component
public class RemoteOrgFallbackFactory implements FallbackFactory<RemoteOrgService> {
    @Override
    public RemoteOrgService create(Throwable cause) {
        return new RemoteOrgService() {
            @Override
            public R<List<String>> getOrgByUserCode(String userCode) {
                return R.fail("调用远程接口获取数据权限失败");
            }

            @Override
            public R<List<SysInternalUser>> getReviewSubUserByUserCode(String userCode) {
                return R.fail("调用远程接口获取复核人员下级人员信息失败");
            }
        };
    }
}
