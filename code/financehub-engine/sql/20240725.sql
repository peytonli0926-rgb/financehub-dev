CREATE TABLE eg_charge_off_summary_report (
	id int8 NOT NULL, -- ID
	period_code int4 NULL, -- 会计期间
	contract_code varchar(100) NULL, -- 合同编号
	org_id varchar(100) NULL, -- 签约主体
	verification_status varchar(100) NULL, -- 核销状态
	client_code varchar(100) NULL, -- 客户编码
	client_name varchar(100) NULL, -- 客户名称
	verification_date timestamp NULL, -- 核销时间
	financial_expense_amount numeric(20, 2) NULL, -- 财务核销敞口
	provision_reversal_amount numeric(20, 2) NULL, -- 拨备转回金额
	provision_reversal_year varchar(50) NULL, -- 拨备转回年份
	tax_verification_date timestamp NULL, -- 税务核销日期
	tax_verification_amount numeric(20, 2) NULL, -- 税务核销金额
	bad_debt_write_off_balance numeric(20, 2) NULL, -- 坏账核销余额
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
	create_by varchar(32) NULL, -- 创建人
	create_time timestamp NULL, -- 创建时间
	update_by varchar(32) NULL, -- 更新人
	update_time timestamp NULL, -- 更新时间
	CONSTRAINT eg_charge_off_summary_reportt_pk PRIMARY KEY (id)
);
COMMENT ON TABLE eg_charge_off_summary_report IS 'Charge Off汇总报表';

-- Column comments

COMMENT ON COLUMN eg_charge_off_summary_report.id IS 'ID';
COMMENT ON COLUMN eg_charge_off_summary_report.contract_code IS '合同编号';
COMMENT ON COLUMN eg_charge_off_summary_report.org_id IS '签约主体';
COMMENT ON COLUMN eg_charge_off_summary_report.verification_status IS '核销状态';
COMMENT ON COLUMN eg_charge_off_summary_report.client_code IS '客户编码';
COMMENT ON COLUMN eg_charge_off_summary_report.client_name IS '客户名称';
COMMENT ON COLUMN eg_charge_off_summary_report.verification_date IS '核销时间';
COMMENT ON COLUMN eg_charge_off_summary_report.financial_expense_amount IS '财务核销敞口';
COMMENT ON COLUMN eg_charge_off_summary_report.provision_reversal_amount IS '拨备转回金额';
COMMENT ON COLUMN eg_charge_off_summary_report.provision_reversal_year IS '拨备转回年份';
COMMENT ON COLUMN eg_charge_off_summary_report.tax_verification_date IS '税务核销日期';
COMMENT ON COLUMN eg_charge_off_summary_report.tax_verification_amount IS '税务核销金额';
COMMENT ON COLUMN eg_charge_off_summary_report.bad_debt_write_off_balance IS '坏账核销余额';
COMMENT ON COLUMN eg_charge_off_summary_report.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN eg_charge_off_summary_report.create_by IS '创建人';
COMMENT ON COLUMN eg_charge_off_summary_report.create_time IS '创建时间';
COMMENT ON COLUMN eg_charge_off_summary_report.update_by IS '更新人';
COMMENT ON COLUMN eg_charge_off_summary_report.update_time IS '更新时间';
COMMENT ON COLUMN eg_charge_off_summary_report.period_code IS '会计期间';

CREATE INDEX eg_charge_off_summary_report_period_code_idx ON financialdb4.eg_charge_off_summary_report (period_code);
CREATE INDEX eg_charge_off_summary_report_contract_code_idx ON financialdb4.eg_charge_off_summary_report (contract_code);
CREATE INDEX eg_charge_off_summary_report_org_id_idx ON financialdb4.eg_charge_off_summary_report (org_id);
