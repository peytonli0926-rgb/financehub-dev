--生产已执行 start
ALTER TABLE eg_voucher_entry ADD eas_voucher_id varchar(50) NULL;
COMMENT ON COLUMN eg_voucher_entry.eas_voucher_id IS '金蝶凭证ID';

ALTER TABLE eg_voucher ADD eas_voucher_id varchar(50) NULL;
COMMENT ON COLUMN eg_voucher.eas_voucher_id IS '金蝶凭证ID';

ALTER TABLE eg_contract_balance ADD eas_voucher_id varchar(50) NULL;
COMMENT ON COLUMN eg_contract_balance.eas_voucher_id IS '金蝶凭证ID';

ALTER TABLE eg_contract_balance_latest ADD eas_voucher_id varchar(50) NULL;
COMMENT ON COLUMN eg_contract_balance_latest.eas_voucher_id IS '金蝶凭证ID';

CREATE INDEX eg_voucher_entry_eas_voucher_id_idx ON eg_voucher_entry (eas_voucher_id);
CREATE INDEX eg_voucher_eas_voucher_id_idx ON eg_voucher (eas_voucher_id);
CREATE INDEX eg_contract_balance_eas_voucher_id_idx ON eg_contract_balance (eas_voucher_id);
CREATE INDEX eg_contract_balance_latest_eas_voucher_id_idx ON eg_contract_balance_latest (eas_voucher_id);
--生产已执行 end

