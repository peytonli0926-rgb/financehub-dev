--尾差调整
-- financialdb.eg_tail_difference_adjustment definition

-- Drop table

-- DROP TABLE financialdb.eg_tail_difference_adjustment;

CREATE TABLE financialdb.eg_tail_difference_adjustment (
                                                           id int8 NOT NULL, -- ID
                                                           org_id varchar(100) NOT NULL, -- 签约主体
                                                           account_code varchar(100) NOT NULL, -- 科目编码
                                                           account_name varchar(100) NULL, -- 科目名称
                                                           business_date timestamp NULL, -- 业务日期
                                                           account_date timestamp NULL, -- 记账日期
                                                           process_instance_id int8 NULL, -- 流程id
                                                           process_status varchar(100) NULL, -- 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
                                                           del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
                                                           create_by varchar(32) NULL, -- 创建人
                                                           create_time timestamp NULL, -- 创建时间
                                                           update_by varchar(32) NULL, -- 更新人
                                                           update_time timestamp NULL, -- 更新时间
                                                           CONSTRAINT eg_tail_difference_adjustment_pk PRIMARY KEY (id)
);
CREATE INDEX eg_tail_difference_adjustment_id_idx ON financialdb.eg_tail_difference_adjustment USING btree (id, org_id, account_code, business_date, process_status);
COMMENT ON TABLE financialdb.eg_tail_difference_adjustment IS '尾差调整';

-- Column comments

COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.account_code IS '科目编码';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.account_name IS '科目名称';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.process_instance_id IS '流程id';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment.update_time IS '更新时间';

-- Permissions

ALTER TABLE financialdb.eg_tail_difference_adjustment OWNER TO financialdb;
GRANT ALL ON TABLE financialdb.eg_tail_difference_adjustment TO financialdb;


--尾差调整详情

-- financialdb.eg_tail_difference_adjustment_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_tail_difference_adjustment_detail;

CREATE TABLE financialdb.eg_tail_difference_adjustment_detail (
                                                                  id int8 NOT NULL, -- ID
                                                                  contract_code varchar(100) NOT NULL, -- 合同编号
                                                                  org_id varchar(100) NOT NULL, -- 签约主体
                                                                  account_code varchar(100) NOT NULL, -- 科目编码
                                                                  account_name varchar(100) NULL, -- 科目名称
                                                                  business_date timestamp NULL, -- 业务日期
                                                                  account_date timestamp NULL, -- 记账日期
                                                                  account_balance numeric(20, 2) NULL DEFAULT 0, -- 科目余额
                                                                  tail_difference_adjustment_id int8 NOT NULL, -- 尾差调整Id
                                                                  del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
                                                                  create_by varchar(32) NULL, -- 创建人
                                                                  create_time timestamp NULL, -- 创建时间
                                                                  update_by varchar(32) NULL, -- 更新人
                                                                  update_time timestamp NULL, -- 更新时间
                                                                  CONSTRAINT eg_tail_difference_adjustment_detail_pk PRIMARY KEY (id)
);
CREATE INDEX eg_tail_difference_adjustment_detail_contract_code_idx ON financialdb.eg_tail_difference_adjustment_detail USING btree (contract_code);
COMMENT ON TABLE financialdb.eg_tail_difference_adjustment_detail IS '尾差调整详情';

-- Column comments

COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.account_code IS '科目编码';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.account_name IS '科目名称';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.account_date IS '记账日期';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.account_balance IS '科目余额';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.tail_difference_adjustment_id IS '尾差调整Id';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_tail_difference_adjustment_detail.update_time IS '更新时间';

-- Permissions
