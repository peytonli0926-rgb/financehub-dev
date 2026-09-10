-- CR005 业务事件名称纠错：保留旧名称作为兼容别名，新数据统一展示“收到并核销租金”。
UPDATE eg_field_mapping
SET field_name='收款业务事件',
    source_value='收到现金折扣,C000,CR000,收取保证金,收到保证金,C001,CR001,收到并核销租金,到并核销租金,收到租金,C005,CR005,款项无法确认,款项无法确认款,C006,CR006,人工明确款项用途,明确不明款项用途,C007,CR007,收到款项核销,款项核销,C008,CR008,退回的分润费,收到退回分润费,C033,CR033,前期逾期留购价,收到前期逾期留购价,C043,CR043',
    update_by='fix-collection-event-name',update_time=NOW()
WHERE id=202609075000001 AND del_flag='0';

UPDATE eg_field_mapping
SET field_name='收到并核销租金',
    source_value='收到并核销租金,到并核销租金,收到租金,C005,CR005',
    update_by='fix-collection-event-name',update_time=NOW()
WHERE id=202609075000004 AND del_flag='0';

-- 已有原始流水也同步纠错，否则合同生命周期仍会优先显示历史 JSON 中的旧名称。
UPDATE eg_raw_transaction_data
SET message_content=JSON_SET(
        message_content,
        '$.event_name',IF(JSON_UNQUOTE(JSON_EXTRACT(message_content,'$.event_name'))='到并核销租金','收到并核销租金',JSON_EXTRACT(message_content,'$.event_name')),
        '$.sourceEventCode',IF(JSON_UNQUOTE(JSON_EXTRACT(message_content,'$.sourceEventCode'))='到并核销租金','收到并核销租金',JSON_EXTRACT(message_content,'$.sourceEventCode')),
        '$.sourceEventOriginal',IF(JSON_UNQUOTE(JSON_EXTRACT(message_content,'$.sourceEventOriginal'))='到并核销租金','收到并核销租金',JSON_EXTRACT(message_content,'$.sourceEventOriginal'))
    ),
    update_by='fix-collection-event-name',update_time=NOW()
WHERE JSON_UNQUOTE(JSON_EXTRACT(message_content,'$.event_name'))='到并核销租金'
   OR JSON_UNQUOTE(JSON_EXTRACT(message_content,'$.sourceEventCode'))='到并核销租金'
   OR JSON_UNQUOTE(JSON_EXTRACT(message_content,'$.sourceEventOriginal'))='到并核销租金';

UPDATE eg_voucher
SET voucher_summary=REPLACE(voucher_summary,'到并核销租金','收到并核销租金'),
    update_by='fix-collection-event-name',update_time=NOW()
WHERE voucher_summary LIKE '%到并核销租金%';
