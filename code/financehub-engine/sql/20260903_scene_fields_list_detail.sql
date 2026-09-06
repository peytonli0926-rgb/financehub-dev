ALTER TABLE eg_scene_fields
    ADD COLUMN parent_id BIGINT NULL COMMENT '父字段ID，List明细字段使用',
    ADD COLUMN required_flag CHAR(1) NOT NULL DEFAULT '0' COMMENT '是否必填(0否1是)',
    ADD COLUMN sort_no INT NOT NULL DEFAULT 0 COMMENT '字段排序';

CREATE INDEX idx_scene_fields_parent_id ON eg_scene_fields(parent_id);
