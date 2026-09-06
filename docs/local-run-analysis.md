# FinanceHub 本地启动可行性分析

本文档基于当前 Mac 环境和 `code/` 目录下源码做启动分析。目标是判断是否能在本地从源码启动整套程序，并列出需要补齐的组件、依赖顺序和启动步骤。

## 1. 当前结论

当前环境具备部分基础设施，但还不能直接从源码完整启动整套程序。

### 1.1 2026-06-08 本地 Redis/MySQL 配置处理记录

已完成本地 Redis/MySQL 连接配置确认：

- Redis 已运行并监听 `127.0.0.1:6379`，`redis-cli ping` 返回 `PONG`。
- MySQL 已运行并监听 `127.0.0.1:3306`，版本为 `8.0.46`。
- 本地 MySQL 账号 `db01/1qaz@wsx` 可访问 `financialdb4` 和 `moji`。
- `financialdb4` 当前有 76 张表，`moji` 当前有 20 张表。
- 本地 Nacos 已由 `launchctl` 托管启动，API 端口为 `8848`，Console 端口为 `8080`。
- Nacos namespace/group `financehub-local` / `financehub-local` 下已有本地配置：
  - `financehub-dev.yml`
  - `financehub-auth-dev.yml`
  - `financehub-admin-service-dev.yml`
  - `financehub-engine-service-dev.yml`
  - `financehub-gateway-dev.yml`
  - `financehub-moji-service-dev.yml`
  - `sentinel-financehub-gateway.json`
- `financehub-dev.yml` 中 Redis 指向 `127.0.0.1:6379`，MySQL 指向 `jdbc:mysql://127.0.0.1:3306/financialdb4`。
- 源码中后端服务的本地开发 Nacos 配置已调整为指向 `127.0.0.1:8848`、namespace/group `financehub-local` / `financehub-local`。

### 1.2 2026-06-08 auth/admin/gateway 启动验证记录

已按 `auth -> admin-service -> gateway` 顺序启动并验证基础登录链路：

- `financehub-auth` 已由 `launchctl` 托管，label 为 `financehub.auth`，监听 `8200`。
- `financehub-admin-service` 已由 `launchctl` 托管，label 为 `financehub.admin`，监听 `8201`。
- `financehub-gateway` 已由 `launchctl` 托管，label 为 `financehub.gateway`，监听 `8888`。
- 三个服务启动时均使用 JDK 8：`/Users/peyton/.jdks/amazon-corretto-8.jdk/Contents/Home/bin/java`。
- 三个服务均强制设置 `spring.cloud.nacos.discovery.ip=127.0.0.1`，避免 gateway 通过 Nacos 解析到非回环网卡 IP 后转发超时。
- 日志目录：`/Users/peyton/financehub-dev/logs/local-services/`。
- 网关登录验证通过：
  - `POST http://127.0.0.1:8888/auth/sso/login`
  - body: `{"username":"admin","password":"123456"}`
  - 返回 token：`5206955594014ef38071842efa82d4c1`
- 网关用户信息验证通过：
  - `GET http://127.0.0.1:8888/auth/sso/getUserInfo`
  - header: `Authorization: 5206955594014ef38071842efa82d4c1`
  - 返回用户：`admin` / `local-admin`
- 网关到 admin-service 的数据库查询链路验证通过：
  - `GET http://127.0.0.1:8888/admin/dict/data/list`
  - 返回：`{"total":0,"rows":[],"code":200,"msg":"查询成功"}`

停止本地服务：

```bash
launchctl remove financehub.gateway
launchctl remove financehub.admin
launchctl remove financehub.auth
```

### 1.3 2026-06-09 Node/前端启动记录

已安装并验证 Node 16：

- Homebrew core 已不再提供 `node@16`，因此使用 Homebrew 安装 `nvm`，再通过 `nvm` 安装 Node。
- Node 版本：`v16.20.2`。
- npm 版本：`8.19.4`。
- 前端项目已增加 `.nvmrc`：`code/financehub-web/.nvmrc`。
- 因现有 `node_modules/.bin` 下命令缺执行权限，已统一执行 `chmod +x node_modules/.bin/*`。
- 因现有 `node_modules` 中 `esbuild` 是 Windows 二进制，已执行 `npm rebuild esbuild`，修复为当前 macOS arm64 可用版本。
- 前端 dev server 已由 `launchctl` 托管，label 为 `financehub.web`，监听 `8899`。
- 访问地址：`http://localhost:8899/`。
- 日志文件：`/Users/peyton/financehub-dev/logs/local-services/web.log`。

