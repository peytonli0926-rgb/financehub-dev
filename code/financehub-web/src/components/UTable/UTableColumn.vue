<template>
  <template v-if="columns.prop === 'operation' && !columns.hide">
    <el-table-column :prop="columns.prop" :label="columns.label" :align="columns.align" :fixed="columns.fixed"
      :width="columns.width">
      <template v-slot="{ row, $index }">
        <template v-if="columns.slot">
          <slot :name="columns.prop" :row="row" :index="$index"></slot>
        </template>
        <template v-else>
          <el-button link type="primary" v-permission="permission.edit" @click="rowOperateion('EDIT', row)" v-if="isEditDisabled(row)">{{
            operateBtn.editBtn.label || '编辑' }}</el-button>
          <el-button link type="danger" v-permission="permission.delete" @click="rowOperateion('DEL', row)" v-if="operateBtn.delBtn.isShow">{{
            operateBtn.delBtn.label || '删除' }}</el-button>
          <slot name="operationBtn" :row="row" />
        </template>
      </template>
    </el-table-column>
  </template>
  <template v-else-if="columns.children && columns.children.length > 0">
    <el-table-column :key="columns.prop" :prop="columns.prop" :label="columns.label" :align="columns.align"
      :fixed="columns.fixed" :width="columns.width">
      <template v-for="item of columns.children">
        <UTableColumn  :permission="permission" @icon-events="iconEvents" :key="item.prop" :columns="item"
          v-if="item.children && item.children.length > 0">
          <template v-for="(item, key, index) in $slots" :key="index" #[key]="propsData">
            <slot :name="key" v-bind="propsData"></slot>
          </template>
        </UTableColumn>
        <UTableColumnItem :permission="permission"  @icon-events="iconEvents" v-else :column="item" :key="item.prop + 1">
          <template v-for="(item, key, index) in $slots" :key="index" #[key]="propsData">
            <slot :name="key" v-bind="propsData"></slot>
          </template>
        </UTableColumnItem>
      </template>
    </el-table-column>

  </template>
  <template v-else>
    <UTableColumnItem :permission="permission" @icon-events="iconEvents" :column="columns">
      <template v-for="(item, key, index) in $slots" :key="index" #[key]="propsData">
        <slot :name="key" v-bind="propsData"></slot>
      </template>
    </UTableColumnItem>
  </template>
</template>
<script setup>
import UTableColumnItem from '@/components/UTable/UTableColumnItem.vue'
const emit = defineEmits(['icon-events', 'row-operateion'])
const props = defineProps({
  columns: {
    type: Object,
    default: () => { }
  },
  operateBtn: {
    type: Object,
    default: () => { }
  },
  permission: {
    type: Object,
    default: () => { }
  }
})

const isEditDisabled = (row) => {
  const { editBtn } = props.operateBtn
  let loop = false
  if (editBtn.isShow) {
    if (typeof (editBtn.condition) === 'function') {
      loop = editBtn.condition(row)
    } else {
      loop = editBtn.isShow
    }
  }

  return loop
}

const iconEvents = (op) => {
  emit('icon-events', op)
}
const rowOperateion = (type, row) => {
  emit('row-operateion', type, row)
}

</script>

<style lang="scss" scoped></style>
