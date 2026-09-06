# FinanceHub 系统架构说明

## 1. 文档目的

本文从软件工程视角描述 FinanceHub 的系统边界、模块职责、运行架构、数据流、依赖关系和当前本地环境状态。本文依据当前 `code/` 源码、前端页面结构、后端 controller 分布和 `docs/local-run-analysis.md` 的本地启动验证结果整理。

## 2. 系统定位

FinanceHub 是面向财务核算、租赁业务、资产转让、凭证生成、金蝶对接、对账和报表分析的内部财务中台系统。

系统核心目标：

- 汇聚多个业务系统的合同、还款、银行流水、费用、资产和凭证数据。
- 通过可配置的业务场景、字段映射、规则和科目体系生成财务凭证。
- 支撑租赁、资产转让、减值计提、长期应收、服务费、税费、保证金、核销等财务业务处理。
- 对接金蝶 EAS，完成基础资料同步、凭证推送、凭证查询和结果回写。
- 提供运营、财务、管理人员使用的 Web 工作台、查询、审核、报表和异常处理能力。

## 3. 总体架构

系统采用前后端分离和 Spring Cloud 微服务架构。

```text
Browser
  |
  | HTTP /api
  v
financehub-web  : Vue 3 + Vite
  |
  | Vite proxy / production reverse proxy
  v
financehub-gateway : Spring Cloud Gateway
  |
  +--> financehub-auth          : 登录、SSO、用户认证
  +--> financehub-admin-service : 用户、角色、字典、审计
  +--> financehub-engine-service: 财务引擎、业务处理、凭证规则、报表
  +--> financehub-etl-service   : 外部系统数据同步、金蝶同步、定时任务
  +--> financehub-moji-service  : 文档中存在配置，当前源码缺失

Shared Infrastructure:
  - Nacos: 配置中心、服务注册发现
  - MySQL: 业务主库
  - Redis: token、缓存、临时状态
  - RabbitMQ: 异步业务消息、队列消费
  - XXL-JOB: 定时任务调度
  - Sentinel: 网关和服务限流熔断
  - Kingdee EAS / 外部业务系统: 第三方数据源和凭证目标系统
```

## 4. 源码模块

| 模块 | 技术 | 默认端口 | 职责 |
| --- | --- | --- | --- |
| `financehub-parent` | Maven parent | N/A | 统一版本、依赖管理、插件配置 |
| `financehub-common` | Spring Boot library | N/A | 公共核心、Redis、安全、Swagger、MyBatis、日志等基础能力 |
| `financehub-gateway` | Spring Cloud Gateway | `8888` | 统一入口、路由、鉴权过滤、限流配置 |
| `financehub-auth` | Spring Boot | `8200` | 登录、SSO、token、用户信息接口 |
| `financehub-admin` | Spring Boot | `8201` | 用户、角色、组织、字典、操作日志 |
| `financehub-engine` | Spring Boot | `8202` | 财务引擎、凭证规则、业务单据、合同、报表、审批和查询 |
| `financehub-etl` | Spring Boot | `8203` | 外部业务数据同步、金蝶数据同步、ETL 任务、XXL-JOB handler |
| `financehub-web` | Vue 3 + Vite | `8899` | 前端工作台和业务操作界面 |

## 5. 后端服务职责

### 5.1 Gateway

`financehub-gateway` 是所有前端 API 请求的统一入口。

职责：

- 接收 `/api/**` 或生产网关请求。
- 根据 Nacos 服务发现转发到 `auth`、`admin`、`engine`、`etl` 等服务。
- 执行白名单、token、权限和请求日志过滤。
- 承载 Sentinel gateway 限流规则。

当前本地验证：

- 监听 `8888`。
- `/auth/sso/login` 登录成功。
- `/admin/**` 可正常转发。
- `/engine/**` 已能路由到本地 engine。

### 5.2 Auth

`financehub-auth` 负责认证和 SSO。

职责：

- 用户登录。
- token 生成与校验。
- 用户信息查询。
- SSO 相关接口。

当前本地验证：

- 监听 `8200`。
- `POST /auth/sso/login` 返回 token。
- `GET /auth/sso/getUserInfo` 可获取用户信息。

### 5.3 Admin

`financehub-admin-service` 是系统管理服务。

职责：

- 用户管理。
- 角色与数据权限。
- 组织与内部用户绑定。
- 字典类型、字典数据。
- 操作日志。

