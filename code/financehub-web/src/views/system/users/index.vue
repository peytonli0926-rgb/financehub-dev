<template>
    <div>
      <UTable ref="utableRef"  :options="options"  >
        <template #operationBtn="{ row }">
            <el-button type="primary" link size="small" @click="fromToRulePage(row)">分配管理用户</el-button>
        </template>

    </UTable>

    </div>
  </template>
<script setup>
import { ref } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'

const { router } = useRouter()
const utableRef = ref()

const options = optionsConfig(router)

const fromToRulePage = ({ id }) => {
  router.push(`/system/usersManage?parentUserId=${id}`)
}

</script>
