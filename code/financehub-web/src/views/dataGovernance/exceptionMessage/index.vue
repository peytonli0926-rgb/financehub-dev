<template>
    <div>
        <UTable ref="utableRef" @selection-change="selectionChange" :options="options" @search-form-data="handlerChange">
            <template #exceptionMessage="{ row }">
                <!-- <el-link type="primary" @click="handlerClick(row.exceptionMessage, '异常消息')">查看【异常消息】</el-link> -->
                <el-text type="primary" class="w-150px mb-2" truncated  @click="handlerClick(row.exceptionMessage, '异常消息')">
                    {{ row.exceptionMessage }}
                </el-text>
            </template>
            <template #exceptionStacktrace="{ row }">
                <!-- <el-link type="primary" @click="handlerClick(row.exceptionStacktrace, '异常堆栈')">查看【异常堆栈】</el-link> -->
                <el-text type="primary" class="w-150px mb-2" truncated  @click="handlerClick(row.exceptionStacktrace, '异常堆栈')">
                    {{ row.exceptionStacktrace }}
                </el-text>

            </template>
            <template #messageBody="{ row }">
                <!-- <el-link type="primary" @click="handlerClick(row.messageBody, '消息内容')">查看【消息内容】</el-link> -->

                <el-text type="primary" class="w-150px mb-2" truncated @click="handlerClick(row.messageBody, '消息内容')">
                    {{ row.messageBody }}
                </el-text>

            </template>

            <!-- <template #operationBtn="{ row }">
                <el-button type="primary" link size="small" @click="rePushMessage(row)">重新推送</el-button>
            </template> -->
            <!-- <template #operateHeaderRight>
                <el-button type="primary" :disabled="exportDisabled" @click="rePushMessage()">重新推送</el-button>
            </template> -->
        </UTable>

    </div>
</template>
<script setup>
import { ref } from 'vue'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'
import { ElMessage, ElNotification } from 'element-plus'
import { rePushMessageError } from '@/api/system'
const store = useStore()
import { confirmEl } from '@/utils'
const dictData = store.getters['useDictMapping/dictMapping']
const { router } = useRouter()
const utableRef = ref()
const formData = ref({})
const selectedData = ref({})
const exportDisabled = ref(true)
const options = optionsConfig(router, dictData)

const handlerChange = (values) => {
  formData.value = values
}
// 多选数据
const selectionChange = (data) => {
  const submitStatus = data.length === 0 ? false : data.every(item => item.status === '0')
  exportDisabled.value = !submitStatus
  selectedData.value = data
}

// 重新推送
const rePushMessage = () => {
  confirmEl('您确定要进行重新推送吗？').then(() => {
    pushMessage()
  })
}
const pushMessage = async () => {
  const ids = selectedData.value.map(item => item.id)
  const { code, msg } = await rePushMessageError({ ids }).catch(() => ElMessage.error(msg))
  if (code === 200) {
    utableRef.value.requestBefore()
  } else {
    ElMessage({ type: 'error', zIndex: 9999, message: msg })
  }
}
// 查看异常信息
const handlerClick = (messgae, title) => {
  ElNotification({
    title,
    message: `<div style="max-height:500px;overflow:auto">${messgae}</div>`,
    dangerouslyUseHTMLString: true,
    duration: 3000
  })
}

</script>
