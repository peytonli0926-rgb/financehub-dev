CREATE TABLE financialdb4.eg_batch_modify_template (
	id int8 NOT NULL,
	account_checking_month varchar(10) NULL,
	collection_accounts_bank varchar(200) NULL,
	system_code varchar(50) NULL,
	ebank_serial_number text NULL,
	remark varchar(1000) NULL,
	financial_primary_classic varchar(200) NULL DEFAULT NULL::character varying,
	confirm_account_property varchar(200) NULL DEFAULT NULL::character varying,
	non_lease_account_checking_comments varchar(1000) NULL,
	operate_history_comments varchar(2000) NULL
);
COMMENT ON TABLE financialdb4.eg_batch_modify_template IS '未确认收款-对账表批量修改上传模板表';

-- Column comments

COMMENT ON COLUMN financialdb4.eg_batch_modify_template.id IS 'ID';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.account_checking_month IS '对账月份';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.collection_accounts_bank IS '到账主体';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.system_code IS '系统编码';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.ebank_serial_number IS '业务系统的网银编号-小网银';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.remark IS 'comments';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.financial_primary_classic IS '财务初分类';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.confirm_account_property IS '运营部确认款项性质';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.non_lease_account_checking_comments IS '非租对账备注';
COMMENT ON COLUMN financialdb4.eg_batch_modify_template.operate_history_comments IS '运营部历史备注';
