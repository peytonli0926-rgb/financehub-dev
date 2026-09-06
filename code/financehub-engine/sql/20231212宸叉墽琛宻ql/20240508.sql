ALTER TABLE financialdb.eg_internal_transfer RENAME COLUMN bank_account TO bank_account_code;
COMMENT ON COLUMN financialdb.eg_internal_transfer.bank_account_code IS '银行账号编码';

ALTER TABLE financialdb.eg_parity_transfer ADD payment_date timestamp NULL;
COMMENT ON COLUMN financialdb.eg_parity_transfer.payment_date IS '支付日期';
ALTER TABLE financialdb.eg_parity_transfer ADD bank_account_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_parity_transfer.bank_account_code IS '银行账号编码';

ALTER TABLE financialdb.eg_contract ADD is_dzzc varchar(10) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_contract.is_dzzc IS '是否抵债资产，0否，1是';


ALTER TABLE financialdb.eg_contract ADD rent_contract_total numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_contract.rent_contract_total IS '租赁合同总计';
ALTER TABLE financialdb.eg_contract ADD last_cost numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_contract.last_cost IS '期末残值';
ALTER TABLE financialdb.eg_contract ADD lease_before_recevied_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_contract.lease_before_recevied_amount IS '起租前已收租金';
