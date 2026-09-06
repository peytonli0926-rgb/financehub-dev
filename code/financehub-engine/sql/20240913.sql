ALTER TABLE financialdb4.eg_claim_order_detail ADD attach_neid int8 NULL;
COMMENT ON COLUMN financialdb4.eg_claim_order_detail.attach_neid IS '云盘附件定位neid';
ALTER TABLE financialdb4.eg_claim_order_detail ADD attach_nsid int NULL;
COMMENT ON COLUMN financialdb4.eg_claim_order_detail.attach_nsid IS '云盘附件定位nsid';

ALTER TABLE financialdb4.eg_assets_expense ALTER COLUMN service_fee_rate TYPE varchar(20) USING service_fee_rate::varchar;
