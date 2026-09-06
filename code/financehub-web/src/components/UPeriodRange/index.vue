<template>
  <div class="u-flex u-row-between w-100">
    <el-select  :loading="loading"  v-model="valueData.starValue"
    :placeholder="attrs.startPlaceholder||'开始区间'"  style="width: 100%;" @change="changeItem($event,0)">
    <el-option v-for="item in startOptions" :key="item.periodCode" :label="item.periodName"
      :value="item.periodCode"  />
  </el-select>
  -
  <el-select  :loading="loading"  v-model="valueData.endValue"
    :placeholder="attrs.endPlaceholder||'结束区间'"  style="width: 100%;" @change="changeItem($event,1)">
    <el-option v-for="item in endOptions" :key="item.periodCode" :label="item.periodName"
      :value="item.periodCode"  />
  </el-select>
</div>
</template>
<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { getCommonTableList } from '@/api/common'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['update:modelValue'])

const valueData = ref({
  starValue: '',
  endValue: ''
})
const options = ref([])
const startOptionsList = ref([])
const endOptionsList = ref([])
const loading = ref(false)

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
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
    default: () => { }
  },
  attrs: {
    type: Object,
    default: () => { }
  },
  defaultvalue: {
    type: Function
  },
  isSort: {
    type: Boolean,
    default: false
  }
})

const startOptions = computed({
  get () {
    return startOptionsList.value
  },
  set (val) {
    startOptionsList.value = options.value.filter(item => item.periodCode <= val)
  }
})

const endOptions = computed({
  get () {
    return endOptionsList.value
  },
  set (val) {
    endOptionsList.value = options.value.filter(item => item.periodCode >= val)
  }
})
const customRequest = async () => {
  try {
    const { data, code } = await getCommonTableList({ url: '/engine/scene/account-period/queryAll', method: 'post' })
    if (code === 200) {
      options.value = data
      startOptionsList.value = data
      endOptionsList.value = data
    }
  } catch (error) {
    ElMessage.error('系统错误')
  }
}

const changeItem = (val, index) => {
  if (index === 0) {
    endOptions.value = val
    valueData.value.starValue = val
  } else {
    startOptions.value = val
    valueData.value.endValue = val
  }
  emit('update:modelValue', [valueData.value.starValue, valueData.value.endValue])
}

watch(() => props.modelValue, (n, o) => {
  if (n.length === 0) {
    valueData.value = {
      starValue: '',
      endValue: ''
    }
    emit('update:modelValue', new Array(2))
  } else {
    valueData.value = {
      starValue: n[0],
      endValue: n[1]
    }
  }
}, { deep: true, immediate: true })

onMounted(() => {
  customRequest()
})

</script>
