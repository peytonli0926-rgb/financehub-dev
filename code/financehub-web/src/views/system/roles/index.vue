<template>
    <div>
        <UTable ref="utableRef" :options="options">
            <template #operationBtn="{ row }">
                <el-button type="primary" link size="small" @click="fromToRulePage(row, 'data')">分配数据权限</el-button>
                <el-button type="primary" link size="small" @click="fromToRulePage(row, 'user')">分配角色用户</el-button>
            </template>
            <template #status="{ row }">
        <el-switch active-value="0" inactive-value="1" v-model="row.status" size="small"
          @change="changeEnableFlag(row)" />
      </template>
        </UTable>

    </div>
</template>
<script setup>
import { ref } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { changeSwitchRequest } from '@/utils'

const { router } = useRouter()
const utableRef = ref()

const options = optionsConfig(router)

const fromToRulePage = ({ id, roleName, status }, type) => {
  if (type === 'data') {
    router.push(`/system/rolesDataAuth?roleId=${id}&roleName=${roleName}`)
    return
  }
  router.push(`/system/rolesUserAuth?roleId=${id}`)
}

// 切换switch 状态
const changeEnableFlag = async (row) => {
  changeSwitchRequest(options.request.edit, row, options.rowKey)
}
</script>
