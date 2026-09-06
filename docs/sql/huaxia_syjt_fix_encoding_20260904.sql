-- Repair the Huaxia SYJT template without relying on the Windows console code page.
SET NAMES utf8mb4;

SET @scene_id = 1722178556031397889;
SET @voucher_id = 2096000100000000001;
SET @old_voucher_id = 1797465340633604098;

SET @table_ref = SUBSTRING_INDEX(
    (SELECT source FROM eg_scene_voucher WHERE id = @old_voucher_id), '.', 1
);
SET @field_accrued = (SELECT field_name FROM eg_scene_fields WHERE scene_id = @scene_id AND field_code = 'isAccrual' LIMIT 1);
SET @field_income = (SELECT field_name FROM eg_scene_fields WHERE scene_id = @scene_id AND field_code = 'incomeAccural' LIMIT 1);
SET @field_off_income = (SELECT field_name FROM eg_scene_fields WHERE scene_id = @scene_id AND field_code = 'incomeOther' LIMIT 1);
SET @field_in_to_off = (SELECT field_name FROM eg_scene_fields WHERE scene_id = @scene_id AND field_code = 'intableTransferOuttableAmount' LIMIT 1);
SET @field_off_to_in = (SELECT field_name FROM eg_scene_fields WHERE scene_id = @scene_id AND field_code = 'outtableTransferIntableAmount' LIMIT 1);

SET @ref_accrued = CONCAT(@table_ref, '.', @field_accrued, '}');
SET @ref_income = CONCAT(@table_ref, '.', @field_income, '}');
SET @ref_off_income = CONCAT(@table_ref, '.', @field_off_income, '}');
SET @ref_in_to_off = CONCAT(@table_ref, '.', @field_in_to_off, '}');
SET @ref_off_to_in = CONCAT(@table_ref, '.', @field_off_to_in, '}');
SET @yes_text = CONVERT(UNHEX('E698AF') USING utf8mb4);
SET @summary_text = CONVERT(UNHEX('E694B6E79B8AE8AEA1E68F90') USING utf8mb4);

UPDATE eg_scene
SET scene_name = CONVERT(UNHEX('E58D8EE5A48FE98791E7A79FE694B6E79B8AE8AEA1E68F90') USING utf8mb4), del_flag = '0'
WHERE id = @scene_id;

UPDATE eg_scene_voucher target
JOIN eg_scene_voucher mature ON mature.id = @old_voucher_id
SET target.source = mature.source,
    target.voucher_type = mature.voucher_type,
    target.company = mature.company,
    target.business_date = mature.business_date,
    target.voucher_date = mature.voucher_date,
    target.currency = mature.currency,
    target.dept_name = mature.dept_name,
    target.voucher_summary = CONCAT('''', @summary_text, ''''),
    target.scene_voucher_name = CONVERT(UNHEX('E58D8EE5A48FE98791E7A79FE89E8DE8B584E7A79FE8B581E694B6E79B8AE8AEA1E68F90') USING utf8mb4),
    target.script_condition = CONCAT(
        @ref_accrued, '==''', @yes_text, '''&&(',
        @ref_income, '!=0||', @ref_off_income, '!=0||',
        @ref_in_to_off, '!=0||', @ref_off_to_in, '!=0)'
    ),
    target.del_flag = '0'
WHERE target.id = @voucher_id;

UPDATE eg_scene_voucher_entry
SET voucher_summary = CONCAT('''', @summary_text, ''''), del_flag = '0'
WHERE scene_voucher_id = @voucher_id;

UPDATE eg_scene_voucher_condition
SET script_condition = CASE
        WHEN scene_voucher_entry_id IN (2096000100000000011, 2096000100000000012) THEN CONCAT(@ref_income, '!=0')
        WHEN scene_voucher_entry_id IN (2096000100000000013, 2096000100000000014) THEN CONCAT(@ref_off_income, '!=0')
        WHEN scene_voucher_entry_id IN (2096000100000000015, 2096000100000000016, 2096000100000000017, 2096000100000000018) THEN CONCAT(@ref_in_to_off, '!=0')
        WHEN scene_voucher_entry_id IN (2096000100000000019, 2096000100000000020, 2096000100000000021, 2096000100000000022) THEN CONCAT(@ref_off_to_in, '!=0')
    END,
    script_amount = CASE
        WHEN scene_voucher_entry_id IN (2096000100000000011, 2096000100000000012) THEN @ref_income
        WHEN scene_voucher_entry_id IN (2096000100000000013, 2096000100000000014) THEN @ref_off_income
        WHEN scene_voucher_entry_id IN (2096000100000000015, 2096000100000000016, 2096000100000000017, 2096000100000000018) THEN @ref_in_to_off
        WHEN scene_voucher_entry_id IN (2096000100000000019, 2096000100000000020, 2096000100000000021, 2096000100000000022) THEN @ref_off_to_in
    END,
    amount_description = @summary_text,
    dondition_description = @summary_text,
    del_flag = '0'
WHERE scene_voucher_entry_id BETWEEN 2096000100000000011 AND 2096000100000000022;

-- Only the Huaxia template is active. Historical templates stay available in backups.
UPDATE eg_scene_voucher
SET del_flag = '1'
WHERE scene_id = @scene_id AND id <> @voucher_id;
UPDATE eg_scene_voucher_entry
SET del_flag = '1'
WHERE scene_voucher_id IN (SELECT id FROM eg_scene_voucher WHERE scene_id = @scene_id AND id <> @voucher_id);
UPDATE eg_scene_voucher_condition
SET del_flag = '1'
WHERE scene_voucher_entry_id IN (
    SELECT id FROM eg_scene_voucher_entry
    WHERE scene_voucher_id IN (SELECT id FROM eg_scene_voucher WHERE scene_id = @scene_id AND id <> @voucher_id)
);
