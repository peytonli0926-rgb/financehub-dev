ALTER TABLE financialdb.eg_offline_contract ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_offline_contract.process_instance_id IS '流程id';

ALTER TABLE financialdb.eg_contract_his ADD process_instance_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_contract_his.process_instance_id IS '流程id';


ALTER TABLE financialdb.eg_impairment_provision_detail ADD impairment_provision_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision_detail.impairment_provision_id IS '减值计提id';

ALTER TABLE financialdb.eg_impairment_provision ADD write_off_original_id int8 NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision.write_off_original_id IS '冲销来源id';
ALTER TABLE financialdb.eg_impairment_provision ADD is_write_off bpchar(1) DEFAULT 0 NULL;
COMMENT ON COLUMN financialdb.eg_impairment_provision.is_write_off IS '是否被冲销（0：否，1：是）';

ALTER TABLE financialdb.eg_non_confirm_collection_sum ADD collection_accounts_bank_code text NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.collection_accounts_bank_code IS '到账主体编码';
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_sum.collection_accounts_bank IS '到账主体名称';
ALTER TABLE financialdb.eg_non_confirm_collection_sum DROP COLUMN org_id;
ALTER TABLE financialdb.eg_non_confirm_collection_sum DROP COLUMN org_name;

ALTER TABLE financialdb.eg_non_confirm_collection_sum DROP COLUMN collection_accounts_bank_name;
ALTER TABLE financialdb.eg_manual ADD create_user_name varchar(100) NULL;
COMMENT ON COLUMN financialdb.eg_manual.create_user_name IS '创建用户名称';
--以上uat prod已执行


ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD cur_claim_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.cur_claim_amount IS '该次认领金额总额';
ALTER TABLE financialdb.eg_non_confirm_collection_second_detail ADD write_off_detail_ids varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_second_detail.write_off_detail_ids IS '冲销明细的ID';