手动启用 Node 16：

```bash
export NVM_DIR="$HOME/.nvm"
. /opt/homebrew/opt/nvm/nvm.sh
cd /Users/peyton/financehub-dev/code/financehub-web
nvm use
node -v
npm -v
```

停止前端 dev server：

```bash
launchctl remove financehub.web
```

### 1.4 2026-06-09 基础栈优先启动记录

当前先不启动 `engine` / `etl`，优先保持基础链路可用：

- Nacos 已运行，监听 `8848`，`/nacos/v1/ns/operator/metrics` 返回 `{"status":"UP"}`。
- MySQL 已运行，监听 `127.0.0.1:3306`，账号 `db01/1qaz@wsx` 可执行 `SELECT 1`。
- Redis 已运行，监听 `127.0.0.1:6379`，`redis-cli ping` 返回 `PONG`。
- `financehub-auth` 已运行，监听 `8200`。
- `financehub-admin-service` 已运行，监听 `8201`。
- `financehub-gateway` 已运行，监听 `8888`。
- `financehub-web` 已运行，监听 `8899`，首页 HTTP 状态码为 `200`。
- 前端开发代理已切到本地网关：
  - 配置文件：`code/financehub-web/src/config/compile.config.js`
  - `/api` target：`http://127.0.0.1:8888/`
  - 验证请求：`POST http://127.0.0.1:8899/api/auth/sso/login`
  - 返回：`{"code":200,"msg":null,"data":"5206955594014ef38071842efa82d4c1"}`
  - gateway 日志中确认最终请求为 `http://127.0.0.1:8888/auth/sso/login`。
- 网关登录验证通过：
  - `POST http://127.0.0.1:8888/auth/sso/login`
  - body: `{"username":"admin","password":"123456"}`
  - 返回：`{"code":200,"msg":null,"data":"5206955594014ef38071842efa82d4c1"}`
- 该阶段未启动 `engine` 时，前端页面中触发 `/engine/**` 请求会经由本地 gateway 到 `8888`，但 gateway 会返回 `financehub-engine-service` 实例不可用；后续已单独检查并启动 engine，见 1.5。
- 已停止 `financehub.etl`，`8202` / `8203` 当前没有监听服务。

当前基础栈停止命令：

```bash
launchctl remove financehub.web
launchctl remove financehub.gateway
launchctl remove financehub.admin
launchctl remove financehub.auth
```

### 1.5 2026-06-09 engine 启动检查记录

已检查并启动 `financehub-engine-service`，当前结论：不是后端数据库没连上，主要阻塞在私有依赖、API 分支匹配和外部 RabbitMQ。

处理过程：

- 初始状态 `8202` 未监听，`financehub-engine-service/target` 下没有可执行 jar。
- 直接构建失败，原因是缺私有依赖 `com.kingdee:LTPATokenManager:1.0.0`：
  - 本地只有 `.lastUpdated` 失败记录，没有 jar。
  - `nexus.utfinancing.com` 当前本机 DNS 无法解析。
  - 为了继续做本地启动验证，临时安装了本地 stub jar 到 `~/.m2`，只用于启动验证，不能验证真实金蝶 LTPA token 功能。
- 越过 LTPA 后，构建暴露 `financehub-engine` 和本地 `financehub-etl-api` 不匹配：
  - engine 当前分支：`V20260114-fix`
  - etl 当前工作树分支：`master`
  - engine 需要 `OperationBusinessFacade` 和 `com.utfinancing.financehub.etl.model.dto.OrgPeriodDTO`
  - 这两个 API 在 etl 的 `origin/lease_income_yypt` 分支中存在。
  - 已用临时 worktree `.local/financehub-etl-lease` 安装该分支的 `financehub-etl-api` 到本地 Maven 仓库，未切换当前 etl 工作树。
- `financehub-engine-service` 已成功打包：
  - `code/financehub-engine/financehub-engine-service/target/financehub-engine-service.jar`
- 已创建本地启动脚本：
  - `.local/run-engine-local.sh`
  - 该脚本补齐本地 Nacos、XXL-JOB、EAS、附件服务、审批 URL 等启动占位配置。
