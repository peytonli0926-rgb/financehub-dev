ALTER TABLE financialdb.eg_contract ADD depreciation_reserves_amount_kp numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract.depreciation_reserves_amount_kp IS '核销合同开票时转回的拨备发生额';
ALTER TABLE financialdb.eg_contract ADD depreciation_reserves_amount_sk numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract.depreciation_reserves_amount_sk IS '核销合同收款时转回的拨备发生额';

ALTER TABLE financialdb.eg_contract ADD outtax_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract.outtax_amount IS '核销合同税金计提金额';

ALTER TABLE financialdb.eg_contract ADD lease_revenue_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_contract.lease_revenue_amount IS '核销合同收益计提金额';

ALTER TABLE financialdb.eg_impairment_provision_detail ADD origin_currency varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.origin_currency IS '原币币种';
ALTER TABLE financialdb.eg_impairment_provision_detail ADD target_currency varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.target_currency IS '目标币种';

ALTER TABLE financialdb.eg_contract ADD account_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_contract.account_date IS '记账日期';