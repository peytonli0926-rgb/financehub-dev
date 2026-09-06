<template>
  <div>
    <el-form
      ref="ruleFormRef"
      :model="searchForm"
      :label-width="labelWidth"
      :label-position="labelPosition"
      class="form-search"
    >
      <el-row :gutter="15">
        <template v-for="column of computedColCount" :key="column.prop">
          <el-col :span="column.span || span">
            <el-form-item
              :label="(formType === 'search' && column.alias) || column.label"
              :prop="column.prop"
              :rules="ruleSearchRequired(column)"
            >
              <USelect
                :form-data="searchForm"
                v-model="searchForm[column.prop]"
                :dict-type="column.dictType"
                :key-value="column.keyValue"
                :option="column.option"
                @change="changeOperation($event, column)"
                :request="column.request"
                :attrs="formItemDisabled(column.attrs)"
                :placeholder="`${column.placeholder || '请选择'}`"
                v-if="column.type === 'select'"
              />

              <el-date-picker
                style="width: 100%"
                v-model="searchForm[column.prop]"
                class="w-100"
                :type="column.pickerType"
                clearable
                v-else-if="column.type === 'date'"
                v-bind="formItemDisabled(column.attrs)"
                :placeholder="`${column.placeholder || '请选择'}`"
              />

              <el-input-number
                controls-position="right"
                v-model="searchForm[column.prop]"
                class="w-100"
                v-bind="formItemDisabled(column.attrs)"
                v-else-if="column.type === 'inputNumber'"
                :placeholder="`${column.placeholder || '请输入'}`"
                clearable
              />

              <URuleEditor
                v-else-if="column.type === 'editor'"
                :editor-id="column.prop"
                v-model="searchForm[column.prop]"
                v-bind="formItemDisabled(column.attrs)"
              />
              <USearchPerson
                v-else-if="column.type === 'person'"
                :attrs="formItemDisabled(column.attrs)"
                v-model="searchForm[column.prop]"
                :selectionType="column.selectionType"
              />
              <UDrawer
                :option="column.attrs"
                v-else-if="column.type === 'drawer'"
                v-model="searchForm[column.prop]"
              />

              <USelectPagination
                :form-data="searchForm"
                v-model="searchForm[column.prop]"
                :dict-type="column.dictType"
                :key-value="column.keyValue"
                :option="column.option"
                @change="changeOperation($event, column)"
                :request="column.request"
                :attrs="formItemDisabled(column.attrs)"
                :placeholder="`${column.placeholder || '请选择'}`"
                v-else-if="column.type === 'select-pagination'"
              />

              <UInputRange
                v-else-if="column.type === 'inputrange'"
                :attrs="formItemDisabled(column.attrs)"
                v-model="searchForm[column.prop]"
              />

              <UPeriodRange
                v-else-if="column.type === 'selectrange'"
                :attrs="formItemDisabled(column.attrs)"
                v-model="searchForm[column.prop]"
              />

              <el-input
                v-model="searchForm[column.prop]"
                v-bind="formItemDisabled(column.attrs)"
                v-else
                :placeholder="`${column.placeholder || '请输入'}`"
                clearable
              />
            </el-form-item>
          </el-col>
        </template>

        <el-col :span="operateCol" class="u-text-right" v-if="isSearchBtn">
          <!--  -->
          <el-button
            type="primary"
            link
            @click="showMoreFormItem"
            v-if="formAllColumnList.length > (span === 6 ? 7 : 5)"
          >
            {{ isShowMoreFormItem ? '展开' : '收缩' }}
            <el-icon v-if="isShowMoreFormItem">
              <ArrowDownBold />
            </el-icon>
            <el-icon v-else>
              <ArrowUpBold />
            </el-icon>
          </el-button>
          <el-button :icon="Refresh" @click="resetForm(ruleFormRef)" v-if="isResetBtn"
            >重置</el-button
          >
          <el-button
            v-if="searchBtnShow"
            :icon="Search"
            type="primary"
            @click="submitForm(ruleFormRef)"
            >搜索</el-button
          >
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import URuleEditor from '@/components/URuleEditor/index.vue'
import UDrawer from '@/components/UDrawer/index.vue'
import USearchPerson from '@/components/USearchPerson/index.vue'
import UInputRange from '@/components/UInputRange/index.vue'
import USelectPagination from '@/components/USelectPagination/index.vue'
import UPeriodRange from '@/components/UPeriodRange/index.vue'
import { ArrowUpBold, ArrowDownBold, Search, Refresh } from '@element-plus/icons-vue'
const ruleFormRef = ref()
const searchForm = ref({})
const formCountList = ref([])
const formAllColumnList = ref([])

