--成本类服务费新增字段
--start 已执行
ALTER TABLE financialdb.eg_cost_channel_fee ADD no_tax_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.no_tax_amount IS '金额（不含税）';

ALTER TABLE financialdb.eg_cost_channel_fee ADD tax_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.tax_amount IS '税额';

ALTER TABLE financialdb.eg_cost_channel_fee ADD business_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_cost_channel_fee.business_date IS '业务日期';


--资金系统新增生成凭证标识
ALTER TABLE financialdb.eg_fund_ebank_transaction_data ADD is_generate_voucher bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_fund_ebank_transaction_data.is_generate_voucher IS '是否生成凭证（0：否，1：是）';

ALTER TABLE financialdb.eg_fund_payment_data ADD is_generate_voucher bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_fund_payment_data.is_generate_voucher IS '是否生成凭证(0:否，1：是)';
--end 已执行

--凭证行
ALTER TABLE financialdb.eg_voucher_entry ADD bill_contract_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.bill_contract_code IS '借款合同编号';
ALTER TABLE financialdb.eg_voucher_entry ADD assist_flags varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.assist_flags IS '凭证行维度';
--ALTER TABLE financialdb.eg_voucher_entry DROP COLUMN voucher_amount;
--ALTER TABLE financialdb.eg_voucher_entry DROP COLUMN client_flag;
--ALTER TABLE financialdb.eg_voucher_entry DROP COLUMN client_name;
--ALTER TABLE financialdb.eg_voucher_entry DROP COLUMN contract_flag;
--ALTER TABLE financialdb.eg_voucher_entry DROP COLUMN contract_name;
--凭证行配置
ALTER TABLE financialdb.eg_scene_voucher_entry ADD cash_attribute_flag char(1) NULL;
COMMENT ON COLUMN financialdb.eg_scene_voucher_entry.cash_attribute_flag IS '是否现金流属性相关(0:否 1:是)';
ALTER TABLE financialdb.eg_scene_voucher_entry ADD assist_flags varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_scene_voucher_entry.assist_flags IS '凭证行维度';
--ALTER TABLE financialdb.eg_scene_voucher_entry DROP COLUMN client_flag;
--ALTER TABLE financialdb.eg_scene_voucher_entry DROP COLUMN contract_flag;
--偿还计划新增字段
alter table eg_repayment_plan add plan_date_period int4;
comment on column eg_repayment_plan.plan_date_period is '计划还款日(yyyyMM)';

--创建索引
CREATE INDEX idx_repayment_plan_contract_code ON financialdb.eg_repayment_plan USING btree (contract_code);
CREATE INDEX idx_repayment_plan_plan_date_period ON financialdb.eg_repayment_plan USING btree (plan_date_period);
--已执行