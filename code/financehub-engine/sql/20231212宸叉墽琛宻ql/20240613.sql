CREATE TABLE financialdb.eg_ta_other_payable (
     id int8 NOT NULL,
     batch_no varchar(100) NULL,
     reclassification_amount numeric(20, 2) NULL DEFAULT 0,
     process_status varchar(64) NULL DEFAULT 1,
     process_instance_id int8 NULL,
     is_generate_voucher bpchar(1) NULL DEFAULT 0,
     voucher_id text NULL,
     error_info text NULL,
     del_flag bpchar(1) NULL DEFAULT 0,
     create_by varchar(32) NULL,
     create_time timestamp NULL,
     update_by varchar(32) NULL,
     update_time timestamp NULL,
     reclassification_month timestamp NULL
);

CREATE TABLE financialdb.eg_ta_other_payable_detail (
    id int8 NOT NULL,
    reclassification_month timestamp NULL,
    bank_org_id varchar(100) NULL,
    system_code varchar(50) NULL,
    ebank_serial_number varchar(100) NULL,
    ebank_batch_no varchar(100) NULL,
    financial_primary_classic varchar(200) NULL,
    confirm_account_property varchar(200) NULL,
    reclassification_amount numeric(20, 2) NULL DEFAULT 0,
    account_code varchar(100) NULL,
    account_name varchar(200) NULL,
    account_date timestamp NULL,
    account_age int4 NULL,
    account_age_class varchar(100) NULL,
    pay_client_name varchar(200) NULL,
    process_status varchar(64) NULL DEFAULT 0,
    process_instance_id int8 NULL,
    is_generate_voucher bpchar(1) NULL DEFAULT 0,
    voucher_id text NULL,
    error_info text NULL,
    del_flag bpchar(1) NULL DEFAULT 0,
    create_by varchar(32) NULL,
    create_time timestamp NULL,
    update_by varchar(32) NULL,
    update_time timestamp NULL,
    ta_other_payable_id int8 NULL,
    CONSTRAINT eg_ta_other_payable_detail_pk PRIMARY KEY (id)
);

CREATE TABLE financialdb.eg_ta_reclassification (
    id int8 NOT NULL,
    reclassification_month timestamp NULL,
    org_id varchar(100) NULL,
    ta_reclassification_amount numeric(20, 2) NULL DEFAULT 0,
    process_status varchar(64) NULL DEFAULT 0,
    process_instance_id int8 NULL,
    is_generate_voucher bpchar(1) NULL DEFAULT 0,
    voucher_id text NULL,
    error_info text NULL,
    account_date timestamp NULL,
    del_flag bpchar(1) NULL DEFAULT 0,
    create_by varchar(32) NULL,
    create_time timestamp NULL,
    update_by varchar(32) NULL,
    update_time timestamp NULL,
    CONSTRAINT eg_ta_reclassification_pk PRIMARY KEY (id)
);

CREATE TABLE financialdb.eg_ta_reclassification_detail (
   id int8 NOT NULL,
   reclassification_month timestamp NULL,
   system_code varchar(50) NULL,
   org_id varchar(100) NULL,
   contract_code varchar(100) NULL,
   business_code varchar(100) NULL,
   contract_status varchar(50) NULL,
   client_code varchar(100) NULL,
   client_name varchar(200) NULL,
   bank_org_id varchar(100) NULL,
   ebank_serial_number varchar(100) NULL,
   ebank_batch_no varchar(100) NULL,
   ebank_client_name varchar(200) NULL,
   operation_remark text NULL,
   ta_excess_balance numeric(20, 2) NULL DEFAULT 0,
   rent_check varchar(10) NULL,
   ebank_check varchar(10) NULL,
   ta_reclassification_amount numeric(20, 2) NULL DEFAULT 0,
   lease_date_end timestamp NULL,
   receivable_rent_balance numeric(20, 2) NULL DEFAULT 0,
   remark text NULL,
   special_contract_status varchar(100) NULL,
   ta_account_code varchar(100) NULL,
   ta_account_name varchar(200) NULL,
   exception_type text NULL,
   process_status varchar(64) NULL DEFAULT 0,
   process_instance_id int8 NULL,
   is_generate_voucher bpchar(1) NULL DEFAULT 0,
   voucher_id text NULL,
   error_info text NULL,
   account_date timestamp NULL,
   del_flag bpchar(1) NULL DEFAULT 0,
   create_by varchar(32) NULL,
   create_time timestamp NULL,
   update_by varchar(32) NULL,
   update_time timestamp NULL,
   ta_reclassification_id int8 NULL,
   account_code varchar(100) NULL,
   CONSTRAINT eg_ta_reclassification_detail_pk PRIMARY KEY (id)
);

CREATE TABLE financialdb.eg_ta_reclassification_upload_record (
  id int8 NOT NULL,
  batch_id varchar(100) NULL,
  reclassification_month timestamp NULL,
  contract_code varchar(100) NULL,
  ebank_batch_no varchar(100) NULL,
  ta_reclassification_amount numeric(20, 2) NULL DEFAULT 0,
  account_code varchar(100) NULL,
  remark text NULL,
  upload_by varchar(32) NULL,
  upload_status varchar(32) NULL,
  del_flag bpchar(1) NULL DEFAULT 0,
  create_by varchar(32) NULL,
  create_time timestamp NULL,
  update_by varchar(32) NULL,
  update_time timestamp NULL
);

ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD remark varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.remark IS 'comments';
ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD non_lease_account_checking_comments varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.non_lease_account_checking_comments IS '非租对账备注';
ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD non_lease_non_claim_amount numeric(20, 2) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.non_lease_non_claim_amount IS '非租未认领金额';
ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ADD non_lease_non_claim_reasons varchar(1000) NULL;
COMMENT ON COLUMN financialdb.eg_non_confirm_collection_account_checking.non_lease_non_claim_reasons IS '非租未认领原因';
ALTER TABLE financialdb.eg_non_confirm_collection_account_checking ALTER COLUMN collection_accounts_bank_code TYPE varchar(1000) USING collection_accounts_bank_code::varchar;
--uat d3,prod 以上已执行