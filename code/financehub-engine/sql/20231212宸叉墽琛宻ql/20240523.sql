CREATE TABLE financialdb.eg_repayment_plan_change_records (
	id int8 NOT NULL,
	change_time timestamp NULL,
	contract_code varchar(50) NULL,
	system_code varchar(50) NULL,
	org_id varchar(100) NULL,
	create_by varchar(64) NULL,
	create_time timestamp NULL,
	update_by varchar(64) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT 0
);
COMMENT ON TABLE financialdb.eg_repayment_plan_change_records IS '偿还计划变更记录';

-- Column comments

COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.change_time IS '变更时间';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.contract_code IS '合同编码';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.system_code IS '系统编码';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.del_flag IS '是否删除（0:否，1：是）';
ALTER TABLE financialdb.eg_repayment_plan_change_records ADD lease_start_or_change varchar(1) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_change_records.lease_start_or_change IS '0:起租|1:偿还计划变更';


ALTER TABLE financialdb.eg_contract_balance ADD margin_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.margin_transition_balance IS '保证金过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance ADD margin_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.margin_transition_amount IS '保证金过渡科目发生额';
ALTER TABLE financialdb.eg_contract_balance_latest ADD margin_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.margin_transition_balance IS '保证金过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance_latest ADD margin_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.margin_transition_amount IS '保证金过渡科目发生额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD margin_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.margin_transition_balance IS '保证金过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD margin_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.margin_transition_amount IS '保证金过渡科目发生额';
ALTER TABLE financialdb.eg_contract_balance_month ADD margin_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_month.margin_transition_balance IS '保证金过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance_month ADD margin_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_month.margin_transition_amount IS '保证金过渡科目发生额';

ALTER TABLE financialdb.eg_contract_balance_latest ADD depreciation_reserves_entrusted_loans_interest_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.depreciation_reserves_entrusted_loans_interest_balance IS '委托贷款应收利息减值准备余额';
ALTER TABLE financialdb.eg_contract_balance_latest ADD depreciation_reserves_entrusted_loans_interest_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.depreciation_reserves_entrusted_loans_interest_amount IS '委托贷款应收利息减值准备发生额';
ALTER TABLE financialdb.eg_contract_balance_latest ADD bank_deposits_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.bank_deposits_transition_balance IS '银行存款过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance_latest ADD bank_deposits_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.bank_deposits_transition_amount IS '银行存款过渡科目发生额';
ALTER TABLE financialdb.eg_contract_balance ADD depreciation_reserves_entrusted_loans_interest_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.depreciation_reserves_entrusted_loans_interest_balance IS '委托贷款应收利息减值准备余额';
ALTER TABLE financialdb.eg_contract_balance ADD depreciation_reserves_entrusted_loans_interest_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.depreciation_reserves_entrusted_loans_interest_amount IS '委托贷款应收利息减值准备发生额';
ALTER TABLE financialdb.eg_contract_balance ADD bank_deposits_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.bank_deposits_transition_balance IS '银行存款过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance ADD bank_deposits_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance.bank_deposits_transition_amount IS '银行存款过渡科目发生额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD depreciation_reserves_entrusted_loans_interest_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_entrusted_loans_interest_balance IS '委托贷款应收利息减值准备余额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD depreciation_reserves_entrusted_loans_interest_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.depreciation_reserves_entrusted_loans_interest_amount IS '委托贷款应收利息减值准备发生额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD bank_deposits_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.bank_deposits_transition_balance IS '银行存款过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance_temp ADD bank_deposits_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.bank_deposits_transition_amount IS '银行存款过渡科目发生额';
ALTER TABLE financialdb.eg_contract_balance_month ADD depreciation_reserves_entrusted_loans_interest_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_month.depreciation_reserves_entrusted_loans_interest_balance IS '委托贷款应收利息减值准备余额';
ALTER TABLE financialdb.eg_contract_balance_month ADD depreciation_reserves_entrusted_loans_interest_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_month.depreciation_reserves_entrusted_loans_interest_amount IS '委托贷款应收利息减值准备发生额';
ALTER TABLE financialdb.eg_contract_balance_month ADD bank_deposits_transition_balance numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_month.bank_deposits_transition_balance IS '银行存款过渡科目余额';
ALTER TABLE financialdb.eg_contract_balance_month ADD bank_deposits_transition_amount numeric(20, 2) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract_balance_month.bank_deposits_transition_amount IS '银行存款过渡科目发生额';


ALTER TABLE financialdb.eg_repayment_plan ADD adjustment_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan.adjustment_amount IS '租赁收入调整额';
ALTER TABLE financialdb.eg_repayment_plan ADD change_after_ending_amortized_cost numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan.change_after_ending_amortized_cost IS '变更后期末摊余成本';
ALTER TABLE financialdb.eg_repayment_plan ADD change_after_rental_income numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan.change_after_rental_income IS '变更后租赁收入';

ALTER TABLE financialdb.eg_repayment_plan_his ADD adjustment_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_his.adjustment_amount IS '租赁收入调整额';
ALTER TABLE financialdb.eg_repayment_plan_his ADD change_after_ending_amortized_cost numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_his.change_after_ending_amortized_cost IS '变更后期末摊余成本';
ALTER TABLE financialdb.eg_repayment_plan_his ADD change_after_rental_income numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_repayment_plan_his.change_after_rental_income IS '变更后租赁收入';