const isShowMoreFormItem = ref(false)
const emit = defineEmits(['update:modelValue', 'search-submit'])
const props = defineProps({
  formType: {
    type: String,
    default: ''
  },
  modelValue: {
    type: Object,
    default: () => {}
  },
  isSearchBtn: {
    type: Boolean,
    default: true
  },
  labelPosition: {
    type: String,
    default: 'right'
  },
  labelWidth: {
    type: String,
    default: '120px'
  },
  span: {
    type: Number,
    default: 6
  },
  operateType: {
    type: String,
    default: ''
  },
  isResetBtn: {
    type: Boolean,
    default: true
  },
  searchBtnShow: {
    type: Boolean,
    default: true
  },
  columns: {
    type: Array,
    default: () => [],
    require: true
  } // 表格展示配置
})

// 计算搜索按钮的位置
const operateCol = computed(() => {
  const { span } = props
  const len = formCountList.value.length
  return 24 - (len % (24 / parseInt(span))) * (parseInt(span) || 8)
})

// 关联关系取值
const changeOperation = (item = {}, column) => {
  if (props.formType === 'search') return
  const { relation } = column
  if (!relation) return
  relation.map((it) => {
    if (typeof it === 'string') {
      searchForm.value[it] = item[it] || ''
    } else {
      searchForm.value[it.prop] = item[it.selectKey] || ''
    }
  })
}
// 设置查询条件展开收缩
const computedColCount = computed({
  get() {
    if (props.formType === 'search') {
      return formCountList.value
    } else {
      return formAllColumnList.value
    }
  },
  set(value) {
    formCountList.value = value
  }
})

// 展开收缩事件
const showMoreFormItem = () => {
  const _columns = formAllColumnList.value
  const sliceLen = 24 / props.span + (24 / props.span - 1)
  const newColumns = isShowMoreFormItem.value
    ? _columns
    : _columns.length > sliceLen
      ? _columns.slice(0, sliceLen)
      : _columns
  isShowMoreFormItem.value = !isShowMoreFormItem.value
  nextTick(() => {
    computedColCount.value = newColumns
  })
}
// 监听传过来的值
watch(
  () => props.columns,
  (n) => {
    formAllColumnList.value = n
  },
  {
    deep: true,
    immediate: true
  }
)
// 监听传过来的值
watch(
  () => props.modelValue,
  (n) => {
    searchForm.value = n
  },
  {
    deep: true,
    immediate: true
  }
)
// 监听输入值的问题
watch(
  () => searchForm.value,
  (n) => {
    emit('update:modelValue', n)
  },
  {
    deep: true,
    immediate: true
  }
)

// 判断是否需要校验表单
const ruleSearchRequired = ({ isSearchRequired = false, rules = [] }) => {
  let _rules = []
  if (props.formType === 'search') {
    if (isSearchRequired) {
      _rules = rules
    } else {
      _rules = []
    }
  } else {
    _rules = rules
  }

  return _rules
  //  ? (column.rules || []) : []
}

// 判断是否禁用
const formItemDisabled = (attrs = {}) => {
  const _attrs = Object.assign({}, attrs)
  if (props.formType === 'search') {
    _attrs.disabled = _attrs?.SEARCH ?? false
  } else {
    _attrs.disabled =
      _attrs[props.operateType] === undefined
        ? _attrs?.disabled ?? false
        : _attrs[props.operateType]
  }
  return _attrs
}

// 数据校验
const validateField = () => {
  return ruleFormRef.value.validateField()
}

// 提交数据
const submitForm = async (formEl) => {
  if (!formEl) return

  await formEl.validate(async (valid, fields) => {
    if (valid) {
      emit('search-submit', searchForm.value)
    } else {
      console.log('error submit!', fields)
    }
  })
}

// 重置表单
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
  emit('search-submit', searchForm.value)
}
onMounted(() => {
  setTimeout(() => {
    showMoreFormItem()
  }, 500)
})
defineExpose({
  submitForm,
  searchForm: searchForm.value,
  validateField
})
</script>

<style lang="scss" scoped>
:deep(.el-input__wrapper) {
  width: 93% !important;
}

.form-search {
  :deep(.el-form-item__label) {
    flex-wrap: wrap !important;
    align-items: center !important;
    line-height: 18px !important;
    text-align: right;
  }
}
</style>