主要 controller：

- `SysInternalUserController`
- `SysInternalRoleController`
- `SysInternalRoleOrgController`
- `SysDictTypeController`
- `SysDictDataController`
- `SysOperlogController`

### 5.4 Engine

`financehub-engine-service` 是业务核心。

职责：

- 财务凭证规则配置和生成。
- 会计场景、字段映射、科目、税率、业务配置。
- 租赁、合同、回款、费用、保证金、服务费、减值、长期应收、核销等业务处理。
- 资产转让、折价转让、ABS 出表、第三方支付等业务处理。
- 凭证查询、凭证报表、余额查询。
- 对账、异常消息、数据执行任务。
- 审批消息消费和业务状态回写。
- 外部接口调用和金蝶凭证接口调用。

主要业务包：

- `scene`: 会计场景、字段、科目、税率、凭证模板。
- `rule`: 规则执行、接口数据、MQ 错误消息。
- `finance`: 合同、凭证、核算、报表、资产转让、减值、税费等主体业务。
- `claim`: 报销/认领类业务。
- `verification`: 核销、诉讼费等核验业务。
- `integration`: 金蝶和组织外部接口。
- `approve`: 审批 MQ 监听和状态处理。

当前本地验证：

- 已启动并监听 `8202`。
- 已注册到 Nacos。
- 数据库 `SELECT 1` 成功。
- RabbitMQ 未运行，因此 MQ listener 持续报连接错误，但 HTTP 服务已启动。

### 5.5 ETL

`financehub-etl-service` 负责外部数据同步和定时任务。

职责：

- 从外部业务系统拉取还款计划、回款、合同等数据。
- 同步金蝶基础资料和凭证状态。
- 执行报表同步和对账数据同步。
- 提供 ETL API 供 engine Feign 调用。
- 承载 XXL-JOB 定时任务 handler。

主要 controller：

- `KingdeeDataSyncController`
- `KingdeeEasController`
- `RepaymentPlanController`
- `VoucherTransactionController`
- `InvoiceClaimController`
- `MicroDataController`
- `PlatformDataController`
- `CommvehDataController`
- `PassvehDataController`

当前状态：

- 当前未运行，`8203` 未监听。
- 本地曾验证 ETL 可启动，但需要补 XXL-JOB、EAS 等配置。

## 6. 前端架构

`financehub-web` 使用 Vue 3 + Vite。

主要功能域来自 `src/views`：

- 系统管理：用户、角色、数据权限、凭证生成。
- 引擎配置：业务配置、场景配置、规则配置、字段映射、科目、税率、字典。
- 查询业务：合同、客户、凭证、科目、余额、批量查询。
- 租赁业务：合同录入、租金确认、核销、保证金、服务费、税费、尾差调整。
- 资产转让：普通转让、折价转让、第三方转让、ABS 出表。
- 计量引擎：收益计提、减值计提、咨询服务费。
- 对账报表：租赁应收、保险费、保证金、金蝶科目、异常报表。
- 数据治理：指标、主数据配置、异常消息。
- 自制凭证：自制凭证新增、详情、查询。
- 门户入口：待审批、首页、价值映射。

当前本地配置：

- dev server 监听 `8899`。
- `/api` 代理已指向 `http://127.0.0.1:8888/`。

## 7. 数据架构

### 7.1 主数据库

当前本地 MySQL：

- `financialdb4`: 主业务库，当前本地有 76 张表。
- `moji`: 文档记录存在，当前有 20 张表。

主要数据类型：

- 用户、角色、字典、审计。
- 合同、还款计划、回款、费用、保证金。
- 凭证头、凭证分录、凭证规则、凭证场景。
- 金蝶基础数据、科目、币种、客户、辅助核算。
- 对账数据、异常消息、任务记录。

### 7.2 Redis

用途：

- token 和用户会话。
- 业务临时缓存。
- 文件服务 token 缓存。
- 分布式状态或幂等控制。

### 7.3 RabbitMQ

用途：

- 业务数据异步导入。
- 还款计划、交易数据、银行流水、审批消息等异步消费。
- 错误消息重推。

当前状态：

- 本地未运行。
- engine 已启动，但 Rabbit listener 持续报连接错误。

## 8. 关键业务流

### 8.1 登录访问流

