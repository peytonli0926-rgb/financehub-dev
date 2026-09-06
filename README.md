# FinanceHub Dev

华夏金租会计引擎本地开发工程，包含网关、认证、管理服务、会计引擎、ETL、公共模块及前端。

## 目录

- `code/financehub-gateway`：网关
- `code/financehub-auth`：认证服务
- `code/financehub-admin`：管理服务
- `code/financehub-engine`：会计引擎
- `code/financehub-etl`：ETL 服务
- `code/financehub-common`：公共模块
- `code/financehub-parent`：Maven 父工程
- `code/financehub-web`：Vue 前端

## 本地启动

Windows 环境执行：

```bat
start-financehub.cmd
```

启动脚本会检查并启动 MySQL、Redis、Nacos、网关、认证服务、管理服务、会计引擎及前端。脚本中的运行时路径和本地数据库参数应按本机环境调整。

默认访问地址：`http://localhost:8899/`

