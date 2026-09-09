-- Re-enable the original tail-difference adjustment feature for the MySQL runtime.

CREATE TABLE IF NOT EXISTS eg_tail_difference_adjustment (
    id BIGINT NOT NULL,
    org_id VARCHAR(100) NOT NULL,
    account_code VARCHAR(100) NOT NULL,
    account_name VARCHAR(200),
    business_date DATETIME,
    account_date DATETIME,
    process_instance_id BIGINT,
    process_status VARCHAR(100),
    del_flag CHAR(1) DEFAULT '0',
    create_by VARCHAR(32),
    create_time DATETIME,
    update_by VARCHAR(32),
    update_time DATETIME,
    approve_error_info VARCHAR(2000),
    is_generate_voucher CHAR(1) DEFAULT '0',
    PRIMARY KEY (id),
    INDEX idx_tail_adjust_query (org_id, account_code, business_date, process_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='尾差调整';

CREATE TABLE IF NOT EXISTS eg_tail_difference_adjustment_detail (
    id BIGINT NOT NULL AUTO_INCREMENT,
    contract_code VARCHAR(100) NOT NULL,
    org_id VARCHAR(100) NOT NULL,
    account_code VARCHAR(100) NOT NULL,
    account_name VARCHAR(200),
    business_date DATETIME,
    account_date DATETIME,
    account_balance DECIMAL(20,6) DEFAULT 0,
    tail_difference_adjustment_id BIGINT,
    del_flag CHAR(1) DEFAULT '0',
    create_by VARCHAR(32),
    create_time DATETIME,
    update_by VARCHAR(32),
    update_time DATETIME,
    voucher_ids VARCHAR(2000),
    receivable_rent DECIMAL(20,6) DEFAULT 0,
    receivable_residual_value DECIMAL(20,6) DEFAULT 0,
    payable_device DECIMAL(20,6) DEFAULT 0,
    payable_other DECIMAL(20,6) DEFAULT 0,
    contract_status VARCHAR(100),
    rental_income_after_total DECIMAL(20,6) DEFAULT 0,
    rental_income_after_lease_total DECIMAL(20,6) DEFAULT 0,
    overdue_days INT,
    client_code VARCHAR(100),
    error_info VARCHAR(2000),
    business_code VARCHAR(100),
    lease_date_end DATETIME,
    tax_rate DECIMAL(10,6),
    receive_sum DECIMAL(20,6) DEFAULT 0,
    PRIMARY KEY (id),
    INDEX idx_tail_adjust_detail_contract (contract_code),
    INDEX idx_tail_adjust_detail_parent (tail_difference_adjustment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='尾差调整详情';

CREATE TABLE IF NOT EXISTS eg_tdad_contract_balance_temp (
    contract_code VARCHAR(100),
    client_code VARCHAR(100),
    org_id VARCHAR(100),
    business_code VARCHAR(100),
    payable_agency_estimate_balance DECIMAL(19,6) DEFAULT 0,
    payable_band_cost_estimate_balance DECIMAL(19,6) DEFAULT 0,
    payable_device_estimate_balance DECIMAL(19,6) DEFAULT 0,
    payable_other_cost_estimate_balance DECIMAL(19,6) DEFAULT 0,
    payable_other_estimate_balance DECIMAL(19,6) DEFAULT 0,
    payable_pledge_estimate_balance DECIMAL(19,6) DEFAULT 0,
    payable_unpledge_estimate_balance DECIMAL(19,6) DEFAULT 0,
    payable_vehicle_estimate_balance DECIMAL(19,6) DEFAULT 0,
    receivable_outtax_balance DECIMAL(19,6) DEFAULT 0,
    receivable_service_outtax_balance DECIMAL(19,6) DEFAULT 0,
    receive_sum_outtax_balance DECIMAL(19,6) DEFAULT 0,
    receive_unrealized_revenue_balance DECIMAL(19,6) DEFAULT 0,
    unrealized_revenue_balance DECIMAL(19,6) DEFAULT 0,
    receivable_rent_balance DECIMAL(19,6) DEFAULT 0,
    receivable_residual_value_balance DECIMAL(19,6) DEFAULT 0,
    payable_device_balance DECIMAL(19,6) DEFAULT 0,
    payable_other_balance DECIMAL(19,6) DEFAULT 0,
    receive_sum_balance DECIMAL(19,6) DEFAULT 0,
    INDEX idx_tdad_balance (org_id, contract_code, business_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='尾差调整合同余额临时表';

UPDATE eg_scene
SET enable_flag = '1', del_flag = '0', update_time = CURRENT_TIMESTAMP
WHERE scene_code = 'WCTZ';

UPDATE eg_scene_fields
SET del_flag = '0', update_time = CURRENT_TIMESTAMP
WHERE scene_id = (SELECT id FROM eg_scene WHERE scene_code = 'WCTZ' LIMIT 1);

UPDATE eg_scene_voucher
SET del_flag = '0', update_time = CURRENT_TIMESTAMP
WHERE scene_id = (SELECT id FROM eg_scene WHERE scene_code = 'WCTZ' LIMIT 1);

UPDATE eg_scene_voucher_entry e
JOIN eg_scene_voucher v ON v.id = e.scene_voucher_id
SET e.del_flag = '0', e.update_time = CURRENT_TIMESTAMP
WHERE v.scene_id = (SELECT id FROM eg_scene WHERE scene_code = 'WCTZ' LIMIT 1);

UPDATE eg_scene_voucher_condition c
JOIN eg_scene_voucher_entry e ON e.id = c.scene_voucher_entry_id
JOIN eg_scene_voucher v ON v.id = e.scene_voucher_id
SET c.del_flag = '0', c.update_time = CURRENT_TIMESTAMP
WHERE v.scene_id = (SELECT id FROM eg_scene WHERE scene_code = 'WCTZ' LIMIT 1);
