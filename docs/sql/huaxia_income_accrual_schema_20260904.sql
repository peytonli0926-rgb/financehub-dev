-- Supporting tables/columns required by the mature income-accrual process.
ALTER TABLE eg_repayment_plan
    ADD COLUMN IF NOT EXISTS impairment_third_stage varchar(10) DEFAULT '0';

CREATE TABLE IF NOT EXISTS eg_repayment_plan_temp LIKE eg_repayment_plan;
CREATE TABLE IF NOT EXISTS eg_repayment_plan_provision LIKE eg_repayment_plan;

CREATE TABLE IF NOT EXISTS eg_outstanding_amount_init (
    id bigint NOT NULL PRIMARY KEY,
    contract_code varchar(100),
    account_number varchar(100),
    account_name varchar(200),
    client_code varchar(100),
    org_id varchar(100),
    end_balance_for decimal(20,2)
);

ALTER TABLE eg_lease_income_details
    ADD COLUMN IF NOT EXISTS cash_change decimal(20,2),
    ADD COLUMN IF NOT EXISTS lessor_other_costs decimal(20,2),
    ADD COLUMN IF NOT EXISTS enter_observe_period int,
    ADD COLUMN IF NOT EXISTS enter_observe_final_repayment_date datetime;

-- The mature process stores month-end as 23:59:59.999 and compares it exactly.
-- Millisecond precision avoids MySQL DATETIME rounding it into the next month.
ALTER TABLE eg_lease_income
    MODIFY COLUMN business_date datetime(3);
ALTER TABLE eg_lease_income_details
    MODIFY COLUMN business_date datetime(3),
    MODIFY COLUMN account_date datetime(3);

-- Periods use YYYYMM (for example 202609); the legacy tinyint column overflows.
ALTER TABLE eg_contract_balance_temp
    MODIFY COLUMN id bigint,
    MODIFY COLUMN voucher_id bigint,
    MODIFY COLUMN interface_data_id bigint,
    MODIFY COLUMN period_code int;
