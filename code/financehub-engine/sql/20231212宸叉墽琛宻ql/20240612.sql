ALTER TABLE financialdb.eg_ta_other_payable ADD reclassification_month timestamp NULL;
COMMENT ON COLUMN financialdb.eg_ta_other_payable.reclassification_month IS '重分类月份';

ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD collection_accounts_bank_code text NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.collection_accounts_bank_code IS '到账主体编码';
