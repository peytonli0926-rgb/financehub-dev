<template>
  <el-popover placement="top-start" :width="500" trigger="click">
    <template #reference>
      <div class="w-100 popover-input">
        <el-input
          :placeholder="placeholder"
          @change="inputChange"
          class="w-100"
          v-model="showLabel"
          @clear="clear"
          clearable
          :suffix-icon="Search"
        >
        </el-input>
        <!-- <div class="multiple-tag">
      <el-tag size="small">Large</el-tag>
     </div> -->
      </div>
    </template>
    <template #default>
      <div class="u-p-10">
        <el-input :placeholder="placeholder" v-model="searchKey" clearable />
        <ul class="select-pagination" v-loading="loading">
          <li
            :class="`${selectedList.includes(item[keyValue.value || 'value']) ? 'active' : ''}`"
            v-for="item in options"
            :key="item[keyValue.value || 'value']"
            @click="selectedItem(item)"
          >
            <span v-if="keyValue.formatLabel">
              {{ item[keyValue.formatLabel[0]] }}
              <small>({{ item[keyValue.formatLabel[1]] }})</small>
            </span>
            <span v-else>{{
              item[keyValue.label || 'label'] || item[keyValue.value || 'value']
            }}</span>
            <el-icon v-if="selectedList.includes(item[keyValue.value || 'value'])"
              ><Select
            /></el-icon>
          </li>
        </ul>

        <div class="u-p-5 u-text-center">
          <el-pagination
            small
            v-model:current-page="pagination.pageNum"
            background
            layout="total, prev, pager, next,"
            :total="pagination.total"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </template>
  </el-popover>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { getCommonTableList } from '@/api/common'
import { ElMessage } from 'element-plus'
import { Search, Select } from '@element-plus/icons-vue'
import { debounceRef } from '@/utils'

const emit = defineEmits(['update:modelValue', 'change'])
const loading = ref(false)
const searchKey = debounceRef('', 100)
const options = ref([])
const selectedData = ref({})
const showLabel = ref()

// 分页参数
const pagination = ref({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const props = defineProps({
  modelValue: {
    type: [String, Array],
    default: () => ''
  },
  placeholder: {
    type: String,
    default: '请选择'
  },
  code: {
    type: String,
    default: ''
  },
  request: {
    type: Object,
    default: () => {}
  },
  attrs: {
    type: Object,
    default: () => ({})
  },
  keyValue: {
    type: Object,
    default: () => ({
      label: 'label',
      value: 'value'
    })
  },
  formData: {
    type: Object,
    default: () => ({})
  },
  searchName: {
    type: String,
    default: 'searchKey'
  }
})

const inputChange = (val) => {
  const { multiple } = props.attrs
  if (multiple) {
    emit('update:modelValue', val.split(','))
    emit('change', val.split(','))
  } else {
    emit('update:modelValue', val)
    emit('change', val)
    setTimeout(() => {
      changeSearchKey(val)
    }, 100)
  }
}

const selectedList = computed(() => {
  const { multiple } = props.attrs
  if (multiple) {
    return props.modelValue
  }
  return [props.modelValue]
})

const changeSearchKey = (val) => {
  customRequest({ [props.searchName]: val, pageNum: 1 })
}

const selectedItem = (item) => {
  const { label, value } = props.keyValue
  const { multiple } = props.attrs
  if (multiple) {
    if (!selectedList.value.includes(item[value])) {
      selectedData.value = {
        ...selectedData.value,
        [item[value]]: item
      }
      const keys = Object.keys(selectedData.value)
      showLabel.value = keys.map((it) => {
        return selectedData.value[it][label]
      })
      emit('update:modelValue', keys)
      emit('change', keys)
    } else {
      delete selectedData.value[item[value]]
      const keys = Object.keys(selectedData.value)
      showLabel.value = keys.map((it) => {
        return selectedData.value[it][label]
      })
      emit('update:modelValue', keys)
      emit('change', keys)
    }
  } else {
    selectedData.value = {
      [item[value]]: item
    }
    emit('update:modelValue', item[value])
    emit('change', item[value])
    showLabel.value = item[label] || item[value]
  }
}

const clear = () => {
  const { multiple } = props.attrs
  showLabel.value = ''
  selectedData.value = {}
  if (multiple) {
    emit('update:modelValue', [])
    emit('change', [])
    return
  }
  emit('update:modelValue', '')
  emit('change', '')
}

watch(
  () => props.modelValue,
  (n, o) => {
    // const { multiple } = props.attrs
    console.log(n)
    // if (multiple && n.length === 0) {
    // emit('update:modelValue', [])
    // emit('change', [])
    // } else {
    if (!Array.isArray(n)) {
      customRequest({ [props.searchName]: n })
    }
    // }
  }
)

const customRequest = async (queryData = {}) => {
  try {
    const { label, value } = props.keyValue
    loading.value = true
    let { url, method, params = {}, formKey } = props.request
    if (formKey) {
      params = { ...params, [formKey]: props.formData[formKey] }
    }
    const { data } = await getCommonTableList({
      url,
      method,
      params: { ...params, ...pagination.value, ...queryData }
    })
    if (data.records && data.records.length > 0) {
      const records = data.records
      const obj = records.find((item) => item[value] === props.modelValue)
      if (obj) {
        showLabel.value = obj[label]
      } else {
        showLabel.value = ''
      }

      options.value = records
      pagination.value = {
        ...pagination.value,
        total: data.total,
        pageNum: data.current,
        pageSize: data.size
      }
    } else {
      options.value = []
      pagination.value = {
        ...pagination.value,
        total: data.total,
        pageNum: data.current,
        pageSize: data.size
      }
    }

    loading.value = false
  } catch (error) {
    loading.value = false
    ElMessage.error('系统错误')
  }
}
// 分页
const handleCurrentChange = (page) => {
  pagination.value.pageNum = page
  customRequest({ [props.searchName]: searchKey.value })
}

const init = async () => {
  const { request, modelValue } = props
  if (Array.isArray(modelValue)) return
  if (Reflect.has(request || {}, 'url')) {
    customRequest({ [props.searchName]: modelValue })
    searchKey.value = modelValue
  }
  showLabel.value = modelValue
}

watch(
  () => props.request,
  (n, o) => {
    if (n.url !== o?.url) {
      init()
    }
  },
  { deep: true, immediate: true }
)
watch(() => searchKey.value, changeSearchKey)
</script>

<style lang="scss" scoped>
.select-pagination {
  max-height: 400px;
  padding: 10px;
  margin: 0;
  overflow: auto;
  list-style: none;
  cursor: pointer;

  li {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 5px 10px;
    margin-top: 2px;

    &:hover {
      background-color: #f5f7fa;
    }

    &.active {
      font-weight: bold;
      color: var(--el-color-primary);
      background-color: #f5f7fa;
    }
  }
}

.popover-input {
  position: relative;
}

.multiple-tag {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>
