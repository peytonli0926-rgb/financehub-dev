ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD wirte_off_voucher_exception text NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.wirte_off_voucher_exception IS '核销回款凭证生成异常信息';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ALTER COLUMN write_off_voucher_id TYPE varchar(1000) USING write_off_voucher_id::varchar;
ALTER TABLE financialdb.eg_business_claim_repayment_record ADD process_status varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.process_status IS '处理状态';

ALTER TABLE financialdb.eg_business_claim_repayment_record ADD remark text NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.remark IS '备注';
