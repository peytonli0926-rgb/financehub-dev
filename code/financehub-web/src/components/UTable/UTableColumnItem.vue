<template>
  <el-table-column :prop="column.prop" :align="column.align" :fixed="column.fixed" :min-width="column.minWidth"
    :width="column.width" v-if="!column.hide">
    <template #header>
      <div class="u-flex u-row-between">
        <span> {{ column.label }} </span>
        <span v-if="column.icon" @click="iconEvents(column)">
          <el-tooltip effect="dark" :content="column.tips">
            <el-icon>
              <component :is="column.icon" />
            </el-icon>
          </el-tooltip>

        </span>
      </div>
    </template>
    <template v-slot="{ row, $index }">
      <template v-if="column.slot">
        <slot :name="column.prop" :row="row" :index="$index"></slot>
      </template>
      <template v-else-if="column.tooltip&&column.render">
          <el-tooltip v-if="column.tooltip" effect="dark" popper-class="custom-tooltip"
            :content="column.render(row, column)">
            <template #content>
              <span v-if="typeof column.render(row, column) === 'string'" v-html="column.render(row, column)"></span>
              <component v-else :is="column.render(row,column)"></component>
            </template>
          
            <div class="colum-style" :style="{ width:((column.width-20)+'px') }">
              <span v-if="typeof column.render(row, column) === 'string'" v-html="column.render(row, column)"></span>
              <component v-else :is="column.render(row,column)"></component>
            </div>
         
          </el-tooltip>
          <div v-else class="colum-style" :style="{ width:((column.width-20)+'px') }">
            <span v-if="typeof column.render(row, column) === 'string'" v-html="column.render(row, column)"></span>
            <component v-else :is="column.render(row,column)"></component>
          </div>
        </template>
        <template v-else-if="column.render">
          <div  class="colum-style" :style="{ width:((column.width-20)+'px') }">
            <span v-if="typeof column.render(row, column) === 'string'" v-html="column.render(row, column)"></span>
            <component v-else :is="column.render(row,column)"></component>
          </div>
        </template>
        <template v-else-if="column.tooltip">
          <el-tooltip  effect="dark" popper-class="custom-tooltip"
            :content="row[column.prop]">
            <div class="colum-style" :style="{ width:((column.width-20)+'px') }">
              {{ row[column.prop] }}
            </div>
          </el-tooltip>
      </template>
      <template v-else>
        {{ formatDateColumn(column,row) }}
      </template>
    </template>
  </el-table-column>
</template>

<script setup>
// import { parseTime } from '@/utils'
import moment from 'moment'
const emit = defineEmits(['icon-events'])
defineProps({
  column: {
    type: Object,
    default: () => { }
  }
})
//
const formatDateColumn = ({ prop, type, isFormat = true, attrs }, row) => {
  if (row[prop] === undefined || row[prop] == null) return ''
  const { format } = attrs || {}
  if (type === 'date' && isFormat) {
    return moment(row[prop]).format(format || 'YYYY-MM-DD')
  }
  return row[prop]
}
const iconEvents = (op) => {
  emit('icon-events', op)
}

</script>

<style lang="scss" scoped>
.colum-style {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
</style>
