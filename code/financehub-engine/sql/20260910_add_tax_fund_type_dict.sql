-- 税号配置新增金额类型的中文数据字典。
-- 使用 NOT EXISTS 保证脚本可以重复执行。

INSERT INTO financehub_lease.sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, css_class, list_class,
   is_default, status, create_by, create_time, update_by, update_time, remark)
SELECT 206, '印花税', 'stamp_duty', 'sys_cash_type', '', '',
       'N', '0', 'tax-config', NOW(), 'tax-config', NOW(), '税号配置金额类型'
WHERE NOT EXISTS (
  SELECT 1 FROM financehub_lease.sys_dict_data
  WHERE dict_type = 'sys_cash_type' AND dict_value = 'stamp_duty'
);

INSERT INTO financehub_lease.sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, css_class, list_class,
   is_default, status, create_by, create_time, update_by, update_time, remark)
SELECT 207, '通用税率', 'tax_general', 'sys_cash_type', '', '',
       'N', '0', 'tax-config', NOW(), 'tax-config', NOW(), '税号配置金额类型'
WHERE NOT EXISTS (
  SELECT 1 FROM financehub_lease.sys_dict_data
  WHERE dict_type = 'sys_cash_type' AND dict_value = 'tax_general'
);

INSERT INTO financialdb4.sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, css_class, list_class,
   is_default, status, create_by, create_time, update_by, update_time, remark)
SELECT 206, '印花税', 'stamp_duty', 'sys_cash_type', '', '',
       'N', '0', 'tax-config', NOW(), 'tax-config', NOW(), '税号配置金额类型'
WHERE NOT EXISTS (
  SELECT 1 FROM financialdb4.sys_dict_data
  WHERE dict_type = 'sys_cash_type' AND dict_value = 'stamp_duty'
);

INSERT INTO financialdb4.sys_dict_data
  (dict_sort, dict_label, dict_value, dict_type, css_class, list_class,
   is_default, status, create_by, create_time, update_by, update_time, remark)
SELECT 207, '通用税率', 'tax_general', 'sys_cash_type', '', '',
       'N', '0', 'tax-config', NOW(), 'tax-config', NOW(), '税号配置金额类型'
WHERE NOT EXISTS (
  SELECT 1 FROM financialdb4.sys_dict_data
  WHERE dict_type = 'sys_cash_type' AND dict_value = 'tax_general'
);
