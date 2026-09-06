alter table eg_contract
    add sharing_service_fee_flag boolean default TRUE;

comment
    on column eg_contract.sharing_service_fee_flag is '分摊服务费标识';

alter table eg_contract
    add end_sharing_service_fee_flag boolean default false;

comment
    on column eg_contract.end_sharing_service_fee_flag is '结束服务费分摊标识';

alter table eg_contract
    add set_end_sharing_service_fee_period int default 0;

comment
    on column eg_contract.set_end_sharing_service_fee_period is '设置结束服务费分摊标识时期间';

alter table eg_contract
    add special_status_adjustment_flag boolean default false;

comment
on column eg_contract.special_status_adjustment_flag is '是否特殊状态调整';


create table eg_repayment_plan_snapshot
(
    id                                               bigint not null primary key,
    client_code                                      varchar(100),
    client_name                                      varchar(200),
    contract_code                                    varchar(100),
    contract_name                                    varchar(200),
    plan_date                                        date,
    rent_amount                                      numeric(20, 2),
    principal_amount                                 numeric(20, 2),
    interest_amount                                  numeric(20, 2),
    principal_tax                                    numeric(20, 2),
    interest_tax                                     numeric(20, 2),
    outflow_amount                                   numeric(20, 2),
    planned_interest                                 numeric(20, 2),
    planned_principal                                numeric(20, 2),
    cash_flow                                        numeric(20, 2),
    opening_amortized_cost                           numeric(20, 2),
    ending_amortized_cost                            numeric(20, 2),
    actual_daily_rate                                numeric(14, 6),
    rental_income                                    numeric(20, 2),
    service_fee_amortization_rate                    numeric(20, 6),
    service_fee_amortization_income                  numeric(20, 2),
    xirr_rate                                        numeric(14, 6),
    actual_repayment_date                            date,
    actual_repayment_principal_balance               numeric(20, 2),
    actual_repayment_principal_amount                numeric(20, 2),
    actual_repayment_interes_balance                 numeric(20, 2),
    actual_repayment_interes_amount                  numeric(20, 2),
    recapture_status                                 varchar(100),
    overdue_earnings                                 numeric(20, 2),
    create_by                                        varchar(32),
    create_time                                      timestamp,
    update_by                                        varchar(32),
    update_time                                      timestamp,
    del_flag                                         char           default '0'::bpchar,
    comment                                          varchar(500),
    overdue_days                                     integer,
    periods                                          integer,
    pay_method                                       varchar(100),
    message_id                                       varchar(100),
    system_code                                      varchar(100),
    rental_income_on_balance                         numeric(20, 2) default 0,
    rental_income_off_balance                        numeric(20, 2) default 0,
    allocation_method                                varchar(100),
    allocation_ratio                                 numeric(24, 6),
    service_fee_received                             numeric(20, 2),
    service_fee_allocation_no_tax                    numeric(20, 2),
    last_month_service_fee_allocation_no_tax         numeric(20, 2),
    reclassification_adjustment_no_tax_amount        numeric(20, 2),
    x_year_month_adjustment_amount                   numeric(20, 2),
    not_accrued_amount                               numeric(20, 2),
    accumulated_accrued_amount                       numeric(20, 2),
    accumulated_actual_repayment_interes_amount      numeric(20, 2),
    paid_in_handling_fees_add_other_income_sub_costs numeric(20, 2),
    ta_reclassification                              numeric(20, 2),
    unrealized_revenue                               numeric(20, 2),
    previous_paid_period                             timestamp,
    rental_income_before_total                       numeric(20, 2),
    rental_income_after_total                        numeric(20, 2),
    overdue_adjustment_amount                        numeric(20, 2),
    total_recorded_amount                            numeric(20, 2),
    confirmed_actual_receipt                         numeric(20, 2),
    accrued                                          char           default '0'::bpchar,
    income_provision_method                          varchar(10),
    exception_type                                   varchar(200),
    rental_income_on_balance_confirmed               numeric(20, 2),
    rental_income_off_balance_confirmed              numeric(20, 2),
    actual_repayment_rent_amount                     numeric(20, 2),
    labor_overdue_days                               integer,
    before_x_year_month_amount                       numeric(20, 2),
    allocation_after_x_year_month_balance            numeric(20, 2),
    org_id                                           varchar(100),
    labor_overdue_mark                               varchar(100),
    process_method                                   varchar(100),
    observed                                         char,
    observed_expiration_date                         timestamp,
    last_repayment_date                              timestamp,
    next_payment_date                                timestamp,
    confirmed_income                                 numeric(20, 2),
    paid_handling_fees                               numeric(20, 2),
    other_income                                     numeric(20, 2),
    paid_other_costs                                 numeric(20, 2),
    invoicing_flag                                   varchar(10),
    manual_change_mark                               varchar(10),
    plan_date_period                                 integer,
    amortized                                        char,
    on_and_off_balance_sheet                         char,
    source_irr_rate                                  numeric(20, 6),
    ebank_serial_number                              varchar(100),
    settlement_way                                   varchar(100),
    adjustment_amount                                numeric(20, 2),
    change_after_ending_amortized_cost               numeric(20, 2),
    change_after_rental_income                       numeric(20, 2)
);

