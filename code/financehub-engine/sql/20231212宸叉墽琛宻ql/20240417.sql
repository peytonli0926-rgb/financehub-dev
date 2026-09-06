ALTER TABLE financialdb.eg_voucher_to_eas_record ADD batch_uuid varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.batch_uuid IS '批次';
ALTER TABLE financialdb.eg_voucher_to_eas_result ADD batch_uuid varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_result.batch_uuid IS '批次';
COMMENT ON COLUMN financialdb.eg_send_eas2_result.result_key IS '索引号';
COMMENT ON TABLE financialdb.eg_send_eas2_result IS '记录发送EAS2数据成功表';