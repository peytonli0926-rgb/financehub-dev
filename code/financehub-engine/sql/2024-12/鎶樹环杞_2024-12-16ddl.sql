CREATE TABLE eg_convert_transfer
(
    id                  BIGINT PRIMARY KEY,
    batch               VARCHAR(255),
    business_date       TIMESTAMP,
    finance_date        TIMESTAMP,
    transfer_party      VARCHAR(255),
    transferee_party    VARCHAR(255),
    reference_date      TIMESTAMP,
    trade_date          TIMESTAMP,
    transfer_price      numeric(20, 2) default 0,
    contract_num        INTEGER,
    is_invoice_flag     VARCHAR(1),
    process_instance_id BIGINT,
    process_status      char(1),
    is_generate_voucher VARCHAR(1)     DEFAULT '0',
    voucher_id          VARCHAR(255),
    error_info          varchar(2000),
    account_date        TIMESTAMP,
    del_flag            char(1)     DEFAULT '0',
    create_by           VARCHAR(255),
    create_time         TIMESTAMP,
    update_by           VARCHAR(255),
    update_time         TIMESTAMP
);

-- 添加索引
create index idx_eg_convert_transfer_batch on eg_convert_transfer (batch);
create index idx_eg_convert_transfer_account_date on eg_convert_transfer (account_date);

-- 添加列注释
COMMENT ON COLUMN eg_convert_transfer.id IS 'ID';
COMMENT ON COLUMN eg_convert_transfer.batch IS '批次';
COMMENT ON COLUMN eg_convert_transfer.business_date IS '业务日期';
COMMENT ON COLUMN eg_convert_transfer.finance_date IS '财务日期';
COMMENT ON COLUMN eg_convert_transfer.transfer_party IS '转让方';
COMMENT ON COLUMN eg_convert_transfer.transferee_party IS '受让方';
COMMENT ON COLUMN eg_convert_transfer.reference_date IS '基准日';
COMMENT ON COLUMN eg_convert_transfer.trade_date IS '交易日';
COMMENT ON COLUMN eg_convert_transfer.transfer_price IS '转让价格';
COMMENT ON COLUMN eg_convert_transfer.contract_num IS '合同数量';
COMMENT ON COLUMN eg_convert_transfer.is_invoice_flag IS '转让后是否开发票（0：否，1：是）';
COMMENT ON COLUMN eg_convert_transfer.process_instance_id IS '流程id';
COMMENT ON COLUMN eg_convert_transfer.process_status IS '1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';
COMMENT ON COLUMN eg_convert_transfer.is_generate_voucher IS '是否已生成凭证（0：未生成1：已生成）默认0';
COMMENT ON COLUMN eg_convert_transfer.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN eg_convert_transfer.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN eg_convert_transfer.account_date IS '记账日期';
COMMENT ON COLUMN eg_convert_transfer.del_flag IS '是否删除（0：未删除1：删除）默认0';
COMMENT ON COLUMN eg_convert_transfer.create_by IS '创建人';
COMMENT ON COLUMN eg_convert_transfer.create_time IS '创建时间';
COMMENT ON COLUMN eg_convert_transfer.update_by IS '更新人';
COMMENT ON COLUMN eg_convert_transfer.update_time IS '更新时间';


CREATE TABLE eg_convert_transfer_detail
(
    id                          BIGINT PRIMARY KEY,
    convert_transfer_id         BIGINT,
    batch                       VARCHAR(255),
    contract_code               VARCHAR(255),
    client_code                 VARCHAR(255),
    client_name                 VARCHAR(255),
    org_id                      VARCHAR(255),
    financial_contract_status   VARCHAR(255),
    tax_rate                    numeric(20, 2) default 0,
    receivable_rent             numeric(20, 2) default 0,
    receivable_residual_value   numeric(20, 2) default 0,
    receivable_outtax           numeric(20, 2) default 0,
    unrealized_revenue          numeric(20, 2) default 0,
    lessee_margin               numeric(20, 2) default 0,
    depreciation_reserves       numeric(20, 2) default 0,
    appraised_value             numeric(20, 2) default 0,
    transfer_open               numeric(20, 2) default 0,
    supplementary_provision     numeric(20, 2) default 0,
    revenue_recognition         numeric(20, 2) default 0,
    payable_agency_estimate     numeric(20, 2) default 0,
    payable_vehicle_estimate    numeric(20, 2) default 0,
    payable_band_cost_estimate  numeric(20, 2) default 0,
    payable_pledge_estimate     numeric(20, 2) default 0,
    payable_unpledge_estimate   numeric(20, 2) default 0,
    payable_other_cost_estimate numeric(20, 2) default 0,
    base_date_accrued_income    numeric(20, 2) default 0,
    base_date_provision         numeric(20, 2) default 0,
    base_date_receive           numeric(20, 2) default 0,
    voucher_id                  VARCHAR(255),
    error_info                  varchar(2000),
    account_date                TIMESTAMP,
    del_flag                    char(1)     DEFAULT '0',
    create_by                   VARCHAR(255),
    create_time                 TIMESTAMP,
    update_by                   VARCHAR(255),
    update_time                 TIMESTAMP
);

