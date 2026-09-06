CREATE TABLE financialdb.eg_non_confirm_amount_for_business (
	id int8 NOT NULL,
	create_by varchar(64) NULL,
	create_time timestamp NULL,
	update_by varchar(64) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT 0,
	account_checking_month varchar(10) NULL,
	system_code varchar(50) NULL,
	ebank_serial_number varchar(200) NULL,
	system_amount numeric(20, 2) NULL
);

-- Column comments

COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.del_flag IS '是否删除（0:否，1：是）';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.account_checking_month IS '对账月份';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.system_code IS '系统编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.ebank_serial_number IS '业务系统批扣流水号';
COMMENT ON COLUMN financialdb.eg_non_confirm_amount_for_business.system_amount IS '系统金额';

ALTER TABLE financialdb.eg_lease_income_details ADD history_overdue varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.history_overdue IS '历史逾期';

ALTER TABLE financialdb.eg_lease_income_details ADD paid_interest numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.paid_interest IS '实收利息';
ALTER TABLE financialdb.eg_lease_income_details ADD confirmed_income numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.confirmed_income IS '已确认收益';

--prod 已执行