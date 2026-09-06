ALTER TABLE financialdb.eg_business_claim_repayment_record ADD org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.org_id IS '组织机编码';

ALTER TABLE financialdb.eg_business_claim_repayment_record ADD org_name varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.org_name IS '签约主体名称';

ALTER TABLE financialdb.eg_business_claim_repayment_record ADD scene_code varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.scene_code IS '场景编码';
ALTER TABLE financialdb.eg_business_claim_repayment_record ADD scene_name varchar(200) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.scene_name IS '场景名称';

ALTER TABLE financialdb.eg_business_claim_repayment_record ADD batch_no int4 NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.batch_no IS '处理批次号';
ALTER TABLE financialdb.eg_business_claim_repayment_record ADD operation_type varchar(50) NULL;
COMMENT ON COLUMN financialdb.eg_business_claim_repayment_record.operation_type IS '操作类型';

ALTER TABLE financialdb.eg_voucher_to_eas_record ADD json_param text NULL;
COMMENT ON COLUMN financialdb.eg_voucher_to_eas_record.json_param IS '报文';

ALTER TABLE financialdb.eg_non_confirm_collection_sum ALTER COLUMN accounts_receivable SET DEFAULT 0;
ALTER TABLE financialdb.eg_non_confirm_collection_sum ALTER COLUMN bank_amount SET DEFAULT 0;
