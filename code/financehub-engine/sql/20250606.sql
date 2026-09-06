create table eg_service_fee_plan
(
    id                            bigint,
    contract_code                 varchar(100),
    org_id                        varchar(50),
    service_fee_no                varchar(100),
    service_org_id                varchar(50),
    service_fee_amortization_rate numeric(14, 6),
    actual_accrued_amount         numeric(20, 2),
    adjust_amount                 numeric(20, 2),
    del_flag                      char,
    create_by                     varchar(32),
    create_time                   timestamp(6),
    update_by                     varchar(32),
    update_time                   timestamp(6),
    plan_date                     date,
    service_fee_total             numeric(20, 2),
    service_fee_agreed_amount     numeric(20, 2),
    actual_receive_service_fee    numeric(20, 2),
    contract_confirmed_amount     numeric(20, 2),
    agreed_confirmed_amount       numeric(20, 2),
    agreed_apportion_amount       numeric(20, 2),
    contract_device_amount        numeric(20, 2),
    plan_apportion_amount         numeric(20, 2),
    periods                       integer,
    plan_apportion_no_tax         numeric(20, 2),
    plan_amount                   numeric(20, 2),
    plan_amount_no_tax            numeric(20, 2),
    actual_receive_no_tax         numeric(20, 2),
    service_fee_total_no_tax      numeric(20, 2)
);

comment on column eg_service_fee_plan.service_fee_total_no_tax is '服务费收入税后';

create table eg_service_fee
(
    id                              bigint not null
        constraint eg_service_fee_pk
            primary key,
    account_date                    timestamp(6),
    business_date                   timestamp(6),
    org_id                          varchar(100),
    service_fee_amortization_income numeric(20, 2),
    process_status                  varchar(50),
    is_generate_voucher             char default '0'::bpchar,
    del_flag                        char default '0'::bpchar,
    create_by                       varchar(32),
    create_time                     timestamp(6),
    update_by                       varchar(32),
    update_time                     timestamp(6),
    submit_by                       varchar(100),
    process_instance_id             bigint,
    voucher_status                  varchar(5)
);

comment on table eg_service_fee is '服务费分摊表';

comment on column eg_service_fee.id is 'ID';

comment on column eg_service_fee.account_date is '记账日期';

comment on column eg_service_fee.business_date is '业务日期';

comment on column eg_service_fee.org_id is '签约主体';

comment on column eg_service_fee.service_fee_amortization_income is '服务费摊销收入';

comment on column eg_service_fee.process_status is '处理状态';

comment on column eg_service_fee.is_generate_voucher is '是否已生成凭证（0：未生成1：已生成）默认0';

comment on column eg_service_fee.del_flag is '是否删除（0：未删除1：删除）默认0';

comment on column eg_service_fee.create_by is '创建人';

comment on column eg_service_fee.create_time is '创建时间';

comment on column eg_service_fee.update_by is '更新人';

comment on column eg_service_fee.update_time is '更新时间';

comment on column eg_service_fee.submit_by is '提交人';

comment on column eg_service_fee.process_instance_id is '流程实例id';

comment on column eg_service_fee.voucher_status is '凭证状态';

create index eg_service_fee_id_idx
    on eg_service_fee (id);