- `financehub-engine-service` 已由 `launchctl` 托管启动：
  - label：`financehub.engine`
  - HTTP 端口：`8202`
  - XXL-JOB executor 端口：`9998`
  - 日志：`logs/local-services/engine.log`
- Nacos 注册成功：
  - `financehub-local financehub-engine-service 127.0.0.1:8202 register finished`
- 数据库连接已验证成功：
  - engine 日志中 Druid 执行多次 `SELECT 1` 成功。
- gateway 到 engine 的路由已不再返回 `financehub-engine-service` 实例不可用：
  - `GET http://127.0.0.1:8888/engine/actuator/health`
  - 当前返回 `{"code":401,"msg":"令牌不能为空"}`，说明请求已经路由到本地 gateway 鉴权链路，而不是 503 找不到 engine。

当前剩余风险：

- RabbitMQ 未运行，engine 启动后 Rabbit listener 持续报：
  - `Failed to check/redeclare auto-delete queue(s)`
  - `connection error`
- XXL-JOB Admin 未配置，当前 `xxl.job.admin.addresses` 为空，只能启动本地 executor，不能注册到调度中心。
- LTPA 使用的是本地 stub，仅用于启动验证；真实金蝶登录/token 相关功能需要公司私有 jar。
- engine 依赖的 etl-api 来自临时 worktree 的 `origin/lease_income_yypt`，后续最好统一 engine/etl 分支版本。

停止 engine：

```bash
launchctl remove financehub.engine
```

主要阻塞项：

- JDK 8 已安装到用户目录，但当前默认 `java` 仍是 JDK 21，Maven 默认仍运行在 OpenJDK 26；后续需要为项目显式切换 `JAVA_HOME`。
- 缺 Maven 私服配置。本地没有 `~/.m2/settings.xml`，也没有 `com.utfinancing.financehub` 内部包缓存。
- 缺私有依赖 `com.kingdee:LTPATokenManager:1.0.0`。本地只有 `.lastUpdated` 失败记录。
- 缺 RabbitMQ。`engine` / `etl` 存在 RabbitMQ listener 和 template，完整业务启动需要 RabbitMQ。

如果先补齐 JDK 8、Maven 私服配置和 RabbitMQ，并确认本地 Nacos 配置完整，整套程序具备本地启动条件。

## 2. 主要阻塞项清单

后续可以按下面列表逐项解决。建议不要跳过前 4 项，因为它们直接决定源码能否构建和启动。