-- 添加索引
CREATE INDEX idx_eg_convert_transfer_detail_convert_transfer_id ON eg_convert_transfer_detail (convert_transfer_id);
CREATE INDEX idx_eg_convert_transfer_detail_batch ON eg_convert_transfer_detail (batch);

-- 添加列注释
COMMENT ON COLUMN eg_convert_transfer_detail.id IS 'ID';
COMMENT ON COLUMN eg_convert_transfer_detail.convert_transfer_id IS '折价转让id';
COMMENT ON COLUMN eg_convert_transfer_detail.batch IS '转让批次';
COMMENT ON COLUMN eg_convert_transfer_detail.contract_code IS '原合同编码';
COMMENT ON COLUMN eg_convert_transfer_detail.client_code IS '客户编码';
COMMENT ON COLUMN eg_convert_transfer_detail.client_name IS '客户名称';
COMMENT ON COLUMN eg_convert_transfer_detail.org_id IS '签约主体';
COMMENT ON COLUMN eg_convert_transfer_detail.financial_contract_status IS '财务合同状态';
COMMENT ON COLUMN eg_convert_transfer_detail.tax_rate IS '税率';
COMMENT ON COLUMN eg_convert_transfer_detail.receivable_rent IS '应收租金';
COMMENT ON COLUMN eg_convert_transfer_detail.receivable_residual_value IS '应收期末残值';
COMMENT ON COLUMN eg_convert_transfer_detail.receivable_outtax IS '应收销项税';
COMMENT ON COLUMN eg_convert_transfer_detail.unrealized_revenue IS '未实现融资租赁收益';
COMMENT ON COLUMN eg_convert_transfer_detail.lessee_margin IS '承租人保证金';
COMMENT ON COLUMN eg_convert_transfer_detail.depreciation_reserves IS '应收租赁款组合拨备';
COMMENT ON COLUMN eg_convert_transfer_detail.appraised_value IS '评估价';
COMMENT ON COLUMN eg_convert_transfer_detail.transfer_open IS '转让时敞口';
COMMENT ON COLUMN eg_convert_transfer_detail.supplementary_provision IS '补提拨备';
COMMENT ON COLUMN eg_convert_transfer_detail.revenue_recognition IS '收益确认';
COMMENT ON COLUMN eg_convert_transfer_detail.payable_agency_estimate IS '应付经销商服务费-暂估';
COMMENT ON COLUMN eg_convert_transfer_detail.payable_vehicle_estimate IS '应付收车费-暂估';
COMMENT ON COLUMN eg_convert_transfer_detail.payable_band_cost_estimate IS '应付手环成本_暂估';
COMMENT ON COLUMN eg_convert_transfer_detail.payable_pledge_estimate IS '应付抵押费_暂估';
COMMENT ON COLUMN eg_convert_transfer_detail.payable_unpledge_estimate IS '应付解抵押费_暂估';
COMMENT ON COLUMN eg_convert_transfer_detail.payable_other_cost_estimate IS '应付其他租赁成本-暂估';
COMMENT ON COLUMN eg_convert_transfer_detail.base_date_accrued_income IS '基准日后计提收益';
COMMENT ON COLUMN eg_convert_transfer_detail.base_date_provision IS '基准日后计提拨备';
COMMENT ON COLUMN eg_convert_transfer_detail.base_date_receive IS '基准日后收款';
COMMENT ON COLUMN eg_convert_transfer_detail.voucher_id IS '凭证id,多个按照逗号分隔';
COMMENT ON COLUMN eg_convert_transfer_detail.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN eg_convert_transfer_detail.account_date IS '记账日期';
COMMENT ON COLUMN eg_convert_transfer_detail.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN eg_convert_transfer_detail.create_by IS '创建人';
COMMENT ON COLUMN eg_convert_transfer_detail.create_time IS '创建时间';
COMMENT ON COLUMN eg_convert_transfer_detail.update_by IS '更新人';
COMMENT ON COLUMN eg_convert_transfer_detail.update_time IS '更新时间';

CREATE TABLE eg_convert_transfer_plan
(
    id                        BIGINT PRIMARY KEY,
    convert_transfer_id       BIGINT,
    batch                     VARCHAR(255),
    old_contract_code         VARCHAR(255),
    new_contract_code         VARCHAR(255),
    plan_date                 TIMESTAMP,
    periods                   INTEGER,
    receivable_rent           numeric(20, 2) default 0,
    receivable_principal      numeric(20, 2) default 0,
    receivable_interest       numeric(20, 2) default 0,
    receivable_ending_salvage numeric(20, 2) default 0,
    voucher_id                VARCHAR(255),
    account_date              TIMESTAMP,
    error_info                varchar(2000),
    del_flag                  char(1)     DEFAULT '0',
    create_by                 VARCHAR(255),
    create_time               TIMESTAMP,
    update_by                 VARCHAR(255),
    update_time               TIMESTAMP
);

