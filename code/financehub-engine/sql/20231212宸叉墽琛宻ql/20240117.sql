--手工凭证新增字段
ALTER TABLE financialdb.eg_manual_voucher ADD loans_contract_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.loans_contract_code IS '借款合同编号';
ALTER TABLE financialdb.eg_manual_voucher ADD cost_centre varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.cost_centre IS '成本中心';
ALTER TABLE financialdb.eg_manual_voucher ADD employee_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.employee_name IS '员工姓名';
ALTER TABLE financialdb.eg_manual_voucher ADD expense_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.expense_type IS '费用类型';
ALTER TABLE financialdb.eg_manual_voucher ADD financial_institution varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.financial_institution IS '金融机构';
ALTER TABLE financialdb.eg_manual_voucher ADD batch_num varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.batch_num IS '批次号';
ALTER TABLE financialdb.eg_manual_voucher ADD bank_no varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.bank_no IS '银行账号';
ALTER TABLE financialdb.eg_manual_voucher ADD net_bank_no varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.net_bank_no IS '网银编号';
ALTER TABLE financialdb.eg_manual_voucher ADD preparer_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.preparer_name IS '制单人姓名';