1. 用户访问 `financehub-web`。
2. 前端调用 `/api/auth/sso/login`。
3. Vite 或生产反向代理转发到 gateway。
4. gateway 转发到 auth。
5. auth 返回 token。
6. 前端后续请求携带 token。
7. gateway 鉴权后转发到 admin/engine/etl。

### 8.2 凭证生成流

1. 财务人员配置业务场景、字段映射、科目、规则、税率。
2. 外部业务数据进入 engine 或 ETL。
3. engine 根据场景和规则生成凭证头和分录。
4. 凭证进入查询、审核、推送或异常处理流程。
5. engine 或 etl 调用金蝶接口推送凭证。
6. 同步金蝶结果并回写系统状态。

### 8.3 外部数据同步流

1. XXL-JOB 触发 ETL 任务。
2. ETL 从金蝶或各业务系统拉取数据。
3. ETL 落库或通过 Feign/API 提供给 engine。
4. engine 消费数据并执行业务核算、对账或凭证生成。
5. 异常通过数据治理或异常消息模块处理。

### 8.4 MQ 异步处理流

1. 业务系统或 engine 发送消息到 RabbitMQ。
2. engine listener 消费指定队列。
3. 执行业务落库、状态更新、凭证生成或错误记录。
4. 失败消息进入错误消息处理或重推流程。

## 9. 外部依赖

| 依赖 | 用途 | 当前本地状态 |
| --- | --- | --- |
| Nacos | 配置中心、服务发现 | 已运行 |
| MySQL | 主业务库 | 已运行 |
| Redis | 缓存、token | 已运行 |
| RabbitMQ | 异步消息 | 未运行 |
| XXL-JOB Admin | 定时调度 | 未运行 |
| Kingdee EAS | 凭证和基础资料 | 使用占位/远端测试地址 |
| Sentinel Dashboard | 限流控制台 | 未运行 |
| Nexus Maven | 私有依赖 | DNS 当前不可解析 |

## 10. 当前本地运行矩阵

| 组件 | 状态 | 端口 | 说明 |
| --- | --- | --- | --- |
| Nacos | 运行 | `8848` | `status=UP` |
| MySQL | 运行 | `3306` | `SELECT 1` 成功 |
| Redis | 运行 | `6379` | `PONG` |
| Auth | 运行 | `8200` | 登录成功 |
| Admin | 运行 | `8201` | 字典接口成功 |
| Gateway | 运行 | `8888` | 路由正常 |
| Engine | 运行 | `8202` | HTTP 可用，RabbitMQ 报错 |
| Web | 运行 | `8899` | 首页 200 |
| ETL | 未运行 | `8203` | 需单独启动 |
| RabbitMQ | 未运行 | `5672/15672` | engine 日志持续报错 |
| XXL-JOB Admin | 未运行 | 常见 `8080` 或自定义 | executor 本地启动但未注册 |

## 11. 工程风险

1. 分支和 API 版本不一致
   - 当前 engine 分支需要较新的 etl-api，但 etl 工作树在 master。
   - 建议统一后端仓库分支基线。

2. 私有依赖不可复现
   - `LTPATokenManager` 缺失，Nexus DNS 不可解析。
   - 本地 stub 只能用于启动验证。

3. 配置漂移
   - 大量配置依赖 Nacos。
   - 本地通过命令行补参数可启动，但不是长期方案。

4. MQ 强依赖
   - engine 可启动，但 RabbitMQ 未运行会持续报错。
   - 完整业务必须补 RabbitMQ。

5. ETL 与 engine 强耦合
   - engine 通过 Feign 依赖 ETL API。
   - 分支不一致会导致编译失败或运行接口不兼容。

6. 业务域庞大
   - 当前 engine 集中了大量业务 controller 和 service。
   - 后续维护需要更清晰的领域边界和集成测试。

## 12. 建议演进方向

- 固化本地开发 profile，把 `.local/run-engine-local.sh` 中的参数迁移到 Nacos 本地配置。
- 补齐 RabbitMQ 和 XXL-JOB Admin，完成完整链路验证。
- 统一 engine/etl/admin/common 的分支和版本，建立可重复构建的 BOM。
- 将私有 jar 纳入公司 Nexus 或内部制品库，并提供 settings 模板。
- 为登录、凭证生成、合同查询、金蝶推送、MQ 消费建立最小自动化回归测试。
- 梳理 engine 内部业务域，减少跨域 service 直接依赖。
