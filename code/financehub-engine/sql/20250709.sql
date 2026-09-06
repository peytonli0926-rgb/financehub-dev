UPDATE eg_account
SET business_code = 'ZLYW',
    business_name = '租赁业务',
    fund_type     = 'receivable_unconfirm_receipt_temp'
WHERE account_code = '2241.18';

alter table eg_contract_balance
    add receivable_unconfirm_receipt_temp_amount numeric(20, 2);
alter table eg_contract_balance
    add receivable_unconfirm_receipt_temp_balance numeric(20, 2);
comment on column eg_contract_balance.receivable_unconfirm_receipt_temp_amount is '暂收未确认收款发生额';
comment on column eg_contract_balance.receivable_unconfirm_receipt_temp_balance is '暂收未确认收款余额';

alter table eg_contract_balance_latest
    add receivable_unconfirm_receipt_temp_amount numeric(20, 2);
alter table eg_contract_balance_latest
    add receivable_unconfirm_receipt_temp_balance numeric(20, 2);

comment on column eg_contract_balance_latest.receivable_unconfirm_receipt_temp_amount is '暂收未确认收款发生额';
comment on column eg_contract_balance_latest.receivable_unconfirm_receipt_temp_balance is '暂收未确认收款余额';

alter table eg_contract_balance_month
    add receivable_unconfirm_receipt_temp_amount numeric(20, 2);
alter table eg_contract_balance_month
    add receivable_unconfirm_receipt_temp_balance numeric(20, 2);

comment on column eg_contract_balance_month.receivable_unconfirm_receipt_temp_amount is '暂收未确认收款发生额';
comment on column eg_contract_balance_month.receivable_unconfirm_receipt_temp_balance is '暂收未确认收款余额';

alter table eg_contract_balance_temp
    add receivable_unconfirm_receipt_temp_amount numeric(20, 2);
alter table eg_contract_balance_temp
    add receivable_unconfirm_receipt_temp_balance numeric(20, 2);

comment on column eg_contract_balance_temp.receivable_unconfirm_receipt_temp_amount is '暂收未确认收款发生额';
comment on column eg_contract_balance_temp.receivable_unconfirm_receipt_temp_balance is '暂收未确认收款余额';

alter table eg_contract_balance_latest
    rename column other_income__balance to other_income_balance;

alter table eg_contract_balance_month
    add other_income_balance numeric(20, 2);
alter table eg_contract_balance_month
    add other_income_amount numeric(20, 2);

comment on column eg_contract_balance_month.other_income_balance is '其他营业外收入余额';
comment on column eg_contract_balance_month.other_income_amount is '其他营业外收入发生额';

alter table eg_contract_balance_temp
    add other_income_balance numeric(20, 2);
alter table eg_contract_balance_temp
    add other_income_amount numeric(20, 2);

comment on column eg_contract_balance_temp.other_income_balance is '其他营业外收入余额';
comment on column eg_contract_balance_temp.other_income_amount is '其他营业外收入发生额';


