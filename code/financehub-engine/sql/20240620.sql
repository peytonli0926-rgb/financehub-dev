ALTER TABLE eg_contract_balance ADD receivable_other_collection_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance.receivable_other_collection_balance IS '其它应收款项余额';

ALTER TABLE eg_contract_balance ADD receivable_other_collection_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance.receivable_other_collection_amount IS '其它应收款项发生额';

ALTER TABLE eg_contract_balance_latest ADD receivable_other_collection_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance_latest.receivable_other_collection_balance IS '其它应收款项余额';

ALTER TABLE eg_contract_balance_latest ADD receivable_other_collection_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance_latest.receivable_other_collection_amount IS '其它应收款项发生额';



ALTER TABLE eg_contract_balance_temp ADD receivable_other_collection_balance numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance_temp.receivable_other_collection_balance IS '其它应收款项余额';

ALTER TABLE eg_contract_balance_temp ADD receivable_other_collection_amount numeric(20, 2) NULL DEFAULT 0;
COMMENT ON COLUMN eg_contract_balance_temp.receivable_other_collection_amount IS '其它应收款项发生额';


CREATE INDEX eg_impairment_provision_detail_impairment_provision_id_idx ON financialdb.eg_impairment_provision_detail (impairment_provision_id);
CREATE INDEX eg_voucher_entry_contract_code_idx ON ONLY financialdb.eg_voucher_entry USING btree (contract_code);