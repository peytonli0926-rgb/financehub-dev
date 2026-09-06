--eas结果表记录元参数
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD meta_params varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.meta_params IS '元数据参数';