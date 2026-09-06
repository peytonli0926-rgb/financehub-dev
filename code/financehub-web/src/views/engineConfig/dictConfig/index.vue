<template>
  <div>
    <UTable ref="utableRef" :options="options">
      <template #status="{ row }">
        <el-switch active-value="0" v-permission="permission.enable" inactive-value="1" v-model="row.status" size="small"
          @change="changeEnableFlag(row)" />
      </template>
    </UTable>

  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { changeSwitchRequest } from '@/utils'
import { useStore } from 'vuex'

const store = useStore()
const { router } = useRouter()
const utableRef = ref()

const options = optionsConfig(router)
// 切换switch 状态
const changeEnableFlag = async (row) => {
  changeSwitchRequest(options.request.edit, row, options.rowKey)
}

const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.dictConfig
})

</script>
