--凭证分录新增字段
ALTER TABLE financialdb.eg_voucher_entry ADD employee_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.employee_code IS '职员编码';

ALTER TABLE financialdb.eg_voucher_entry ADD expense_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.expense_type IS '费用类型';

ALTER TABLE financialdb.eg_voucher_entry ADD financial_institution varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.financial_institution IS '金融机构';

ALTER TABLE financialdb.eg_voucher_entry ADD cost_centre varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.cost_centre IS '成本中心';

ALTER TABLE financialdb.eg_voucher_entry ADD receipt_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.receipt_number IS '借据号';
ALTER TABLE financialdb.eg_voucher_entry ADD derivative_contract_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.derivative_contract_number IS '衍生合约编号';
ALTER TABLE financialdb.eg_voucher_entry ADD batch_number varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_entry.batch_number IS '批次号';

--eas记录表新增字段
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD system_code varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.system_code IS '数据来源：eas1:EAS1,金蝶中间库：KINGDEE_MIDDLE,中台：FINHUB';

ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD client_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.client_code IS '客户编号';
ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD client_name varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.client_name IS '客户名称';
