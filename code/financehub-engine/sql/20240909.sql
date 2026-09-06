ALTER TABLE financialdb4.eg_report_finance_in_out ADD recycling_equipment_account_balance numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb4.eg_report_finance_in_out.recycling_equipment_account_balance IS '回收设备成本科目余额';


ALTER TABLE financialdb4.eg_voucher_entry ADD eas_voucher_number varchar(50) NULL;
COMMENT ON COLUMN financialdb4.eg_voucher_entry.eas_voucher_number IS '金蝶凭证号';