create table eg_service_fee_details
(
    id                                                bigint not null
        constraint eg_service_fee_details_pk
            primary key,
    account_date                                      timestamp(6),
    business_date                                     timestamp(6),
    service_fee_id                                    bigint,
    contract_code                                     varchar(100),
    service_fee_no                                    varchar(100),
    client_code                                       varchar(100),
    client_name                                       varchar(200),
    org_id                                            varchar(50),
    service_org_id                                    varchar(50),
    contract_status                                   varchar(50),
    allocation_method                                 varchar(100),
    business_code                                     varchar(100),
    business_name                                     varchar(200),
    lease_date_start                                  timestamp(6),
    lease_date_end                                    timestamp(6),
    service_fee_received                              numeric(20, 2),
    service_fee_allocation_no_tax                     numeric(20, 2),
    last_month_service_fee_allocation_no_tax          numeric(20, 2),
    reclassification_adjustment_no_tax_amount         numeric(20, 2),
    accrual_type                                      varchar(100),
    before_x_year_month_amount                        numeric(20, 2),
    x_year_month_adjustment_amount                    numeric(20, 2),
    allocation_after_x_year_month_balance             numeric(20, 2),
    not_accrued_amount                                numeric(20, 2),
    is_generate_voucher                               char default '0'::bpchar,
    periods                                           integer,
    financial_contract_status                         varchar(50),
    accumulated_accrued_amount                        numeric(20, 2),
    allocation_completion_mark                        char default '0'::bpchar,
    exception_type                                    varchar(200),
    voucher_id                                        varchar(2000),
    del_flag                                          char default 0,
    create_by                                         varchar(32),
    create_time                                       timestamp(6),
    update_by                                         varchar(32),
    update_time                                       timestamp(6),
    allocation_ratio                                  numeric(24, 6),
    service_fee_received_tax_included                 numeric(19, 6),
    service_fee_allocation_tax_included               numeric(19, 6),
    last_month_service_fee_allocation_tax_included    numeric(19, 6),
    reclassification_adjustment_tax_included          numeric(19, 6),
    before_this_month_amount_tax_included             numeric(19, 6),
    this_month_adjustment_amount_tax_included         numeric(19, 6),
    allocation_before_this_month_balance_tax_included numeric(19, 6),
    before_this_month_amount_no_tax                   numeric(19, 6),
    this_month_adjustment_amount_no_tax               numeric(19, 6),
    allocation_before_this_month_balance_no_tax       numeric(19, 6)
CREATE TABLE financialdb.eg_org_claim_ebank_no (
	id int8 NOT NULL,
	system_code varchar(100) NULL,
	ebank_no varchar(200) NULL,
	create_by varchar(32) NULL,
	create_time timestamp NULL,
	update_by varchar(32) NULL,
	update_time timestamp NULL,
	del_flag bpchar(1) NULL DEFAULT '0'::bpchar
);
COMMENT ON TABLE financialdb.eg_org_claim_ebank_no IS '机构认领的网银编号';

comment on table eg_service_fee_details is '服务费分摊表详情';

comment on column eg_service_fee_details.id is 'ID';

comment on column eg_service_fee_details.account_date is '记账日期';

comment on column eg_service_fee_details.business_date is '业务日期';

comment on column eg_service_fee_details.service_fee_id is '服务费分摊表id';

comment on column eg_service_fee_details.contract_code is '合同编号';

comment on column eg_service_fee_details.service_fee_no is '服务费协议编号';

comment on column eg_service_fee_details.client_code is '客户编码';

comment on column eg_service_fee_details.client_name is '客户名称';

comment on column eg_service_fee_details.org_id is '签约主体';

comment on column eg_service_fee_details.service_org_id is '服务费签约主体';

comment on column eg_service_fee_details.contract_status is '业务合同状态';

comment on column eg_service_fee_details.allocation_method is '分摊分式(租赁收入分摊、服务费收入分摊)';

comment on column eg_service_fee_details.business_code is '业务类型编码';

comment on column eg_service_fee_details.business_name is '业务类型名称';

comment on column eg_service_fee_details.lease_date_start is '起租日';

comment on column eg_service_fee_details.lease_date_end is '到期日';

comment on column eg_service_fee_details.service_fee_received is '服务费实收（税后）';

comment on column eg_service_fee_details.service_fee_allocation_no_tax is '应分摊的服务费收入（税后）';

comment on column eg_service_fee_details.last_month_service_fee_allocation_no_tax is '上月服务费应分摊金额（税后）';

comment on column eg_service_fee_details.reclassification_adjustment_no_tax_amount is '本月重分类调整(税后)';

comment on column eg_service_fee_details.accrual_type is '计提类型';

comment on column eg_service_fee_details.before_x_year_month_amount is 'X年X月以前';

comment on column eg_service_fee_details.x_year_month_adjustment_amount is 'X年X月调整';

comment on column eg_service_fee_details.allocation_after_x_year_month_balance is 'X年X月摊销后余额';

comment on column eg_service_fee_details.not_accrued_amount is '实际未计提金额';

comment on column eg_service_fee_details.is_generate_voucher is '是否已生成凭证（0：未生成1：已生成）默认0';

comment on column eg_service_fee_details.periods is '本次分摊期数';

comment on column eg_service_fee_details.financial_contract_status is '财务合同状态';

comment on column eg_service_fee_details.accumulated_accrued_amount is '累计已计提金额';

comment on column eg_service_fee_details.allocation_completion_mark is '分摊完结标记';

comment on column eg_service_fee_details.exception_type is '异常类型';

comment on column eg_service_fee_details.voucher_id is '凭证id';

comment on column eg_service_fee_details.del_flag is '是否删除 0：未删除1：已删除';

comment on column eg_service_fee_details.create_by is '创建人';

comment on column eg_service_fee_details.create_time is '创建时间';

comment on column eg_service_fee_details.update_by is '更新人';

comment on column eg_service_fee_details.update_time is '更新时间';

comment on column eg_service_fee_details.allocation_ratio is '分摊比例';

comment on column eg_service_fee_details.service_fee_received_tax_included is '服务费实收（税前）';

comment on column eg_service_fee_details.service_fee_allocation_tax_included is '应分摊的服务费收入（税前）';

comment on column eg_service_fee_details.last_month_service_fee_allocation_tax_included is '上月服务费应分摊金额（税前）';

comment on column eg_service_fee_details.reclassification_adjustment_tax_included is '本月重分类调整(税前)';

comment on column eg_service_fee_details.before_this_month_amount_tax_included is '本月以前（税前）';

comment on column eg_service_fee_details.this_month_adjustment_amount_tax_included is '本月调整（税前）';

comment on column eg_service_fee_details.allocation_before_this_month_balance_tax_included is '本月摊销后余额（税前）';

comment on column eg_service_fee_details.before_this_month_amount_no_tax is '本月以前（税后）';

comment on column eg_service_fee_details.this_month_adjustment_amount_no_tax is '本月调整（税后）';

comment on column eg_service_fee_details.allocation_before_this_month_balance_no_tax is '本月摊销后余额（税后）';


create index eg_service_fee_details_id_idx
    on eg_service_fee_details (id, contract_code, client_name, org_id, account_date);
-- Column comments

COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.id IS 'ID';
COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.system_code IS '系统编码';
COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.ebank_no IS '网银编号';
COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.create_by IS '创建人';
COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.create_time IS '创建时间';
COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.update_by IS '更新人';
COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.update_time IS '更新时间';
COMMENT ON COLUMN financialdb.eg_org_claim_ebank_no.del_flag IS '删除标识(0:未删除,1:已删除)';
