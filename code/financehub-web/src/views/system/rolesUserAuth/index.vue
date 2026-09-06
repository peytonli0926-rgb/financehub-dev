<template>
  <div>
    <UTable ref="utableRef" :autoLoad="false" :addBefore="addBefore" :updateBefore="addBefore" :listAfter="requestAfter" :defaultParams="defaultParams" :options="options"
      @search-form-data="handlerChange">
      <template #status="{ row }">
        <el-switch active-value="0" inactive-value="1" v-model="row.status" size="small"
          @change="changeEnableFlag(row)" />
      </template>
</UTable>

  </div>
</template>
<script setup>
import { ref, onActivated } from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { changeSwitchRequest } from '@/utils'

const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const { router } = useRouter()
const route = useRoute()
const { query } = route.value
const utableRef = ref()
const defaultParams = ref({})
const options = optionsConfig(router, dictData)

const handlerChange = (value) => {
  refreshList(value)
}
const refreshList = (val = {}) => {
  utableRef.value.requestBefore(val)
}
const addBefore = (val) => {
  if (val) {
    const { userCode, ...more } = val
    const arr = userCode.split('|')
    return { ...more, userName: arr[0], userCode: arr[1] }
  }
  return val || {}
}
const requestAfter = (data) => {
  return data.map(item => ({
    ...item,
    userCode: `${item.userName}|${item.userCode}`
  }))
}
// 切换switch 状态
const changeEnableFlag = async (row) => {
  changeSwitchRequest(options.request.edit, row, options.rowKey)
}
onActivated(() => {
  const { roleId } = query
  defaultParams.value = { roleId }
  refreshList()
})

</script>
