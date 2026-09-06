<template>
  <el-form :model="sureFormData" ref="sureFormDataRef" label-width="100px">
    <el-row>
      <el-col :span="8">
        <el-form-item label="记账日期" prop="accountDate" :rules="{ required: true, message: '请选择记账日期', trigger: ['blur', 'change'] }">
          <el-date-picker type="date" value-format="YYYY-MM-DD" format="YYYY-MM-DD" v-model="sureFormData.accountDate"
            placeholder="请选择记账日期" ></el-date-picker>
        </el-form-item>
      </el-col>
      <el-col>
        <el-table :data="sureFormData.claimRecordNewList" border>
          <el-table-column label="原业务系统批扣流水号" prop="ebankSerialNumber"> </el-table-column>
          <el-table-column label="新业务系统批扣流水号">
            <template v-slot="{row,$index}">
              <el-form-item label-width="0px" :prop="`claimRecordNewList.${$index}.newEbankSerialNumber`"
                :rules="{ required: true, message: '请输入新业务系统批扣流水号', trigger: ['blur', 'change'] }">
                <el-input v-model="row.newEbankSerialNumber" placeholder="请输入认新业务系统批扣流水号"></el-input>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="认领/冲销金额" prop="claimAmount" width="150px">
            <template v-slot="{row}">
              <component :is="toThousands(row.claimAmount)"></component>
            </template>
          </el-table-column>
          <el-table-column label="调整金额">
            <template v-slot="{row,$index}">
              <el-form-item label-width="0px" :prop="`claimRecordNewList.${$index}.adjustAmount`"
                :rules="{ required: true,max:row.claimAmount,validator:valideClaimAmount, trigger: ['blur', 'change'] }">
                <el-input-number class="w-100" controls-position="right" v-model="row.adjustAmount" placeholder="请输入调整金额" />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="合同编号">
            <template v-slot="{row}">
              <!-- <USelectPagination :attrs="{ filterable: true,multiple:true }" placeholder="请选择" class="w-100 u-flex-1"
                      title="合同编号" :request="{ url: '/engine/finance/kingdee/option/queryGeneralAsst', method: 'post' , params: { asstType: '合同号' }}"
                      :keyValue="{ label: 'code', value: 'code',formatLabel:['code','name'] }"   v-model="row.contractCode" /> -->

                      <USelect :attrs="{ filterable: true }" :option="row.contractCodeList" class="w-100 u-flex-1" v-model="row.contractCode" />
                    </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col>
        <el-divider />
        <el-table :data="sureFormData.claimRecordOldList" border>
          <el-table-column label="原业务系统批扣流水号" prop="ebankSerialNumber"> </el-table-column>
          <el-table-column label="网银到账金额" prop="bankAmount">
            <template v-slot="{row}">
              <component :is="toThousands(row.bankAmount)"></component>
            </template>
          </el-table-column>
          <el-table-column label="认领/冲销金额" prop="claimAmount">
            <template v-slot="{row}">
              <component :is="toThousands(row.claimAmount)"></component>
            </template> </el-table-column>
          <el-table-column label="网银确认日期" prop="businessHappenDate"> </el-table-column>

        </el-table>

      </el-col>
    </el-row>

  </el-form>
</template>
<script setup>
// 认领
import { ref, watch } from 'vue'
import { validateForm,toThousands } from '@/utils/index'
const props = defineProps({
  detailData: {
    type: Object,
    default: () => ({})
  }
})

const sureFormDataRef = ref()
const sureFormData = ref({
  accountDate: '',
  claimRecordNewList: [],
  claimRecordOldList: [],
  sumId: 0
})

watch(() => props.detailData, (n, o) => {
  if (JSON.stringify(n) !== JSON.stringify(o)) {
    sureFormData.value = {
      ...sureFormData.value,
      ...n
    }
  }
}, { deep: true, immediate: true })
// 判断认领金额是否大于或者小于剩余未确认金额的绝对值
const valideClaimAmount = (rule, value, callback) => {
  const { max } = rule
  if (value > max) {
    callback(new Error('调整金额不能大于认领/冲销金额'))
  } else if (value < -max) {
    callback(new Error('调整金额不能小于认领/冲销金额'))
  } else {
    callback()
  }
}
const exposeFormData = async () => {
  const valid = await validateForm(sureFormDataRef.value)
  if (!valid) return
  return sureFormData.value
}

defineExpose({
  exposeFormData
})

</script>

<style lang="scss" scoped></style>
