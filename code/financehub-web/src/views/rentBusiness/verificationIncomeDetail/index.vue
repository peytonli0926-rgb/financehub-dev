<template>
  <div>
    <UTable ref="utableRef" :autoLoad="false" :defaultParams="defaultParams" :options="options"
      @selection-change="selectionChange" @search-form-data="searchFormData">

      <template #operateHeaderRight>
        <el-button type="primary" @click="validate" v-permission="permission.validate">校验</el-button>
      </template>

    </UTable>

    <UDialog title="异常提醒" :visible="visible" @update:visible="vi => visible = vi">
      <div>已经确认收入的合同，回款后核销余额大于0：</div>
      <ul>
        <li v-for="item of  catchErrors.zeroList" :key="item">{{item}}</li>
      </ul>
      <div>回款后核销余额大于回款前核销余额：</div>
      <ul>
        <li v-for="item of  catchErrors.reservesList" :key="item">{{item}}</li>
      </ul>
    </UDialog>

  </div>
</template>
<script setup>
import { ref, onActivated, computed } from 'vue'
import { optionsConfig } from './config'
import { useRouter, useRoute } from '@toystory/lotso'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { confirmEl } from '@/utils'
import { verifyVerification } from '@/api/rentBusiness/verificationIncomeDetail'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()
// const formValue = ref({})
const visible = ref(false)
const catchErrors = ref({
  reservesList: [],
  zeroList: []
})
const defaultParams = ref({

})
const selectedData = ref([])
const route = useRoute()
const { query } = route.value

const searchFormData = (formData) => {
  // formValue.value = formData
  utableRef.value.requestBefore(formData)
}
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.verificationIncomeDetail
})
// 多选数据
const selectionChange = (data) => {
  selectedData.value = data
}
// 校验
const validate = () => {
  confirmEl('您确定校验这些数据吗?').then(async () => {
    const { code, data, msg } = await verifyVerification(query)
    const { greaterThanReservesList, greaterThanZeroList } = data || {}
    if (greaterThanReservesList.length > 0 | greaterThanZeroList.length > 0) {
      visible.value = true
    }
    catchErrors.value = {
      reservesList: greaterThanReservesList,
      zeroList: greaterThanZeroList
    }
    code === 200 ? ElMessage.success('操作成功') : ElMessage.error(msg)
  })
}
onActivated(() => {
  defaultParams.value = { ...query }
  utableRef.value.requestBefore(query)
})
</script>
