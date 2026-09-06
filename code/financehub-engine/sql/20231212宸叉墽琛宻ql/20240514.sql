ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD voucher_ids varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.voucher_ids IS '凭证id';