| 序号 | 阻塞项 | 当前状态 | 影响范围 | 是否必须 | 建议优先级 | 解决后验证方式 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | JDK 8 环境切换 | JDK 8 已安装：`/Users/peyton/.jdks/amazon-corretto-8.jdk/Contents/Home`；当前默认 `java` 仍是 JDK 21，Maven 默认仍是 OpenJDK 26 | 所有后端 Maven 构建、IDE 启动 | 必须 | P0 | 设置 `JAVA_HOME` 后，`java -version`、`javac -version`、`mvn -version` 都指向 Java 8 |
| 2 | Maven 私服配置缺失 | `~/.m2/settings.xml` 不存在 | 后端依赖下载、内部包安装、私有 jar 下载 | 必须 | P0 | `mvn dependency:resolve` 能拉取公司 Nexus 依赖 |
| 3 | 内部 Maven 模块未安装 | 本地仓库没有 `com.utfinancing.financehub` | 各服务互相依赖无法解析 | 必须 | P0 | `~/.m2/repository/com/utfinancing/financehub` 出现本地安装产物 |
| 4 | `LTPATokenManager` 私有依赖缺失 | 本地只有 `.lastUpdated` 失败记录 | `financehub-engine-service` 构建失败 | 必须，至少启动 engine 必须 | P0 | 本地存在 `LTPATokenManager-1.0.0.jar`，engine 可 `mvn package` |
| 5 | Nacos 配置完整性未确认 | Nacos 在运行，但共享配置内容未确认 | 数据库、Redis、RabbitMQ、网关路由、XXL-JOB 等配置 | 必须 | P0 | Nacos 中存在 `financehub-local` / `financehub-local` / `financehub-dev.yml`，服务启动能读取配置 |
| 6 | RabbitMQ 缺失 | `5672`、`15672` 未监听 | `engine` / `etl` MQ 消费、发送和队列声明 | 完整启动必须 | P1 | RabbitMQ 管理台可访问，服务启动无 RabbitMQ 连接错误 |
| 7 | Node/npm 缺失 | 已通过 nvm 安装 Node `v16.20.2` / npm `8.19.4`，前端 Vite 已可启动 | 前端 Vite 开发服务 | 已完成 | P1 | `nvm use` 后 `node -v`、`npm -v` 正常，`npm run serve` 可执行 |
| 8 | 数据库初始化状态未确认 | MySQL 在运行，但库表和基础数据未确认 | 服务启动、登录、业务查询 | 必须 | P1 | 后端启动无表缺失错误，登录/基础接口可用 |
| 9 | 前端代理指向本地 | 已将 `/api` 代理切到 `http://127.0.0.1:8888/` | 前端请求会打到本地 gateway；不启动 engine 时 `/engine/**` 会返回服务不可用 | 已完成 | P2 | `POST http://127.0.0.1:8899/api/auth/sso/login` 返回 `code=200`，gateway 日志 host 为 `127.0.0.1:8888` |
| 10 | PostgreSQL 未安装/未运行 | `psql` 不存在，`5432` 未监听 | 取决于 Nacos 动态数据源 | 待确认 | P2 | Nacos 数据源中如无 PostgreSQL 可忽略；如有则需连通 |
| 11 | XXL-JOB Admin 未确认 | 未发现明确运行中的 XXL-JOB Admin | `engine` / `etl` 定时任务注册和调度 | 待确认 | P2 | Nacos `xxl.job.admin.addresses` 可访问，服务启动无注册异常 |
| 12 | Sentinel Dashboard 未运行 | `8718` 未监听 | Sentinel 控制台不可用 | 通常非必须 | P3 | 需要控制台时启动 Dashboard；基础服务启动通常可先忽略 |
| 13 | `financehub-moji` 源码缺失 | 部署目录有 jar，源码目录未看到对应模块 | 涉及 `/moji/**` 的功能不可源码调试 | 完整源码调试需要 | P3 | 补齐 `financehub-moji` 源码，或确认前端/网关不依赖该服务 |

### 2.1 推荐解决顺序

建议按这个顺序推进：

1. 安装并切换 JDK 8。
2. 配置 Maven 私服 settings。
3. 解决 `LTPATokenManager` 私有 jar。
4. 按顺序安装内部 Maven 模块。
5. 确认 Nacos `financehub-local` 配置完整。
6. 确认 MySQL 库表和基础数据。
7. 先不启动 `engine-service` / `etl-service`，暂不处理 RabbitMQ、XXL-JOB、EAS 等外部依赖。
8. 优先保持 Nacos、MySQL、Redis、`auth`、`admin-service`、`gateway`、`web` 可用。
9. 验证基础登录链路和前端访问。
10. 后续需要跑完整业务时，再启动或安装 RabbitMQ。
11. 再启动 `engine-service`、`etl-service`，逐个处理 RabbitMQ、XXL-JOB、第三方接口问题。

### 2.2 后续逐项解决建议

每解决一个阻塞项，都建议记录三件事：

- 实际改动：安装了什么、改了什么配置。
- 验证命令：用什么命令确认问题已解决。
- 剩余风险：是否只是解决了构建，还是已经确认运行可用。

这样后面可以把本文档作为本地启动 checklist 逐项打勾，而不是每次重新排查。

## 3. 源码模块关系

源码位于 `code/` 目录下，后端不是一个单一聚合工程，而是多个 Maven 仓库并列：

- `financehub-parent`：父 POM，统一版本和依赖管理。
- `financehub-common`：公共基础模块。
- `financehub-gateway`：网关服务，默认端口 `8888`。
- `financehub-auth`：认证服务，默认端口 `8200`。
- `financehub-admin`：后台管理服务，包含 `financehub-admin-api` 和 `financehub-admin-service`，默认端口 `8201`。
- `financehub-engine`：会计引擎服务，包含 `financehub-engine-api` 和 `financehub-engine-service`，默认端口 `8202`。
- `financehub-etl`：ETL 服务，包含 `financehub-etl-api` 和 `financehub-etl-service`，默认端口 `8203`。
- `financehub-web`：Vue 3 + Vite 前端，默认开发端口 `8899`。

