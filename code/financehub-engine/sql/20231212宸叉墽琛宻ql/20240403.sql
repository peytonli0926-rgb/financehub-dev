ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD currency_type varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.currency_type IS '币种';

ALTER TABLE financialdb.eg_non_confirm_collection_second_detail RENAME COLUMN manual_voucher_id TO manual_voucher_ids;
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ALTER COLUMN manual_voucher_ids TYPE varchar(1000) USING manual_voucher_ids::varchar;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.manual_voucher_ids IS '手工凭证id(逗号分隔)';
