ALTER TABLE financialdb.financialdb.eg_fund_system_balance  ADD payable_account_transition_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance.payable_account_transition_balance IS '应付款项_财务中台过渡余额';

ALTER TABLE financialdb.financialdb.eg_fund_system_balance ADD payable_account_transition_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract_balance.payable_account_transition_amount IS '应付款项_财务中台过渡发生额';
