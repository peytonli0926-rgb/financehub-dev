# 资金财务中台项目-顶级父模块

## 模块介绍
```
financehub-parent: 父模块
    financehub-admin： 后台管理服务
        financehub-admin-api：后台管理模块Feign API
        financehub-admin-service： 后台管理模块服务
    financehub-auth：认证服务
    financehub-common: 基础模块
        financehub-common-core：核心包
        financehub-common-log：用户操作日志spring-boot-starter
        financehub-common-mybatis: mybatisplus spring-boot-starter
        financehub-common-redis: redis spring-boot-starter
        financehub-common-security: spring security spring-boot-starter
        financehub-common-swagger: Swagger spring-boot-starter
    financehub-engine: 会计引擎服务
        financehub-engine-api： 会计引擎服务API
        financehub-engine-service: 会计引擎服务
    financehub-gateway: 网关服务
```

### 后端服务列表
| 序号 | 模块                        | 名称     | 端口    |
|----|---------------------------|--------|-------|
| 1  | financehub-gateway        | 网关服务   | 8888  |
| 2  | financehub-auth           | 认证服务   | 8200  |
| 3  | financehub-admin-service  | 后台管理服务 | 8201  |
| 4  | financehub-engine-service | 会计引擎服务 | 8202  |

### 接口文档

- 单个服务接口地址: http://localhost:{port}/doc.html
- 网关聚合接口地址：http://localhost:8888/doc.html

