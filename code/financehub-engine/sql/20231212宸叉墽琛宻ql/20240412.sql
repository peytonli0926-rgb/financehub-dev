ALTER TABLE financialdb.eg_voucher_to_eas_record ADD import_key varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.import_key IS '全局标识';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD source_bill_id varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.source_bill_id IS '来源单ID';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD source_sys_no varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.source_sys_no IS '来源系统唯一号';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD source_sys_bill_url varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.source_sys_bill_url IS '来源系统单据详情url';
ALTER TABLE financialdb.eg_voucher_to_eas_record ADD is_check varchar(10) NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.is_check IS '是否复核';

ALTER TABLE financialdb.eg_voucher_to_eas_record RENAME COLUMN entry_dc TO entry_d_c;
