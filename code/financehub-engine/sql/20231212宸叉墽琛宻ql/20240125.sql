--最新余额表新增空值索引
CREATE INDEX client_codex_null_values ON eg_contract_balance_latest (client_code) WHERE client_code IS NULL;

CREATE INDEX contract_codex_null_values ON eg_contract_balance_latest (contract_code) WHERE contract_code IS NULL;

CREATE INDEX bill_contract_codex_null_values ON eg_contract_balance_latest (bill_contract_code) WHERE bill_contract_code IS NULL;