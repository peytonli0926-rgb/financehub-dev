# 尾差调整测试用例

## 前置条件

1. 执行 `sql/20260909_reenable_tail_difference_adjustment.sql`（首次恢复表结构时）。
2. 执行 `sql/20260911_tail_difference_latest_balance_config.sql`。
3. 启动网关、认证、管理后台、财务引擎和前端服务。
4. 集成测试前执行 `sql/test/20260911_tail_difference_fixture.sql`。

测试业务日期统一使用 `2026-09-11`，签约主体使用 `HXZL001`，科目使用
`15320201 / 未实现融资收益_利息（动产项目）_回租`，默认尾差范围为 `[-1, 1]`。

## 用例

| 编号 | 场景 | 操作/数据 | 预期结果 |
|---|---|---|---|
| TC01 | 初始化正尾差 | 最新余额 `WCTZ-TEST-POS-001 = 0.01`，执行初始化 | 生成一条明细，余额为 `0.01`，归入 `HXZL001 + 15320201 + 2026-09-11` 的已录入单据 |
| TC02 | 初始化负尾差 | 最新余额 `WCTZ-TEST-NEG-001 = -0.01`，执行初始化 | 生成一条明细，余额为 `-0.01`，与 TC01 归入同一单据 |
| TC03 | 正尾差凭证 | 对初始化单据执行“生成凭证” | 借：`unearned_lease_interest` 0.01；贷：`lease_interest_income` 0.01；借贷平衡，签约主体为 `HXZL001` |
| TC04 | 负尾差凭证 | 同上，检查负尾差合同凭证 | 借：`lease_interest_income` 0.01；贷：`unearned_lease_interest` 0.01；金额均为正数 |
| TC05 | 零余额过滤 | 把测试余额改为 `0` 后重新初始化 | 不生成对应明细 |
| TC06 | 超范围过滤 | 把测试余额改为 `1.01` 或 `-1.01` 后重新初始化 | 默认范围下不生成对应明细 |
| TC07 | 指定范围 | 余额为 `1.01`，初始化参数传 `minAccountBalance=-2,maxAccountBalance=2` | 生成对应明细 |
| TC08 | 重复初始化 | 在单据状态为“已录入”时重复初始化 | 原明细被刷新，不重复生成；单据主键保持不变 |
| TC09 | 多凭证重新生成 | 同一明细关联逗号分隔的多个凭证 ID，再执行生成凭证 | 原凭证均被正确删除并重新生成，不出现 ID 转换异常 |
| TC10 | 无效余额映射 | 字典加入一个其 `fund_type_balance` 不存在于最新余额实体的科目 | 任务状态为“失败”，错误信息明确指出缺失的余额字段 |
| TC11 | 提交与撤回 | 生成凭证后提交，再执行撤回 | 提交后单据/凭证进入已提交；撤回后均恢复已录入 |

## 接口检查顺序

1. `POST /engine/finance/tail-difference-adjustment/initData`
   请求体：`{"businessDate":"2026-09-11","orgIdList":["HXZL001"],"accountCodeList":["15320201"]}`。
2. 轮询 `POST /engine/finance/tail-difference-adjustment/page`，确认单据及两条测试明细。
3. `POST /engine/finance/tail-difference-adjustment/generateVoucher`，请求体为单据 ID 数组。
4. 检查两张凭证的签约主体、科目、方向、金额和借贷平衡。
5. 测试完成后，先调用 `POST /engine/finance/tail-difference-adjustment/deleteByIds` 删除测试单据及凭证，再执行清理 SQL。

## 数据库核对 SQL

```sql
SELECT h.id, h.org_id, h.account_code, h.business_date, h.process_status,
       d.contract_code, d.account_balance, d.voucher_ids, d.error_info
FROM eg_tail_difference_adjustment h
JOIN eg_tail_difference_adjustment_detail d ON d.tail_difference_adjustment_id = h.id
WHERE d.contract_code IN ('WCTZ-TEST-POS-001', 'WCTZ-TEST-NEG-001')
  AND h.del_flag = '0' AND d.del_flag = '0'
ORDER BY d.contract_code;
```

## 本地回归结果（2026-09-11）

- TC01、TC02：通过，初始化任务成功，正负两条明细均准确取自 `eg_contract_balance_latest`。
- TC03、TC04：通过，两张凭证签约主体均为 `HXZL001`，每张借方/贷方合计均为 `0.01`。
- 重复生成检查：通过，旧凭证被删除并生成新凭证，明细凭证 ID 正常刷新。
- 清理检查：通过，测试合同、最新余额、尾差明细、凭证和测试任务均无残留。