comment on column eg_repayment_plan_snapshot.adjustment_amount is '租赁收入调整额';

comment on column eg_repayment_plan_snapshot.change_after_ending_amortized_cost is '变更后期末摊余成本';

comment on column eg_repayment_plan_snapshot.change_after_rental_income is '变更后租赁收入';

create index eg_repayment_plan_snapshot_ix1
    on eg_repayment_plan_snapshot (contract_code, del_flag);

alter table eg_service_fee
    add voucher_status varchar(5);

comment on column eg_service_fee.voucher_status is '凭证状态';


alter table eg_service_fee_details
    add service_fee_received_tax_included decimal(19, 6);
alter table eg_service_fee_details
    add service_fee_allocation_tax_included decimal(19, 6);
alter table eg_service_fee_details
    add last_month_service_fee_allocation_tax_included decimal(19, 6);
alter table eg_service_fee_details
    add reclassification_adjustment_tax_included decimal(19, 6);
alter table eg_service_fee_details
    add before_this_month_amount_tax_included decimal(19, 6);
alter table eg_service_fee_details
    add this_month_adjustment_amount_tax_included decimal(19, 6);
alter table eg_service_fee_details
    add allocation_before_this_month_balance_tax_included decimal(19, 6);
alter table eg_service_fee_details
    add before_this_month_amount_no_tax decimal(19, 6);
alter table eg_service_fee_details
    add this_month_adjustment_amount_no_tax decimal(19, 6);
alter table eg_service_fee_details
    add allocation_before_this_month_balance_no_tax decimal(19, 6);

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







create table eg_tdad_contract_balance_temp
(
    contract_code                       varchar(100),
    client_code                         varchar(100),
    org_id                              varchar(100),
    business_code                       varchar(100),
    payable_agency_estimate_balance     decimal(19, 6) default 0,
    payable_band_cost_estimate_balance  decimal(19, 6) default 0,
    payable_device_estimate_balance     decimal(19, 6) default 0,
    payable_other_cost_estimate_balance decimal(19, 6) default 0,
    payable_other_estimate_balance      decimal(19, 6) default 0,
    payable_pledge_estimate_balance     decimal(19, 6) default 0,
    payable_unpledge_estimate_balance   decimal(19, 6) default 0,
    payable_vehicle_estimate_balance    decimal(19, 6) default 0,
    receivable_outtax_balance           decimal(19, 6) default 0,
    receivable_service_outtax_balance   decimal(19, 6) default 0,
    receive_sum_outtax_balance          decimal(19, 6) default 0,
    receive_unrealized_revenue_balance  decimal(19, 6) default 0,
    unrealized_revenue_balance          decimal(19, 6) default 0,
    receivable_rent_balance             decimal(19, 6) default 0,
    receivable_residual_value_balance   decimal(19, 6) default 0,
    payable_device_balance              decimal(19, 6) default 0,
    payable_other_balance               decimal(19, 6) default 0
);

comment
    on table eg_tdad_contract_balance_temp is '尾差调整合同余额临时表';

CREATE INDEX idx_eg_tdad_contract_balance_temp ON eg_tdad_contract_balance_temp (org_id, contract_code, business_code);



