create table eg_tdad_contract_balance_temp
(
    contract_code                       varchar(100),
    client_code                         varchar(100),
    org_id                              varchar(100),
    business_code                       varchar(100),
    payable_agency_estimate_balance     numeric(19, 6) default 0,
    payable_band_cost_estimate_balance  numeric(19, 6) default 0,
    payable_device_estimate_balance     numeric(19, 6) default 0,
    payable_other_cost_estimate_balance numeric(19, 6) default 0,
    payable_other_estimate_balance      numeric(19, 6) default 0,
    payable_pledge_estimate_balance     numeric(19, 6) default 0,
    payable_unpledge_estimate_balance   numeric(19, 6) default 0,
    payable_vehicle_estimate_balance    numeric(19, 6) default 0,
    receivable_outtax_balance           numeric(19, 6) default 0,
    receivable_service_outtax_balance   numeric(19, 6) default 0,
    receive_sum_outtax_balance          numeric(19, 6) default 0,
    receive_unrealized_revenue_balance  numeric(19, 6) default 0,
    unrealized_revenue_balance          numeric(19, 6) default 0,
    receivable_rent_balance             numeric(19, 6) default 0,
    receivable_residual_value_balance   numeric(19, 6) default 0,
    payable_device_balance              numeric(19, 6) default 0,
    payable_other_balance               numeric(19, 6) default 0
);

comment
on table eg_tdad_contract_balance_temp is '尾差调整合同余额临时表';

alter table eg_tdad_contract_balance_temp
    owner to app_financialdb3;

create index idx_eg_tdad_contract_balance_temp
    on eg_tdad_contract_balance_temp (org_id, contract_code, business_code);

create table eg_tdad_contract_balance_temp
(
    contract_code                       varchar(100),
    client_code                         varchar(100),
    org_id                              varchar(100),
    business_code                       varchar(100),
    payable_agency_estimate_balance     numeric(19, 6) default 0,
    payable_band_cost_estimate_balance  numeric(19, 6) default 0,
    payable_device_estimate_balance     numeric(19, 6) default 0,
    payable_other_cost_estimate_balance numeric(19, 6) default 0,
    payable_other_estimate_balance      numeric(19, 6) default 0,
    payable_pledge_estimate_balance     numeric(19, 6) default 0,
    payable_unpledge_estimate_balance   numeric(19, 6) default 0,
    payable_vehicle_estimate_balance    numeric(19, 6) default 0,
    receivable_outtax_balance           numeric(19, 6) default 0,
    receivable_service_outtax_balance   numeric(19, 6) default 0,
    receive_sum_outtax_balance          numeric(19, 6) default 0,
    receive_unrealized_revenue_balance  numeric(19, 6) default 0,
    unrealized_revenue_balance          numeric(19, 6) default 0,
    receivable_rent_balance             numeric(19, 6) default 0,
    receivable_residual_value_balance   numeric(19, 6) default 0,
    payable_device_balance              numeric(19, 6) default 0,
    payable_other_balance               numeric(19, 6) default 0
);

comment
on table eg_tdad_contract_balance_temp is '尾差调整合同余额临时表';

alter table eg_tdad_contract_balance_temp
    owner to app_financialdb3;

create index idx_eg_tdad_contract_balance_temp
    on eg_tdad_contract_balance_temp (org_id, contract_code, business_code);

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES (0, 'L19A1757001-1,L19A1787001-1,L20A0142001,L20A0140002,L16A1928001,L23A1989007,L20A1433001- 1,L20A1434001-1,L18A1553,L18A1557,L18A1644,VL05Z0001,VL05Z0001', 'JYZL', 'special_zyzl_contract', null, null, 'N', '0', 'system', '2023-11-22 17:31:23.290000', 'system', '2023-11-22 17:31:23.290000', null);

alter table eg_tail_difference_adjustment_detail
    add lease_date_end timestamp;

comment on column eg_tail_difference_adjustment_detail.lease_date_end is '约定到期日';

alter table eg_tail_difference_adjustment_detail
    add tax_rate decimal(10, 6);

comment on column eg_tail_difference_adjustment_detail.tax_rate is '税率';

alter table eg_tail_difference_adjustment_detail
    add receive_sum decimal(15, 6);

comment on column eg_tail_difference_adjustment_detail.receive_sum is '应收总额';

alter table eg_tdad_contract_balance_temp
    add receive_sum_balance numeric(19, 6);

comment on column eg_tdad_contract_balance_temp.receive_sum_balance is '应收总额';



