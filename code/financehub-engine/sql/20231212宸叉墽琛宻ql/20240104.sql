--科目辅助帐余额表
CREATE TABLE financialdb.eg_account_assist_balance (
	id int8 NOT NULL, -- ID
	period_code int4 NULL, -- 会计期间
	contract_code varchar(64) NULL, -- 合同编号
	client_code varchar(64) NULL, -- 客户编号
	business_code varchar(20) NULL, -- 业务类型
	org_id varchar(50) NULL, -- 签约主体
	bill_contract_code varchar(64) NULL, -- 借款合同编号
	currency_code varchar(32) NULL, -- 币种
	account_code varchar(32) NULL, -- 科目编码
	account_name varchar(64) NULL, -- 科目名称
	year_begin_debit_balance numeric(20, 2) NULL, -- 年初借方余额
	year_begin_credit_balance numeric(20, 2) NULL, -- 年初贷方余额
	month_begin_debit_balance numeric(20, 2) NULL, -- 期初借方余额
	month_begin_credit_balance numeric(20, 2) NULL, -- 期初贷方余额
	month_debit_amount numeric(20, 2) NULL, -- 本期借方发生额
	month_credit_amount numeric(20, 2) NULL, -- 本期贷方发生额
	year_debit_amount numeric(20, 2) NULL, -- 年累计借方发生额
	year_credit_amount numeric(20, 2) NULL, -- 年累计贷方发生额
	month_end_debit_balance numeric(20, 2) NULL, -- 期末借方余额
	month_end_credit_balance numeric(20, 2) NULL, -- 期末贷方余额
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 删除标识(0:未删除,1:已删除)
	CONSTRAINT eg_account_assist_balance_pkey PRIMARY KEY (id)
);
CREATE INDEX eg_account_assist_balance_account_code_idx ON financialdb.eg_account_assist_balance USING btree (account_code);
CREATE INDEX eg_account_assist_balance_client_code_idx ON financialdb.eg_account_assist_balance USING btree (client_code);
CREATE INDEX eg_account_assist_balance_contract_code_idx ON financialdb.eg_account_assist_balance USING btree (contract_code);
CREATE INDEX eg_account_assist_balance_period_code_idx ON financialdb.eg_account_assist_balance USING btree (period_code);
COMMENT ON TABLE financialdb.eg_account_assist_balance IS '科目辅助帐余额表';

-- Column comments

COMMENT ON COLUMN financialdb.eg_account_assist_balance.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.period_code IS '会计期间';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.client_code IS '客户编号';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.business_code IS '业务类型';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.bill_contract_code IS '借款合同编号';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.currency_code IS '币种';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.account_code IS '科目编码';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.account_name IS '科目名称';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.year_begin_debit_balance IS '年初借方余额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.year_begin_credit_balance IS '年初贷方余额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.month_begin_debit_balance IS '期初借方余额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.month_begin_credit_balance IS '期初贷方余额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.month_debit_amount IS '本期借方发生额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.month_credit_amount IS '本期贷方发生额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.year_debit_amount IS '年累计借方发生额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.year_credit_amount IS '年累计贷方发生额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.month_end_debit_balance IS '期末借方余额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.month_end_credit_balance IS '期末贷方余额';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_account_assist_balance.del_flag IS '删除标识(0:未删除,1:已删除)';