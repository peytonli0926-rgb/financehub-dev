

CREATE TABLE financialdb.eg_file_record (
    id int8 NOT NULL,
    module_name varchar(100) NULL,
    business_scene varchar(100) NULL,
    file_location varchar(400) NULL,
    file_name varchar(400) NULL,
    execute_status varchar(50) NULL,
    file_upload_time timestamp(6) NULL,
    del_flag bpchar(1) NULL DEFAULT '0'::bpchar,
    create_by varchar(32) NULL,
    create_time timestamp(6) NULL,
    update_by varchar(32) NULL,
    update_time timestamp(6) NULL,
    file_upload_by varchar(200) NULL,
    CONSTRAINT eg_file_record_pk PRIMARY KEY (id)
);

DROP INDEX financialdb.eg_raw_transaction_data_message_id_idx;

--以上uat 已执行，prod 未执行