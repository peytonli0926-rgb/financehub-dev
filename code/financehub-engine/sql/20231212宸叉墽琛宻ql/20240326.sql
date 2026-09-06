ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD system_code varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.system_code IS '系统编码';