-- 添加字段注释
COMMENT ON COLUMN eg_convert_transfer_plan.id IS 'ID';
COMMENT ON COLUMN eg_convert_transfer_plan.convert_transfer_id IS '折价转让id';
COMMENT ON COLUMN eg_convert_transfer_plan.batch IS '转让批次';
COMMENT ON COLUMN eg_convert_transfer_plan.old_contract_code IS '原合同编码';
COMMENT ON COLUMN eg_convert_transfer_plan.new_contract_code IS '新合同编码';
COMMENT ON COLUMN eg_convert_transfer_plan.plan_date IS '计划日期';
COMMENT ON COLUMN eg_convert_transfer_plan.periods IS '期数';
COMMENT ON COLUMN eg_convert_transfer_plan.receivable_rent IS '应收租金';
COMMENT ON COLUMN eg_convert_transfer_plan.receivable_principal IS '应收本金';
COMMENT ON COLUMN eg_convert_transfer_plan.receivable_interest IS '应收利息';
COMMENT ON COLUMN eg_convert_transfer_plan.receivable_ending_salvage IS '应收期末残值';
COMMENT ON COLUMN eg_convert_transfer_plan.voucher_id IS '凭证id(多个逗号分隔)';
COMMENT ON COLUMN eg_convert_transfer_plan.account_date IS '财务日期';
COMMENT ON COLUMN eg_convert_transfer_plan.error_info IS '生成凭证报错信息';
COMMENT ON COLUMN eg_convert_transfer_plan.del_flag IS '是否删除（0-否，1-是）';
COMMENT ON COLUMN eg_convert_transfer_plan.create_by IS '创建人';
COMMENT ON COLUMN eg_convert_transfer_plan.create_time IS '创建时间';
COMMENT ON COLUMN eg_convert_transfer_plan.update_by IS '更新人';
COMMENT ON COLUMN eg_convert_transfer_plan.update_time IS '更新时间';

-- 为 convert_transfer_id 字段添加索引
CREATE INDEX idx_eg_convert_transfer_plan_convert_transfer_id ON eg_convert_transfer_plan (convert_transfer_id);

-- 为 batch 字段添加索引
CREATE INDEX idx_eg_convert_transfer_plan_batch ON eg_convert_transfer_plan (batch);

-- 资产转让-折价转让-内部调拨
create table eg_discounted_transfer_internal
(
    id                  bigint       not null
        constraint eg_discounted_transfer_internal_pk
            primary key,
    batch               varchar(100) not null,
    transfer_party      varchar(100),
    contract_code       varchar(100),
    amount              numeric(20, 2) default 0,
    payment_date        timestamp,
    bank_account_code   varchar(100),
    process_instance_id bigint,
    process_status      varchar(100)   default 1,
    is_generate_voucher char           default '0'::bpchar,
    voucher_id          varchar(2000),
    error_info          varchar(2000),
    account_date        timestamp,
    del_flag            char           default 0,
    create_by           varchar(32),
    create_time         timestamp,
    update_by           varchar(32),
    update_time         timestamp,
    finance_date        timestamp,
    client_code         varchar(100)
);

comment on table eg_internal_transfer is '资产转让-折价转让-内部调拨';

comment on column eg_internal_transfer.id is 'ID';

comment on column eg_internal_transfer.batch is '批次';

comment on column eg_internal_transfer.transfer_party is '转让方';

comment on column eg_internal_transfer.contract_code is '合同编码';

comment on column eg_internal_transfer.amount is '余额';

comment on column eg_internal_transfer.payment_date is '支付日期';

comment on column eg_internal_transfer.bank_account_code is '银行账号编码';

comment on column eg_internal_transfer.process_instance_id is '流程id';

comment on column eg_internal_transfer.process_status is '处理状态 1:已录入,2:已提交,3:已复核,4:已传至金蝶,5:已拒绝';

comment on column eg_internal_transfer.is_generate_voucher is '是否已生成凭证（0：未生成1：已生成）默认0';

comment on column eg_internal_transfer.voucher_id is '凭证id,多个按照逗号分隔';

comment on column eg_internal_transfer.error_info is '生成凭证报错信息';

comment on column eg_internal_transfer.account_date is '记账日期';

comment on column eg_internal_transfer.del_flag is '是否删除（0-否，1-是）';

comment on column eg_internal_transfer.create_by is '创建人';

comment on column eg_internal_transfer.create_time is '创建时间';

comment on column eg_internal_transfer.update_by is '更新人';

comment on column eg_internal_transfer.update_time is '更新时间';

comment on column eg_internal_transfer.finance_date is '财务日期';

comment on column eg_internal_transfer.client_code is '客户编码';