需要注意版本差异：

- 大部分内部模块版本为 `1.0-SNAPSHOT`。
- `financehub-engine-api` 版本为 `1.0.0-SNAPSHOT`。
- `financehub-etl-api` 版本为 `1.0.0-SNAPSHOT`。

## 4. 当前环境状态

已具备：

- Homebrew 已安装。
- Git 已安装。
- Maven 已安装，版本 `3.9.16`。
- Nacos 已运行，端口 `8848`。
- MySQL 已运行，端口 `127.0.0.1:3306`。
- Redis 已运行，端口 `127.0.0.1:6379`。
- `auth`、`admin-service`、`gateway` 已启动，分别监听 `8200`、`8201`、`8888`。
- 前端 Vite dev server 已启动，监听 `8899`。

不满足或缺失：

- JDK 8 已安装，但默认环境尚未切换。
- Maven 当前使用 OpenJDK 26。
- Node/npm 已通过 nvm 可用；默认 shell 需要先加载 nvm 后使用。
- `~/.m2/settings.xml` 不存在。
- 本地 Maven 仓库没有 `com.utfinancing.financehub` 内部模块。
- `com.kingdee:LTPATokenManager:1.0.0` 下载失败。
- RabbitMQ 未安装/未运行，`5672`、`15672` 未监听。
- Docker 未安装。
- PostgreSQL 未安装/未运行，`5432` 未监听。
- Sentinel Dashboard 未运行，`8718` 未监听。
- XXL-JOB Admin 未确认。

## 5. 必备组件

### 5.1 后端必备

1. JDK 8
   - 后端 POM 中 `java.version` 为 `1.8`。
   - 建议让 `java`、`javac`、Maven 和 IDE 都使用 JDK 8。

2. Maven 私服配置
   - 父 POM 中配置了公司 Nexus：
     - `http://nexus.utfinancing.com/repository/releases/`
     - `http://nexus.utfinancing.com/repository/snapshots/`
     - `http://nexus.utfinancing.com/repository/public`
   - 当前没有 `~/.m2/settings.xml`，需要补公司 Maven settings。
   - Maven 3.9 对 HTTP 仓库可能有限制，需要确认 settings 中 mirror / allow-http 配置。

3. 内部 Maven 模块
   - 本地 Maven 仓库当前没有 `com.utfinancing.financehub`。
   - 需要按顺序 `mvn install`。

4. 私有 jar
   - `financehub-engine-service` 依赖 `com.kingdee:LTPATokenManager:1.0.0`。
   - 当前本地只有 `.lastUpdated`，说明下载失败。
   - 需要通过公司 Nexus 下载，或手动安装该 jar 到本地仓库。

5. Nacos
   - 源码里的 `bootstrap.yml` 主要只包含服务名、端口和 Nacos 地址。
   - 数据库、Redis、RabbitMQ、路由、XXL-JOB 等关键配置应来自 Nacos。
   - 当前本机 Nacos 已运行，但需要确认 `financehub-local` namespace/group 下配置完整。

6. MySQL
   - 当前本机 MySQL 已运行。
   - 还需要确认库、账号、表结构和基础数据是否已初始化。

7. Redis
   - 当前本机 Redis 已运行。
   - 需要确认 Nacos 配置指向本地 Redis，或本地能访问配置里的 Redis。

8. RabbitMQ
   - 当前缺失。
   - `engine` 和 `etl` 有 RabbitMQ 相关代码，完整启动建议准备。

### 5.2 前端必备

1. Node.js
   - `financehub-web/package.json` 中 engines 为 `^12 || >=14`。
   - 已安装 Node `v16.20.2`，通过项目 `.nvmrc` 固定版本。

2. npm
   - 已安装 npm `8.19.4`。
   - 已验证 `npm run serve` 可启动 Vite dev server。

3. 前端代理配置
   - `financehub-web/src/config/compile.config.js` 中 `/api` 已代理到本地网关 `http://127.0.0.1:8888/`。
   - 前端通过 `http://127.0.0.1:8899/api/**` 访问后端时，会由 Vite 转发到本地 gateway。

## 6. 可选但建议准备的组件

- Docker Desktop
  - 方便启动 RabbitMQ、PostgreSQL、XXL-JOB 等基础设施。

