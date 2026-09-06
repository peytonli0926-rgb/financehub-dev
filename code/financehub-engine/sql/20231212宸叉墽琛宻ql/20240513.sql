--手工凭证新增是否涉及其他客户及辅助帐字段
ALTER TABLE financialdb.eg_manual_voucher ADD is_related_other_customer bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_manual_voucher.is_related_other_customer IS '是否涉及其他客户及辅助帐（0：否，1：是）';

ALTER TABLE financialdb.eg_voucher_entry ADD is_related_other_customer bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_voucher_entry.is_related_other_customer IS '是否涉及其他客户及辅助帐（0：否，1：是）';


ALTER TABLE financialdb.eg_margin_contract_balance ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_margin_contract_balance.process_instance_id IS '流程实例id';


ALTER TABLE financialdb.eg_contract_balance ADD payable_account_transition_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance.payable_account_transition_balance IS '应付款项_财务中台过渡余额';

ALTER TABLE financialdb.eg_contract_balance ADD payable_account_transition_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance.payable_account_transition_amount IS '应付款项_财务中台过渡发生额';

ALTER TABLE financialdb.eg_contract_balance_latest ADD payable_account_transition_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.payable_account_transition_balance IS '应付款项_财务中台过渡余额';

ALTER TABLE financialdb.eg_contract_balance_latest ADD payable_account_transition_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_latest.payable_account_transition_amount IS '应付款项_财务中台过渡发生额';

ALTER TABLE financialdb.eg_contract_balance_temp ADD payable_account_transition_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_account_transition_balance IS '应付款项_财务中台过渡余额';

ALTER TABLE financialdb.eg_contract_balance_temp ADD payable_account_transition_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance_temp.payable_account_transition_amount IS '应付款项_财务中台过渡发生额';