<template>
   <el-input v-bind="attrs" :modelValue="modelValue" readonly  :placeholder="placeholder"  style="width: 100%;">
    <template #append>
        <el-button :icon="Search" @click="showDialog"/>
      </template>
   </el-input>

   <el-dialog :key="keyRange" :modal="false" v-if="dialogVisible" append-to-body title="用户检索" width="55%"  v-model="dialogVisible"
      draggable>
        <UTable ref="utableRef" @selection-change="selectionChange"   @search-form-data="searchFormData" :autoLoad="false" :options="options" />

        <div class="u-p-10 u-text-right">
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="submitRule">确定</el-button>
        </div>

    </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { optionsConfig } from './data'
const emit = defineEmits(['update:modelValue'])

const chooseValue = ref('')
const options = ref({})
const utableRef = ref()
const dialogVisible = ref(false)
const keyRange = ref(0)
const props = defineProps({
  modelValue: {
    type: String,
    default: () => ''
  },
  placeholder: {
    type: String,
    default: '请选择'
  },
  request: {
    type: Object,
    default: () => { }
  },
  attrs: {
    type: Object,
    default: () => { }
  },
  selectionType: {
    type: String,
    default: ''
  }
})
const selectData = ref([])
const searchFormData = (value) => {
  utableRef.value.requestBefore({
    ...value
  })
}
const selectionChange = (data) => {
  selectData.value = data
}

// watch(props.modelValue,(n,o)=>{
//   chooseValue.value=n
// })

const submitRule = () => {
  const _value = selectData.value.map(({ staffCode, staffName }) => `${staffName}|${staffCode}`)
  chooseValue.value = _value.toString()
  emit('update:modelValue', chooseValue.value)
  cancel()
}

const cancel = () => {
  dialogVisible.value = false
}

const showDialog = () => {
  dialogVisible.value = true
  options.value = { ...optionsConfig(), selectionType: props.selectionType }
  keyRange.value = +new Date()
}

</script>