- PostgreSQL
  - 项目依赖包含 PostgreSQL driver。
  - 是否必需取决于 Nacos 动态数据源配置。

- Sentinel Dashboard
  - 网关配置中出现 `127.0.0.1:8718`。
  - 通常不阻塞基础启动，但控制台不可用。

- XXL-JOB Admin
  - `engine` / `etl` 有大量 XXL-JOB handler。
  - 是否阻塞启动取决于 Nacos 中 `xxl.job.*` 配置和运行策略。

## 7. 推荐启动准备顺序

### Step 1：修正 Java 环境

安装 JDK 8，并确认：

```bash
java -version
javac -version
mvn -version
```

期望 Maven 输出中的 Java version 也是 1.8。

### Step 2：配置 Maven 私服

补齐 `~/.m2/settings.xml`，确保能访问公司 Nexus。

需要重点确认：

- releases 仓库可访问。
- snapshots 仓库可访问。
- public 仓库可访问。
- 私有依赖 `com.kingdee:LTPATokenManager:1.0.0` 可下载。

### Step 3：安装内部 Maven 模块

建议顺序：

```bash
cd /Users/peyton/financehub-dev/code/financehub-parent
mvn install

cd /Users/peyton/financehub-dev/code/financehub-common
mvn install -DskipTests

cd /Users/peyton/financehub-dev/code/financehub-admin
mvn install -DskipTests

cd /Users/peyton/financehub-dev/code/financehub-engine/financehub-engine-api
mvn install -DskipTests

cd /Users/peyton/financehub-dev/code/financehub-etl/financehub-etl-api
mvn install -DskipTests
```

然后再分别构建服务：

```bash
cd /Users/peyton/financehub-dev/code/financehub-gateway
mvn package -DskipTests

cd /Users/peyton/financehub-dev/code/financehub-auth
mvn package -DskipTests

cd /Users/peyton/financehub-dev/code/financehub-admin
mvn package -DskipTests

cd /Users/peyton/financehub-dev/code/financehub-engine
mvn package -DskipTests

cd /Users/peyton/financehub-dev/code/financehub-etl
mvn package -DskipTests
```

### Step 4：确认 Nacos 配置

后端启动需要确认本地 Nacos 中至少存在：

- namespace：`financehub-local`
- group：`financehub-local`
- shared config：`financehub-dev.yml`

配置中需要覆盖：

- `spring.datasource.dynamic.*`
- `spring.redis.*`
- `spring.rabbitmq.*`
- `spring.cloud.gateway.routes`
- `xxl.job.*`
- 第三方服务地址，例如 `third.service.*`

### Step 5：确认数据库

需要确认 MySQL 中存在应用需要的库、表和基础数据。

源码中发现 SQL 主要在：

- `code/financehub-engine/sql`
- `code/financehub-web/insert_statements.sql`

这些 SQL 看起来包含大量增量脚本，不一定是完整初始化脚本。需要先确认当前本地 MySQL 是否已经有可用数据。

### Step 6：准备 RabbitMQ

如果 Nacos 配置启用了 RabbitMQ，本地需要启动 RabbitMQ。

需要确认：

- host
- port
- username
- password
- virtual host
- exchange / queue 是否由代码自动声明，或需要预建

### Step 7：启动后端服务

当前优先顺序：

1. `financehub-auth`
2. `financehub-admin-service`
3. `financehub-gateway`

暂不启动：

- `financehub-engine-service`
- `financehub-etl-service`

原因：

- gateway 依赖其他服务注册到 Nacos 后进行路由。
- auth / admin 是基础认证和管理能力。
- engine / etl 依赖 RabbitMQ、XXL-JOB、EAS 等更多外部组件，先跳过可以更快稳定基础链路。

示例启动参数应覆盖本地 Nacos：

```bash
--spring.profiles.active=dev
--spring.cloud.nacos.config.server-addr=127.0.0.1:8848
--spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
--spring.cloud.nacos.config.namespace=financehub-local
--spring.cloud.nacos.discovery.namespace=financehub-local
--spring.cloud.nacos.config.group=financehub-local
--spring.cloud.nacos.discovery.group=financehub-local
--spring.cloud.nacos.config.shared-configs[0].data-id=financehub-dev.yml
--spring.cloud.nacos.config.shared-configs[0].group=financehub-local
--spring.cloud.nacos.config.shared-configs[0].refresh=true
```

