ALTER TABLE financialdb.eg_contract ADD paid_handling_fees numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_contract.paid_handling_fees IS '实收手续费(含税)';
ALTER TABLE financialdb.eg_contract ADD other_income numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_contract.other_income IS '其它收入(含税)';

ALTER TABLE financialdb.eg_lease_income_details ADD paid_handling_fees numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.paid_handling_fees IS '实收手续费(含税)';
ALTER TABLE financialdb.eg_lease_income_details ADD other_income numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.other_income IS '其它收入(含税)';
ALTER TABLE financialdb.eg_lease_income_details ADD financial_contract_status varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_lease_income_details.financial_contract_status IS '财务合同状态';

ALTER TABLE financialdb.eg_kingdee_hyb_voucher ADD CONSTRAINT eg_kingdee_hyb_voucher_pk PRIMARY KEY (id);
--sql已执行，uat prod