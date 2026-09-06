--手工凭证新增字段
ALTER TABLE financialdb.eg_manual_voucher ADD material_contract_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.material_contract_code IS '物料（合同号）';
ALTER TABLE financialdb.eg_manual_voucher ADD receipt_num varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.receipt_num IS '借据号';
ALTER TABLE financialdb.eg_manual_voucher ADD derivative_contract_code varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.derivative_contract_code IS '衍生合约编号';
ALTER TABLE financialdb.eg_manual_voucher ADD project_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual_voucher.project_type IS '项目类型（开发项目）';

ALTER TABLE financialdb.eg_manual_voucher DROP COLUMN net_bank_no;
ALTER TABLE financialdb.eg_manual DROP COLUMN attachment_num;

ALTER TABLE financialdb.eg_manual ADD is_write_off bpchar(1) NULL DEFAULT 0;
COMMENT ON COLUMN financialdb.eg_manual.is_write_off IS '是否冲销（0：否，1：是）';