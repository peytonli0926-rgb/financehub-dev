-- 华夏金租金额类型余额宽表重建脚本（一次性执行）
-- 口径：sys_dict_data.sys_cash_type 中 status='0' 的金额类型。
-- 每个金额类型生成 {dict_value}_amount 和 {dict_value}_balance 两列。

SET SESSION group_concat_max_len = 1024 * 1024;

CREATE TABLE eg_contract_balance_bak_20260904_amount_rebuild LIKE eg_contract_balance;
INSERT INTO eg_contract_balance_bak_20260904_amount_rebuild SELECT * FROM eg_contract_balance;

CREATE TABLE eg_contract_balance_latest_bak_20260904_amount_rebuild LIKE eg_contract_balance_latest;
INSERT INTO eg_contract_balance_latest_bak_20260904_amount_rebuild SELECT * FROM eg_contract_balance_latest;

CREATE TABLE eg_contract_balance_temp_bak_20260904_amount_rebuild LIKE eg_contract_balance_temp;
INSERT INTO eg_contract_balance_temp_bak_20260904_amount_rebuild SELECT * FROM eg_contract_balance_temp;

CREATE TABLE eg_balance_bak_20260904_amount_rebuild LIKE eg_balance;
INSERT INTO eg_balance_bak_20260904_amount_rebuild SELECT * FROM eg_balance;

DELIMITER $$

CREATE PROCEDURE rebuild_amount_columns(IN target_table VARCHAR(64))
BEGIN
    SELECT GROUP_CONCAT(CONCAT('DROP COLUMN `', column_name, '`') ORDER BY ordinal_position SEPARATOR ',')
      INTO @drop_columns
      FROM information_schema.columns
     WHERE table_schema = DATABASE()
       AND table_name = target_table
       AND (column_name LIKE '%\\_amount' OR column_name LIKE '%\\_balance');

    IF @drop_columns IS NOT NULL THEN
        SET @ddl = CONCAT('ALTER TABLE `', target_table, '` ', @drop_columns);
        PREPARE ddl_statement FROM @ddl;
        EXECUTE ddl_statement;
        DEALLOCATE PREPARE ddl_statement;
    END IF;

    SELECT GROUP_CONCAT(
               CONCAT(
                   'ADD COLUMN `', dict_value, '_amount` DECIMAL(20,2) NOT NULL DEFAULT 0 COMMENT ''发生额'',',
                   'ADD COLUMN `', dict_value, '_balance` DECIMAL(20,2) NOT NULL DEFAULT 0 COMMENT ''余额'''
               )
               ORDER BY dict_sort, dict_code SEPARATOR ','
           )
      INTO @add_columns
      FROM sys_dict_data
     WHERE dict_type = 'sys_cash_type'
       AND status = '0';

    SET @ddl = CONCAT('ALTER TABLE `', target_table, '` ', @add_columns);
    PREPARE ddl_statement FROM @ddl;
    EXECUTE ddl_statement;
    DEALLOCATE PREPARE ddl_statement;
END$$

DELIMITER ;

CALL rebuild_amount_columns('eg_contract_balance');
CALL rebuild_amount_columns('eg_contract_balance_latest');
CALL rebuild_amount_columns('eg_contract_balance_temp');
DROP PROCEDURE rebuild_amount_columns;

DROP TABLE eg_balance;

-- 停用仍引用旧金额类型的科目配置。
UPDATE eg_account a
LEFT JOIN sys_dict_data d
       ON d.dict_type = 'sys_cash_type'
      AND d.status = '0'
      AND d.dict_value = a.fund_type
SET a.del_flag = '1',
    a.update_time = NOW()
WHERE a.del_flag = '0'
  AND d.dict_value IS NULL;
