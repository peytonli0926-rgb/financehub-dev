-- Normalize Huaxia retail leaseback mappings and voucher predicates.
-- CRxxx codes belong to the legacy Kingdee configuration and must not be
-- exposed as source values or used as internal/template decision codes.

UPDATE eg_field_mapping
SET system_code = 'RETAIL_FINANCE_LEASE',
    update_by = 'retail-source-normalization',
    update_time = NOW()
WHERE del_flag = '0'
  AND system_code = 'CYCXT'
  AND (create_by LIKE 'retail-%' OR create_by = 'retail-lifecycle-demo');

UPDATE eg_field_mapping
SET source_value = TRIM(BOTH ',' FROM REGEXP_REPLACE(source_value, ',CR[0-9]+#?', '')),
    target_value = REGEXP_REPLACE(target_value, '^CR([0-9]+)$', 'C$1'),
    update_by = 'retail-source-normalization',
    update_time = NOW()
WHERE del_flag = '0'
  AND system_code = 'RETAIL_FINANCE_LEASE'
  AND (source_value REGEXP 'CR[0-9]' OR target_value REGEXP '^CR[0-9]+$');

UPDATE eg_scene_voucher
SET script_condition = REGEXP_REPLACE(script_condition, 'CR([0-9]+)', 'C$1'),
    update_by = 'retail-source-normalization',
    update_time = NOW()
WHERE del_flag = '0'
  AND script_condition REGEXP 'CR[0-9]';

UPDATE eg_scene_voucher_condition
SET script_condition = REGEXP_REPLACE(script_condition, 'CR([0-9]+)', 'C$1'),
    dondition_description = REGEXP_REPLACE(dondition_description, 'CR([0-9]+)', 'C$1'),
    update_by = 'retail-source-normalization',
    update_time = NOW()
WHERE del_flag = '0'
  AND (script_condition REGEXP 'CR[0-9]' OR dondition_description REGEXP 'CR[0-9]');