### Step 8：启动前端

已安装 Node/npm。手动启动时先加载 nvm：

```bash
export NVM_DIR="$HOME/.nvm"
. /opt/homebrew/opt/nvm/nvm.sh
cd /Users/peyton/financehub-dev/code/financehub-web
nvm use
npm run serve
```

默认端口：

```text
8899
```

如果要连本地后端，需要确认 `/api` 代理到本地网关。

## 8. 验证点

后端基础验证：

```bash
curl http://127.0.0.1:8848/nacos/
curl http://127.0.0.1:8888/doc.html
curl http://127.0.0.1:8201/doc.html
curl http://127.0.0.1:8202/doc.html
```

端口检查：

```bash
lsof -nP -iTCP:8200 -sTCP:LISTEN
lsof -nP -iTCP:8201 -sTCP:LISTEN
lsof -nP -iTCP:8202 -sTCP:LISTEN
lsof -nP -iTCP:8203 -sTCP:LISTEN
lsof -nP -iTCP:8888 -sTCP:LISTEN
lsof -nP -iTCP:8899 -sTCP:LISTEN
```

Nacos 服务注册验证：

- 登录 Nacos 控制台。
- 查看 `financehub-local` namespace。
- 先确认 `auth`、`admin`、`gateway` 服务实例已注册。
- 当前不要求 `engine`、`etl` 注册。

前端验证：

- 打开 `http://127.0.0.1:8899`。
- 登录或访问首页。
- 检查浏览器 Network 中 `/api` 请求是否打到本地 `8888`。
- 当前已用 `POST http://127.0.0.1:8899/api/auth/sso/login` 验证，返回 `code=200`，gateway 日志确认请求落到 `127.0.0.1:8888`。

## 9. 主要风险点

1. JDK 版本风险
   - 当前 Maven 使用 JDK 26，和项目 Java 8 不匹配。
   - 可能出现编译、反射、JDK 模块或依赖兼容问题。

2. Maven 私服风险
   - 没有公司 settings 时，内部依赖和私有 jar 无法拉取。
   - Maven 3.9 对 HTTP 仓库可能有额外限制。

3. Nacos 配置风险
   - 源码本身没有完整本地配置。
   - 缺少 Nacos 配置会导致数据库、Redis、RabbitMQ、XXL-JOB 等属性缺失。

4. 数据库初始化风险
   - SQL 多为增量脚本，不确定是否包含完整初始化。
   - 如果本地 MySQL 没有现成库表和基础数据，服务可能能启动但业务不可用。

5. RabbitMQ 风险
   - 当前未安装/未运行。
   - `engine` / `etl` 监听队列较多，RabbitMQ 配置不完整可能导致启动或业务异常。

6. 前端代理风险
   - 当前前端代理已指向本地网关。
   - 因当前不启动 `engine/etl`，访问依赖这些服务的页面会出现对应服务不可用。

7. 缺失服务风险
   - 之前部署目录中存在 `financehub-moji-service.jar`。
   - 当前 `code/` 目录中没有看到对应 `financehub-moji` 源码。
   - 如果网关或前端依赖 `/moji/**` 路由，完整功能会缺失。

## 10. 可行性判断

按步骤补齐环境后，本地启动是可行的。

最低可行路径：

1. 安装并切换到 JDK 8。
2. Node 14/16 已完成，当前使用 Node `v16.20.2`。
3. 配好 Maven 公司私服。
4. 解决 `LTPATokenManager` 私有依赖。
5. 按顺序 install 内部 Maven 模块。
6. 确认 Nacos `financehub-local` 配置完整。
7. 先不启动 `engine/etl`，优先跑基础栈。
8. 启动 Nacos、MySQL、Redis、`auth/admin/gateway/web`，验证登录链路和前端访问。
9. 后续需要完整业务时，再启动 RabbitMQ。
10. 再启动 `engine/etl`，逐个处理外部依赖问题。

如果只想先跑基础页面和部分接口，可以先不启动 `engine/etl`，优先跑：

- Nacos
- MySQL
- Redis
- `financehub-auth`
- `financehub-admin-service`
- `financehub-gateway`
- `financehub-web`

这样能更快验证本地基础链路，再逐步补齐复杂业务组件。
