ALTER TABLE financialdb.eg_voucher_to_eas_record ADD data_status varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.data_status IS '发送数据状态类型：暂存，提交，审核，过账';
--核销新增会计期间
ALTER TABLE financialdb.eg_verification ADD period_code int4 NULL;
COMMENT ON COLUMN financialdb.eg_verification.period_code IS '会计期间';