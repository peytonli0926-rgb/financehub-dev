ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD account_age int4 NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.account_age IS '帐龄';
