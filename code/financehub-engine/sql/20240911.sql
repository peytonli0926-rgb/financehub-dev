ALTER TABLE financialdb4.eg_assets_expense ADD claim_order_detail_id int8 NOT NULL;
COMMENT ON COLUMN financialdb4.eg_assets_expense.claim_order_detail_id IS 'ID';

ALTER TABLE financialdb4.eg_claim_order_detail ADD is_download_file char(1) NULL;
COMMENT ON COLUMN financialdb4.eg_claim_order_detail.is_download_file IS '是否已经下载过文件';
ALTER TABLE financialdb4.eg_claim_order_detail ALTER COLUMN is_download_file SET DEFAULT 0;

ALTER TABLE financialdb4.eg_assets_expense ALTER COLUMN debt_to_batch_no TYPE varchar(50) USING debt_to_batch_no::varchar;
ALTER TABLE financialdb4.eg_assets_expense ALTER COLUMN account_age TYPE varchar(32) USING account_age::varchar;
