CREATE TABLE financialdb.eg_non_confirm_collection_account_checking (
	id int8 NOT NULL,
	create_by varchar(64) NULL,
	create_time timestamp NULL,
	update_by varchar(64) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT 0,
	account_checking_month varchar(10) NULL,
	collection_accounts_bank text NULL,
	system_code varchar(50) NULL,
	business_ebank_number text NULL,
	ebank_serial_number text NULL,
	business_happen_date timestamp NULL,
	account_age_class varchar(50) NULL,
	month_init_balance numeric(20, 2) NULL,
	cur_month_credit_amount numeric(20, 2) NULL,
	cur_month_balance numeric(20, 2) NULL,
	system_amount numeric(20, 2) NULL,
	diff_amount numeric(20, 2) NULL,
	account_checking_comments varchar(200) NULL,
	financial_primary_classic varchar(200) NULL,
	confirm_account_property varchar(200) NULL,
	is_confirmed varchar(10) NULL
);
COMMENT ON TABLE financialdb.eg_non_confirm_collection_account_checking IS '未确认收款对账';

-- Column comments

COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.del_flag IS '是否删除（0:否，1：是）';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.account_checking_month IS '对账月份';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.collection_accounts_bank IS '到账主体';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.system_code IS '系统编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.business_ebank_number IS '业务系统的网银编号';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.ebank_serial_number IS '业务系统网银编号/批次号(业务系统网银编号或批扣批次（扣款渠道批次号，对应恒运VC_PINGZZY 凭证摘要显示）)';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.business_happen_date IS '入账日期';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.account_age_class IS '账龄分类';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.month_init_balance IS '月初余额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.cur_month_credit_amount IS '本月贷方发生额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.cur_month_balance IS '本月余额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.system_amount IS '系统金额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.diff_amount IS '差额';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.account_checking_comments IS '财务对账备注';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.financial_primary_classic IS '财务初分类';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.confirm_account_property IS '运营部确认款项性质';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.is_confirmed IS '是否已经确认';

ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ALTER COLUMN collection_accounts_bank TYPE varchar(200) USING collection_accounts_bank::varchar;
ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ALTER COLUMN business_ebank_number TYPE varchar(200) USING business_ebank_number::varchar;
ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ALTER COLUMN ebank_serial_number TYPE varchar(200) USING ebank_serial_number::varchar;