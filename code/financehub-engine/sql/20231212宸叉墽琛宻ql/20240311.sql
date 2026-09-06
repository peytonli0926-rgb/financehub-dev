--出表ABS
-- financialdb.eg_out_table_abs definition

-- Drop table

-- DROP TABLE financialdb.eg_out_table_abs;

CREATE TABLE financialdb.eg_out_table_abs (
                                              id int8 NOT NULL, -- ID
                                              loan_contract_code varchar(100) NULL, -- 借款合同编号
                                              business_date timestamp NULL, -- 业务日期
                                              account_date timestamp NULL, -- 业务日期
                                              periods varchar(100) NULL, -- 期数
                                              administrator varchar(100) NULL, -- 管理人
                                              close_date timestamp NULL, -- 封包日
                                              release_date timestamp NULL, -- 发行日
                                              transfer_price numeric(20, 2) NULL, -- 转让价格
                                              contract_num int4 NULL, -- 合同数量
                                              calculation_period varchar(100) NULL, -- 计算周期
                                              transfer_period varchar(100) NULL, -- 转付周期
                                              cash_period varchar(100) NULL, -- 兑付周期
                                              process_instance_id int8 NULL, -- 流程id
                                              process_status varchar(100) NULL, -- 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝
                                              is_generate_voucher bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否已生成凭证（0：未生成1：已生成）默认0
                                              approve_error_info text NULL, -- 审批报错信息
                                              del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
                                              create_by varchar(32) NULL, -- 创建人
                                              create_time timestamp NULL, -- 创建时间
                                              update_by varchar(32) NULL, -- 更新人
                                              update_time timestamp NULL, -- 更新时间
                                              CONSTRAINT eg_out_table_abs_pk PRIMARY KEY (id)
);
CREATE INDEX eg_out_table_abs_loan_contract_code_idx ON financialdb.eg_out_table_abs USING btree (loan_contract_code, business_date, account_date, periods);
COMMENT ON TABLE financialdb.eg_out_table_abs IS '出表ABS';

-- Column comments

COMMENT ON COLUMN financialdb.eg_out_table_abs.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_out_table_abs.loan_contract_code IS '借款合同编号';
COMMENT ON COLUMN financialdb.eg_out_table_abs.business_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.account_date IS '业务日期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.periods IS '期数';
COMMENT ON COLUMN financialdb.eg_out_table_abs.administrator IS '管理人';
COMMENT ON COLUMN financialdb.eg_out_table_abs.close_date IS '封包日';
COMMENT ON COLUMN financialdb.eg_out_table_abs.release_date IS '发行日';
COMMENT ON COLUMN financialdb.eg_out_table_abs.transfer_price IS '转让价格';
COMMENT ON COLUMN financialdb.eg_out_table_abs.contract_num IS '合同数量';
COMMENT ON COLUMN financialdb.eg_out_table_abs.calculation_period IS '计算周期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.transfer_period IS '转付周期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.cash_period IS '兑付周期';
COMMENT ON COLUMN financialdb.eg_out_table_abs.process_instance_id IS '流程id';
COMMENT ON COLUMN financialdb.eg_out_table_abs.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN financialdb.eg_out_table_abs.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN financialdb.eg_out_table_abs.approve_error_info IS '审批报错信息';
COMMENT ON COLUMN financialdb.eg_out_table_abs.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_out_table_abs.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_out_table_abs.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_out_table_abs.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_out_table_abs.update_time IS '更新时间';

-- Permissions

ALTER TABLE financialdb.eg_out_table_abs OWNER TO financialdb;
GRANT ALL ON TABLE financialdb.eg_out_table_abs TO financialdb;
--出表合同详情
-- financialdb.eg_out_table_contract_detail definition

-- Drop table

-- DROP TABLE financialdb.eg_out_table_contract_detail;

CREATE TABLE financialdb.eg_out_table_contract_detail (
                                                          id int8 NOT NULL, -- ID
                                                          out_table_abs_id int8 NOT NULL, -- 出表absId
                                                          loan_contract_code varchar(100) NULL, -- 借款合同编码
                                                          contract_code varchar(100) NULL, -- 合同编号
                                                          client_code varchar(100) NULL, -- 客户编码
                                                          client_name varchar(100) NULL, -- 客户名称
                                                          org_id varchar(100) NULL, -- 签约主体
                                                          financial_contract_status varchar(100) NULL, -- 财务合同状态
                                                          receivable_rent numeric(20, 2) NULL, -- 封包日应收租金
                                                          receivable_residual_value numeric(20, 2) NULL, -- 封包日应收残值
                                                          receivable_outtax numeric(20, 2) NULL, -- 封包日应收销项税
                                                          unrealized_revenue numeric(20, 2) NULL, -- 封包日为实现收益
                                                          lessee_margin numeric(20, 2) NULL, -- 封包日承租人保证金
                                                          voucher_id text NULL, -- 凭证id,多个按照逗号分隔
                                                          error_info varchar(2000) NULL, -- 生成凭证报错信息
                                                          del_flag bpchar(1) NULL DEFAULT '0'::bpchar, -- 是否删除（0：未删除1：删除）默认0
                                                          create_by varchar(32) NULL, -- 创建人
                                                          create_time timestamp NULL, -- 创建时间
                                                          update_by varchar(32) NULL, -- 更新人
                                                          update_time timestamp NULL, -- 更新时间
                                                          CONSTRAINT eg_out_table_contract_detail_pk PRIMARY KEY (id)
);
CREATE INDEX eg_out_table_contract_detail_id_idx ON financialdb.eg_out_table_contract_detail USING btree (id, out_table_abs_id, loan_contract_code, contract_code, client_code, client_name, org_id, financial_contract_status);
COMMENT ON TABLE financialdb.eg_out_table_contract_detail IS '出表ABS合同详情';

-- Column comments

COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.out_table_abs_id IS '出表absId';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.loan_contract_code IS '借款合同编码';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.contract_code IS '合同编号';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.client_code IS '客户编码';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.client_name IS '客户名称';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.org_id IS '签约主体';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.receivable_rent IS '封包日应收租金';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.receivable_residual_value IS '封包日应收残值';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.receivable_outtax IS '封包日应收销项税';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.unrealized_revenue IS '封包日为实现收益';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.lessee_margin IS '封包日承租人保证金';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_out_table_contract_detail.update_time IS '更新时间';

-- Permissions

ALTER TABLE financialdb.eg_out_table_contract_detail OWNER TO financialdb;
GRANT ALL ON TABLE financialdb.eg_out_table_contract_detail TO financialdb;



ALTER table eg_kingdee_voucher_entry ADD org_id varchar(100) NULL;
COMMENT ON COLUMN financialdb2.eg_kingdee_voucher_entry.org_id IS '签约主体';