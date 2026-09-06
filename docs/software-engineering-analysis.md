# FinanceHub 软件工程分析

## 1. 总体评价

FinanceHub 是一个典型的企业内部财务中台系统，业务覆盖面广，采用 Spring Cloud 微服务、Vue 前端、Nacos 配置注册、MySQL/Redis/RabbitMQ/XXL-JOB 等常见企业技术栈。

从工程角度看，系统已经具备清晰的服务拆分雏形，但当前存在可重复构建、配置治理、分支一致性、外部依赖管理和测试自动化不足的问题。业务核心集中在 `financehub-engine-service`，该服务承担了大量领域逻辑，后续维护风险主要来自复杂度和集成依赖。

## 2. 优点

- 服务拆分方向合理：gateway/auth/admin/engine/etl/web 分工清晰。
- 前后端分离，前端业务菜单和后端接口边界基本对应。
- Nacos 统一配置和服务发现，适合多环境部署。
- engine 提供较完整的财务业务模型和规则配置能力。
- ETL 独立承载外部同步和定时任务，避免全部塞入 engine。
- 使用 MyBatis-Plus、Druid、Redis、RabbitMQ、XXL-JOB 等成熟组件。
- 当前基础链路已经可本地启动，说明系统具备可调试基础。

## 3. 主要工程问题

### 3.1 构建不可重复

表现：

- 本地缺 `~/.m2/settings.xml`。
- 公司 Nexus DNS 当前不可解析。
- 私有依赖 `LTPATokenManager` 缺失。
- engine 构建依赖 etl-api 的特定分支。

影响：

- 新人无法稳定拉起项目。
- CI/CD 难以复现。
- 本地调试需要手工修补依赖。

建议：

- 提供标准 Maven settings 模板。
- 将 `LTPATokenManager` 放入可访问制品库。
- 建立后端多仓库版本矩阵。
- 用 BOM 或 parent 约束内部模块版本。

### 3.2 分支和 API 不一致

表现：

- engine 当前分支 `V20260114-fix`。
- etl 当前工作树为 `master`。
- engine 需要 `OperationBusinessFacade` 和 `OrgPeriodDTO`，但 master 的 etl-api 不包含。

影响：

- engine 编译失败。
- 即使编译通过，也可能出现运行期 Feign 接口不兼容。

建议：

- 每个发布版本明确对应的仓库分支和 commit。
- 建立集成分支或 monorepo 聚合构建。
- 在 CI 中编译所有服务和 API 包。

### 3.3 配置治理不足

表现：

- 大量启动配置散落在 Nacos、命令行、脚本和默认 bootstrap 中。
- engine 启动需要大量占位参数。
- 本地需要 `.local/run-engine-local.sh` 才能补齐配置。

影响：

- 环境迁移困难。
- 配置缺失时启动失败，排查成本高。
- 不同开发人员环境容易漂移。

建议：

- 为 `financehub-local` 建立完整配置清单。
- 区分必须配置、可选配置、外部服务配置。
- 对本地开发提供可提交的 sample 配置，不提交敏感值。
- 在服务启动时对关键配置做结构化校验并输出明确提示。

### 3.4 Engine 领域复杂度高

表现：

- `financehub-engine-service` controller 数量很多。
- 合同、凭证、租赁、资产转让、计提、对账、审批、MQ、外部接口都集中在一个服务。

影响：

- 编译和启动依赖多。
- 单服务变更影响面大。
- 测试边界难以控制。

建议：

- 保持短期稳定，不急于拆服务。
- 先按包和领域建立架构边界文档。
- 为核心领域增加集成测试。
- 后续可考虑将报表、凭证推送、数据治理等能力模块化。

### 3.5 外部依赖强耦合

表现：

- RabbitMQ 未运行时 engine 可启动但持续报错。
- XXL-JOB Admin 不存在时 executor 只能本地启动。
- 金蝶 EAS 配置和 LTPA 依赖缺失影响真实功能。

影响：

- 本地开发噪音大。
- 无法独立验证部分 HTTP 功能。
- 对外部系统不可用的降级能力不足。

建议：

- 本地 profile 默认关闭 MQ listener 或提供 mock RabbitMQ。
- 对 XXL-JOB、EAS、附件系统增加 feature flag。
- 将外部接口 client 做适配层，提供 mock 实现。

### 3.6 测试体系不足

当前未看到足够的自动化测试支撑复杂业务。

建议测试分层：

- 单元测试：规则计算、金额计算、日期/账期转换。
- 集成测试：MyBatis mapper、凭证生成、合同查询。
- 契约测试：engine 与 etl-api、admin-api、auth-api。
- 冒烟测试：登录、用户信息、字典查询、engine 基础接口。
- 端到端测试：前端登录和核心页面加载。

## 4. 当前本地环境结论

已可用：

- Nacos、MySQL、Redis。
- auth/admin/gateway/web。
- engine HTTP 服务。
- 前端代理到本地 gateway。
- engine 数据库连接。

未完整：

- RabbitMQ。
- ETL。
- XXL-JOB Admin。
- moji 服务源码/运行服务。
- 金蝶真实 LTPA 私有依赖。

这意味着当前环境适合：

- 前端页面访问。
- 登录和权限基础联调。
- admin 基础接口联调。
- engine HTTP 接口和数据库查询类接口初步联调。

当前环境不适合：

- MQ 消费链路验证。
- ETL 同步链路验证。
- 定时任务调度链路验证。
- 金蝶真实凭证推送验证。

## 5. 推荐工程路线

### 阶段 1：可重复启动

- 固化本地 Nacos 配置。
- 补 RabbitMQ。
- 补 Maven settings 和私有 jar。
- 统一 engine/etl 分支。
- 将启动脚本变成标准开发脚本。

### 阶段 2：可重复构建

- 建立后端聚合构建脚本。
- 固定内部 API 版本。
- 建立 CI 编译检查。
- 产出本地开发环境一键检查命令。

### 阶段 3：核心业务可验证

- 选一个最小业务闭环，例如合同查询到凭证生成。
- 建立测试数据。
- 建立接口级回归测试。
- 建立前端关键页面冒烟测试。

### 阶段 4：生产化质量提升

- 配置审计。
- 日志和链路追踪。
- MQ 和任务调度监控。
- 异常重试闭环。
- 数据质量报表。

## 6. 工程 checklist

P0：

- `~/.m2/settings.xml` 可用。
- `LTPATokenManager` 可从制品库下载。
- engine/etl/common/admin 分支矩阵明确。
- RabbitMQ 本地启动。
- Nacos local 配置完整。

P1：

- ETL 启动。
- XXL-JOB Admin 启动。
- 金蝶测试环境连通。
- engine MQ listener 无连接错误。

P2：

- 自动化冒烟测试。
- API 契约测试。
- 核心规则单元测试。
- 本地环境健康检查脚本。

## 7. 结论

FinanceHub 当前不是一个简单 CRUD 系统，而是包含财务规则、数据集成、异步消息、定时任务和外部系统对接的复杂企业应用。优先事项不应是继续堆功能，而是先把“可重复构建、可重复启动、可重复验证”做好。

短期建议聚焦：

- 统一依赖和分支。
- 补齐 RabbitMQ/ETL/XXL-JOB。
- 固化本地 Nacos 配置。
- 建立核心链路冒烟测试。

完成这些后，再推进业务功能梳理和架构拆分会更稳。
