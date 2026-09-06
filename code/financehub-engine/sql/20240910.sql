ALTER TABLE financialdb4.eg_claim_order_detail ADD attach_id varchar(200) NULL;
COMMENT ON COLUMN financialdb4.eg_claim_order_detail.attach_id IS '附件ID';
