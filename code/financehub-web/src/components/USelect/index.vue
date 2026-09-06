<template>
  <el-select
    v-bind="attrs"
    :loading="loading"
    reserve-keyword
    :filter-method="remoteMethod"
    v-model="valueData"
    :placeholder="placeholder"
    clearable
    style="width: 100%"
    @change="changeItem"
  >
    <el-option
      v-for="item of options"
      :key="item._key"
      :value="item[keyValue.value || 'value']"
      :disabled="item.disabled"
      :label="item[keyValue.label || 'label']"
    >
      <div>
        <span v-if="keyValue.formatLabel && keyValue.formatLabel.length > 0">
          {{ item[keyValue.formatLabel[0]] }} <small>({{ item[keyValue.formatLabel[1]] }})</small>
        </span>
        <span v-else>{{ item[keyValue.label || 'label'] || item[keyValue.value || 'value'] }}</span>
      </div>
    </el-option>
  </el-select>
</template>

<script setup>
import { ref, watchEffect } from 'vue'
import { getDictItem, getCommonTableList } from '@/api/common'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['change'])

const valueData = ref('')
const options = ref([])
const list = ref([])
const loading = ref(false)

const props = defineProps({
  placeholder: {
    type: String,
    default: '请选择'
  },
  option: {
    type: [Array, Promise],
    default: () => []
  },
  dictType: {
    type: String,
    default: ''
  },
  request: {
    type: Object,
    default: () => {}
  },
  attrs: {
    type: Object,
    default: () => {}
  },
  defaultvalue: {
    type: Function
  },
  isSort: {
    type: Boolean,
    default: false
  },
  formData: {
    type: Object,
    default: () => ({})
  },
  keyValue: {
    type: Object,
    default: () => ({
      label: 'label',
      value: 'value'
    })
  }
})

const addListKey = (arr) => {
  return arr.map((item, index) => ({
    ...item,
    _key: `${item[props.keyValue.value]}_${index}_${Math.random()}`
  }))
}
const ifListIsStringArray = (data) => {
  const isString = data.every((item) => typeof item === 'string')
  const _list = data.map((item) => ({ label: item, value: item, _key: `${item}_${Math.random()}` }))
  if (isString) {
    options.value = _list
    list.value = _list
  } else {
    options.value = addListKey(data)
    list.value = addListKey(data)
  }
}
const getDictList = async () => {
  try {
    const { data } = await getDictItem(props.dictType)
    list.value = addListKey(data)
    options.value = addListKey(data)
  } catch (error) {
    ElMessage.error('系统错误')
  }
}

const customRequest = async () => {
  try {
    let { url, method, params = {}, formKey } = props.request
    if (formKey) {
      params = { ...params, [formKey]: props.formData[formKey] }
    }
    const { data } = await getCommonTableList({ url, method, params })
    if (data && Array.isArray(data)) {
      ifListIsStringArray(data)
    }
  } catch (error) {
    ElMessage.error('系统错误')
  }
}

const changeItem = (val) => {
  const item = options.value.find((item) => item[props.keyValue.value] === val)
  if (props.isSort && Array.isArray(val)) {
    const itemArr = val.sort((a, b) => a - b)
    valueData.value = itemArr
  }
  valueData.value = val
  emit('change', item)
}

const remoteMethod = (query) => {
  if (query) {
    loading.value = true
    loading.value = false
    options.value = [...list.value].filter((item) => {
      return (
        item[props.keyValue.label].toLowerCase().includes(query.toLowerCase()) ||
        (typeof item[props.keyValue.value] === 'number'
          ? item[props.keyValue.value].toString().toLowerCase().includes(query.toLowerCase())
          : item[props.keyValue.value].toLowerCase().includes(query.toLowerCase()))
      )
    })
  } else {
    options.value = list.value
  }
}

const init = async () => {
  const { option } = props
  if (Array.isArray(option) && option.length > 0) {
    ifListIsStringArray(option)
  } else if (Object.prototype.toString.call(option) === '[object Promise]') {
    const _option = await props.option
    options.value = addListKey(_option)
    list.value = addListKey(_option)
  } else if (props.dictType) {
    getDictList()
  } else if (Reflect.has(props.request || {}, 'url')) {
    customRequest()
  }
}

watchEffect(async () => {
  init()
})
</script>
