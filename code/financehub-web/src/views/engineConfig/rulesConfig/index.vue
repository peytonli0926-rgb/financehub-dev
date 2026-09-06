<template>
  <div v-loading="loading" class="rules-config">
    <el-card shadow="never" :body-style="{ padding: '12px' }">
      <el-form
        ref="ruleFormRef"
        :model="rulesFormData"
        label-width="150px"
        label-position="top"
        v-if="rulesFormData.ruleList && rulesFormData.ruleList.length > 0"
      >
        <el-collapse accordion v-model="activeNames" @change="handleChange">
          <el-collapse-item
            :name="`form_${rulesForm.id}`"
            v-for="(rulesForm, firstIndex) of rulesFormData.ruleList"
            :key="rulesForm.id"
          >
            <template #title>
              <div class="u-flex u-row-around u-col-center">
                <UHeader>
                  <template #title>
                    <el-popover :width="400" v-if="!rulesForm.isEdit" trigger="hover">
                      <template #reference>
                        <div class="u-m-l-10 title-content">
                          {{ `${rulesForm.sceneVoucherName || '凭证基本信息' + (firstIndex + 1)}` }}
                        </div>
                      </template>
                      <template #default>
                        <div class="u-m-l-10 u-p-5">
                          {{ rulesForm.sceneVoucherName }}
                        </div>
                      </template>
                    </el-popover>

                    <el-input
                      @click="stopHide"
                      placeholder="请输入标题"
                      class="u-m-l-10"
                      v-else
                      v-model="rulesForm.sceneVoucherName"
                    ></el-input>

                    <el-button
                      circle
                      size="small"
                      class="u-m-l-10"
                      :icon="!rulesForm.isEdit ? EditPen : Select"
                      @click="editFromDataItem($event, ruleFormRef, rulesForm)"
                    ></el-button>
                  </template>
                </UHeader>
                <div class="u-flex u-row-around u-m-l-10" style="width: 120px">
                  <el-space>
                    <el-button
                      circle
                      size="small"
                      type="primary"
                      :icon="CopyDocument"
                      @click="copyFromDataItem($event, ruleFormRef, rulesForm)"
                    ></el-button>
                    <el-button
                      circle
                      size="small"
                      type="danger"
                      :icon="Delete"
                      @click="deleteFormDataItem($event, firstIndex)"
                    ></el-button>
                    <el-button
                      circle
                      size="small"
                      :icon="ArrowUp"
                      @click="moveUp(firstIndex, rulesFormData.ruleList, $event)"
                    ></el-button>
                    <el-button
                      circle
                      size="small"
                      :icon="ArrowDown"
                      @click="moveDown(firstIndex, rulesFormData.ruleList, $event)"
                    ></el-button>
                  </el-space>
                </div>
              </div>
            </template>
            <el-card shadow="never" :body-style="{ padding: '12px 12px 0' }">
              <el-row :gutter="16">
                <el-col :span="8">
                  <el-form-item
                    :prop="`ruleList.${firstIndex}.source`"
                    label="数据来源"
                    :rules="{
                      required: true,
                      message: '请选择数据来源',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <UDrawer v-model="rulesForm.source" :option="{ title: '数据来源' }" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item
                    label="规则执行条件"
                    :prop="`ruleList.${firstIndex}.scriptCondition`"
                    :rules="{
                      required: true,
                      message: '请选择规则执行条件',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <URuleEditor
                      :editorId="`${query.sceneId}_formData_${firstIndex}_scriptCondition`"
                      v-model="rulesForm.scriptCondition"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item
                    :prop="`ruleList.${firstIndex}.voucherType`"
                    label="凭证类型"
                    :rules="{
                      required: true,
                      message: '请选择凭证类型',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <USelect
                      :option="dictKeyLists.sys_voucher_type"
                      class="w-100 u-flex-1"
                      v-model="rulesForm.voucherType"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="业务场景">
                    <el-input disabled v-model="rulesForm.sceneName" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item
                    label="公司"
                    :prop="`ruleList.${firstIndex}.company`"
                    :rules="{
                      required: true,
                      message: '请选择公司',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <UDrawer v-model="rulesForm.company" :option="{ title: '公司' }" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item
                    label="业务日期"
                    :prop="`ruleList.${firstIndex}.businessDate`"
                    :rules="{
                      required: true,
                      message: '请选择业务日期',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <UDrawer v-model="rulesForm.businessDate" :option="{ title: '业务日期' }" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="币种" prop="currency">
                    <UDrawer v-model="rulesForm.currency" :option="{ title: '币种' }" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="凭证摘要" prop="voucherSummary">
                    <URuleEditor
                      :editorId="`${query.sceneId}_formData_${firstIndex}_voucherSummary`"
                      v-model="rulesForm.voucherSummary"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item
                    :prop="`ruleList.${firstIndex}.subSceneType`"
                    label="细分场景"
                    :rules="{
                      required: true,
                      message: '请选择细分场景',
                      trigger: ['blur', 'change']
                    }"
                  >
                    <USelect
                      :option="dictKeyLists.subSceneType"
                      class="w-100 u-flex-1"
                      v-model="rulesForm.subSceneType"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-card>
            <el-card class="u-m-t-12 u-rela" shadow="never" :body-style="{ padding: '12px' }">
              <template #header>
                <UHeader title="凭证内容" />
              </template>
              <!-- <div style="max-height:500px;overflow: auto;border:1px solid #f00">  -->
              <template v-if="rulesForm.entryList && rulesForm.entryList.length > 0">
                <el-card
                  shadow="never"
                  :class="[index === 0 ? '' : 'u-m-t-20']"
                  v-for="(item, index) of rulesForm.entryList"
                  :key="item.id"
                >
                  <template #header>
                    <div class="u-flex u-row-between w-100">
                      <UHeader :title="`凭证内容${index + 1}`">
                        <template #icon>
                          <span class="line"></span>
                        </template>
                      </UHeader>
                      <div class="u-flex u-row-around" >
                        <el-space>
                          <el-button
                            circle
                            size="small"
                            :icon="ArrowUp"
                            @click="moveUp(index, rulesForm.entryList, $event)"
                          ></el-button>
                          <el-button
                            circle
                            size="small"
                            :icon="ArrowDown"
                            @click="moveDown(index, rulesForm.entryList, $event)"
                          ></el-button>
                          <el-button
                            circle
                            size="small"
                            type="primary"
                            :icon="CopyDocument"
                            @click="copyEntryItem(rulesForm.entryList, item)"
                          ></el-button>
                          <el-button
                            circle
                            size="small"
                            type="danger"
                            :icon="Delete"
                            @click="deleteEntryItem(rulesForm.entryList, index)"
                          ></el-button>
                        </el-space>
                      </div>
                    </div>
                  </template>

                  <el-row :gutter="16">
                    <el-col :span="8">
                      <el-form-item
                        label="金额类型"
                        :prop="`ruleList.${firstIndex}.entryList.${index}.fundType`"
                        :rules="{
                          required: true,
                          message: '请选择金额类型',
                          trigger: ['blur', 'change']
                        }"
                      >
                        <USelect
                          :attrs="{ filterable: true }"
                          :option="dictKeyLists.sys_cash_type"
                          class="w-100 u-flex-1"
                          v-model="item.fundType"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item
                        label="是否有相关银行账号"
                        :prop="`ruleList.${firstIndex}.entryList.${index}.relateBankFlag`"
                        :rules="{
                          required: true,
                          message: '请选择是否有相关银行账号',
                          trigger: ['blur', 'change']
                        }"
                      >
                        <USelect
                          :option="dictKeyLists.sys_bank_flag"
                          class="w-100 u-flex-1"
                          v-model="item.relateBankFlag"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="8" v-if="item.relateBankFlag === '1'">
                      <el-form-item
                        label="银行账号"
                        :prop="`ruleList.${firstIndex}.entryList.${index}.bankAccount`"
                        :rules="{
                          required: true,
                          message: '请输入银行账号',
                          trigger: ['blur', 'change']
                        }"
                      >
                        <UDrawer v-model="item.bankAccount" :option="{ title: '银行卡号' }" />
                      </el-form-item>
                    </el-col>

                    <el-col :span="8">
                      <el-form-item
                        label="是否现金流属性相关"
                        :prop="`ruleList.${firstIndex}.entryList.${index}.cashAttributeFlag`"
                        :rules="{
                          required: true,
                          message: '请选择是否现金流属性相关',
                          trigger: ['blur', 'change']
                        }"
                      >
                        <USelect
                          :option="dictKeyLists.sys_bank_flag"
                          class="w-100 u-flex-1"
                          v-model="item.cashAttributeFlag"
                        />
                      </el-form-item>
                    </el-col>

                    <el-col :span="8" v-if="item.cashAttributeFlag === '1'">
                      <el-form-item
                        label="现金流属性"
                        :prop="`ruleList.${firstIndex}.entryList.${index}.cashAttribute`"
                        :rules="{
                          required: true,
                          message: '请选择现金流属性',
                          trigger: ['blur', 'change']
                        }"
                      >
                        <!-- <UDrawer v-model="item.cashAttribute" :option="{ title: '现金流属性' }" /> -->
                        <USelect
                          :option="dictKeyLists.cash_attribute"
                          class="w-100 u-flex-1"
                          v-model="item.cashAttribute"
                        />
                      </el-form-item>
                    </el-col>

                    <el-col :span="8">
                      <el-form-item
                        label="摘要名称"
                        :prop="`ruleList.${firstIndex}.entryList.${index}.voucherSummary`"
                      >
                        <URuleEditor
                          :editorId="`${query.sceneId}_formData_${firstIndex}_entryList_${index}_voucherSummary`"
                          v-model="item.voucherSummary"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="凭证行维度">
                        <USelect
                          is-sort
                          :attrs="{ multiple: true }"
                          :option="dictKeyLists.sys_assist_client"
                          class="w-100 u-flex-1"
                          v-model="item.assistFlags"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="24">
                      <el-form-item label="规则判定条件">
                        <el-table border :data="item.conditionList">
                          <el-table-column prop="scriptCondition" label="条件输入">
                            <template v-slot="{ row, $index }">
                              <el-form-item
                                :prop="`ruleList.${firstIndex}.entryList.${index}.conditionList.${$index}.scriptCondition`"
                                :rules="{
                                  required: true,
                                  message: '请选择条件输入',
                                  trigger: ['blur', 'change']
                                }"
                              >
                                <URuleEditor
                                  :editorId="`${query.sceneId}_formData_${firstIndex}_entryList_${index}_conditionList_${$index}_scriptCondition`"
                                  v-model="row.scriptCondition"
                                />
                              </el-form-item>
                            </template>
                          </el-table-column>
                          <el-table-column prop="donditionDescription" label="条件描述">
                            <template v-slot="{ row }">
                              <el-form-item>
                                <el-input v-model="row.donditionDescription" />
                              </el-form-item>
                            </template>
                          </el-table-column>
                          <el-table-column prop="scriptAmount" label="金额">
                            <template v-slot="{ row, $index }">
                              <el-form-item
                                :prop="`ruleList.${firstIndex}.entryList.${index}.conditionList.${$index}.scriptAmount`"
                                :rules="{
                                  required: true,
                                  message: '请选择金额',
                                  trigger: ['blur', 'change']
                                }"
                              >
                                <URuleEditor
                                  :editorId="`${query.sceneId}_formData_${firstIndex}_entryList_${index}_conditionList_${$index}_scriptAmount`"
                                  v-model="row.scriptAmount"
                                />
                              </el-form-item>
                            </template>
                          </el-table-column>
                          <el-table-column prop="amountDescription" label="金额描述">
                            <template v-slot="{ row }">
                              <el-form-item>
                                <el-input v-model="row.amountDescription" />
                              </el-form-item>
                            </template>
                          </el-table-column>
                          <el-table-column prop="debitCreditType" label="借贷方向">
                            <template v-slot="{ row }">
                              <el-form-item>
                                <USelect
                                  :option="dictKeyLists.sys_debit_credit"
                                  class="w-100 u-flex-1"
                                  v-model="row.debitCreditType"
                                />
                              </el-form-item>
                            </template>
                          </el-table-column>
                          <el-table-column prop="date" label="操作" width="100px">
                            <template v-slot="{ row, $index }">
                              <div class="u-flex u-row-around">
                                <el-space>
                                  <el-button
                                    circle
                                    size="small"
                                    type="primary"
                                    :icon="CopyDocument"
                                    @click="copyConditionItem(item, row)"
                                  ></el-button>
                                  <el-button
                                    circle
                                    size="small"
                                    type="danger"
                                    :icon="Delete"
                                    @click="deleteConditionItem(item, $index)"
                                  ></el-button>
                                </el-space>
                              </div>
                            </template>
                          </el-table-column>
                        </el-table>
                      </el-form-item>
                      <el-button type="primary" class="w-100" plain @click="addConditionItem(item)"
                        >新增一行</el-button
                      >
                    </el-col>
                  </el-row>
                </el-card>
              </template>
              <el-empty v-else description="请新增凭证内容"></el-empty>
              <div class="u-m-t-10 u-text-center">
                <el-button type="primary" @click="addEntryItem(rulesForm)">新增凭证内容</el-button>
              </div>
            </el-card>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <el-empty v-else description="请新增凭证"></el-empty>
    </el-card>
    <div class="u-m-t-10">
      <el-button type="primary" @click="addFirstItem(ruleFormRef)">新增凭证</el-button>
    </div>
    <div class="footer-submit u-text-right">
      <el-button class="w-88" @click="cancelPage">取消</el-button>
      <el-button
        class="w-88"
        type="primary"
        v-permission="permission.submit"
        v-loading="loading"
        @click="saveSceneRuleSubmit(ruleFormRef)"
        >保存</el-button
      >
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, onActivated, computed } from 'vue'
import { CopyDocument, Delete, EditPen, Select, ArrowUp, ArrowDown } from '@element-plus/icons-vue'
import { getSceneRuleDetail, saveSceneRuleDetail } from '@/api/engineConfig/sceneConfig'
import { useRouter, useRoute } from '@toystory/lotso'
import { dictMappingToArray, confirmEl } from '@/utils'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'

import { useModifyRouteTitle } from '@/hooks'

const { setCurrentTabbarTitle } = useModifyRouteTitle()
const store = useStore()
// 获取数据字典
const dictData = store.getters['useDictMapping/dictMapping']
const dictKeyLists = ref({
  sys_assist_client: dictMappingToArray(dictData, 'sys_assist_client'),
  sys_debit_credit: dictMappingToArray(dictData, 'sys_debit_credit'),
  sys_voucher_type: dictMappingToArray(dictData, 'sys_voucher_type'),
  sys_cash_type: dictMappingToArray(dictData, 'sys_cash_type'),
  sys_bank_flag: dictMappingToArray(dictData, 'sys_bank_flag'),
  subSceneType: dictMappingToArray(dictData, 'sys_sub_scene_type') || [],
  cash_attribute: dictMappingToArray(dictData, 'cash_attribute') || []

})
const refreshDictKeyLists = async () => {
  const latestDictData = await store.dispatch('useDictMapping/refreshDictMapping')
  const sceneName = query?.sceneName ?? ''
  const subSceneType = dictMappingToArray(latestDictData, 'sys_sub_scene_type') || []
  dictKeyLists.value = {
    sys_assist_client: dictMappingToArray(latestDictData, 'sys_assist_client'),
    sys_debit_credit: dictMappingToArray(latestDictData, 'sys_debit_credit'),
    sys_voucher_type: dictMappingToArray(latestDictData, 'sys_voucher_type'),
    sys_cash_type: dictMappingToArray(latestDictData, 'sys_cash_type'),
    sys_bank_flag: dictMappingToArray(latestDictData, 'sys_bank_flag'),
    subSceneType: sceneName ? [{ label: sceneName, value: sceneName }, ...subSceneType] : subSceneType,
    cash_attribute: dictMappingToArray(latestDictData, 'cash_attribute') || []
  }
}
const loading = ref(false)
const ruleFormRef = ref()
const activeNames = ref()
const entryObject = ref({})
const { router } = useRouter()
const route = useRoute()
const { query } = route.value
const rulesFormData = ref({
  sceneId: query?.sceneId ?? '',
  sceneName: query?.sceneName ?? '',
  ruleList: [
    {
      entryList: []
    }
  ]
})
// 获取按钮权限
const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.rulesConfig
})
// 获取规则详情
const getRuleDetails = async (isSave = false) => {
  loading.value = true
  const { code, data, msg } = await getSceneRuleDetail(query.sceneId)

  if (code === 200) {
    const { ruleList, ...result } = data

    const _ruleList = [...(ruleList ?? [])].map(({ entryList, ...res }) => {
      entryObject.value[res.id] = entryList
      return res
    })
    if (isSave) {
      rulesFormData.value = { ...rulesFormData.value, ...result }
    } else {
      rulesFormData.value = { ...result, ruleList: _ruleList || [] }
    }

    setTimeout(() => {
      ruleList && ruleList.length > 0 && ruleFormRef.value.clearValidate()
      loading.value = false
    }, 100)
  } else {
    loading.value = false
    ElMessage.error(msg)
  }
}
// 上移
const moveUp = (index, arr, e) => {
  e.stopPropagation()
  if (index === 0) {
    ElMessage.error('已是第一条')
    return
  }
  arr.splice(index - 1, 1, ...arr.splice(index, 1, arr[index - 1]))
}
// 下移
const moveDown = (index, arr, e) => {
  e.stopPropagation()
  if (index === arr.length - 1) {
    ElMessage.error('已是最后一条')
    return
  }
  arr.splice(index, 1, ...arr.splice(index + 1, 1, arr[index]))
}
const handleChange = (e) => {
  const _id = e.split('_')
  const { ruleList, ...res } = rulesFormData.value
  const _conditionList = ruleList.map((item) => {
    if (item.id === _id[1] && !Array.isArray(item?.entryList)) {
      item.entryList = entryObject.value[_id[1]]
    }
    return item
  })
  nextTick(() => {
    rulesFormData.value = { ruleList: _conditionList, ...res }
  })
}
const handleSubmitForm = async () => {
  const { ruleList, ...res } = JSON.parse(JSON.stringify(rulesFormData.value))

  await Promise.all(
    ruleList.map(async (item) => {
      if (!Array.isArray(item?.entryList)) {
        const _list = entryObject.value[item.id]
        item.entryList = await _list
      }
    })
  )

  return { ruleList, ...res }
}
const validateForm = (formEl) => {
  return new Promise((resolve) => {
    if (formEl) {
      formEl.validate((valid) => resolve(valid))
    } else {
      resolve(true)
    }
  })
}

// 新增凭证
const addFirstItem = async (formEl, list) => {
  const valid = await validateForm(formEl)
  const { sceneName, ruleList } = rulesFormData.value
  if (!valid) {
    ElMessage.error({
      zIndex: 9999999,
      message: '您的信息还未填写完整，请检查！'
    })
    return
  }
  ruleList.push({
    id: +new Date(),
    entryList: [],
    ...list,
    sceneName: sceneName || (query?.sceneName ?? '')
  })
  nextTick(() => {
    scrollToElementBottom()
    activeNames.value = `form_${ruleList.length - 1}`
  })
}

const editFromDataItem = (e, formEl, list) => {
  e.stopPropagation()
  list.isEdit = !list.isEdit
}
const stopHide = (e) => {
  e.stopPropagation()
}
// 复制外层整体
const copyFromDataItem = (e, formEl, list) => {
  e.stopPropagation()
  confirmEl('您确定要复制该条数据吗？').then(() => {
    const entryList = [...(list.entryList || [])].map((item) => {
      const conditionList = restConditionListId(item.conditionList || [])
      return {
        ...item,
        id: +new Date(),
        conditionList
      }
    })
    addFirstItem(formEl, { ...list, entryList })
  })
}
// 删除外层
const deleteFormDataItem = (e, index) => {
  e.stopPropagation()
  confirmEl('删除该条数据不能恢复，您确定要删除吗？').then(() => {
    rulesFormData.value.ruleList.splice(index, 1)
  })
}

// 新增凭证内容
const addEntryItem = async (formItem) => {
  formItem.entryList = [
    ...(formItem?.entryList ?? []),
    {
      id: +new Date(),
      assistFlags: [],
      bankAccount: '',
      cashAttribute: '',
      conditionList: [],
      fundType: '',
      relateBankFlag: '',
      voucherSummary: ''
    }
  ]
  nextTick(() => {
    scrollToElementBottom()
  })
}
// 复制要重新生成id
const restConditionListId = (list = []) => {
  const _conditionList = list.map((item) => {
    const { id, ...result } = item
    return {
      id: +new Date(),
      ...result
    }
  })
  return _conditionList
}
// 复制凭证
const copyEntryItem = (entryList, row) => {
  const { id, ...column } = row
  const _conditionList = restConditionListId(column.conditionList)
  const _temp = Object.assign({}, column, { conditionList: _conditionList })
  entryList.push({ ..._temp, id: +new Date() })
  nextTick(() => {
    scrollToElementBottom()
  })
}
// 删除凭证内容
const deleteEntryItem = (entryList, index) => {
  entryList.splice(index, 1)
}
// 新增条件
const addConditionItem = (list) => {
  list.conditionList = [
    ...(list?.conditionList ?? []),
    {
      // fundType: '',
      scriptCondition: '',
      donditionDescription: '',
      scriptAmount: '',
      amountDescription: '',
      debitCreditType: ''
    }
  ]
}
// 复制条件
const copyConditionItem = (list, row) => {
  const { id, ...column } = row
  list.conditionList.push({ ...column, id: +new Date() })
}
// 删除条件
const deleteConditionItem = (list, index) => {
  list.conditionList.splice(index, 1)
}
// 数据提交
const saveSceneRuleSubmit = async (formEl) => {
  const valid = await validateForm(formEl)
  if (!valid) {
    ElMessage.error('您的信息还未填写完整，请检查！')
    return
  }
  loading.value = true
  const params = await handleSubmitForm()
  const { code, msg, data } = await saveSceneRuleDetail(params).catch(
    (res) => (loading.value = false)
  )
  if (code === 200) {
    loading.value = false
    if (data) {
      ElMessage.success('保存成功')
      getRuleDetails(true)
    } else {
      confirmEl('操作冲突，需刷新页面！').then(() => {
        getRuleDetails()
      })
    }
  } else {
    ElMessage.error(msg)
  }
}
// 滚动到底部
const scrollToElementBottom = () => {
  const el = document.querySelector('.el-scrollbar__wrap')
  if (el) {
    el.scrollTo({
      // 滚动到元素位置
      top: 50000, // 推荐使用，getBoundingClientRect 相对于当前视口的位置
      behavior: 'smooth' // 平滑滚动
    })
  }
}
const cancelPage = () => {
  store.dispatch('tabsBar/delVisitedRoute', router.currentRoute.value)
  router.push('/engineConfig/sceneConfig')
}

onMounted(async () => {
  await refreshDictKeyLists()
  getRuleDetails()
})

onActivated(() => {
  const { sceneId, sceneName } = query
  const { subSceneType = [] } = dictKeyLists.value || {}
  setCurrentTabbarTitle('sceneName')
  dictKeyLists.value.subSceneType = [{ label: sceneName, value: sceneName }, ...subSceneType]
  store.dispatch('useDictMapping/getFieldsList', sceneId)
  store.dispatch('useDictMapping/getEditorFields', sceneId)
})
</script>

<style lang="scss" scoped>
.rules-config {
  :deep(.el-input__wrapper) {
    width: 95.5% !important;
  }

  :deep(.footer-copyright) {
    display: none !important;
  }

  :deep(.el-card__header) {
    padding: 10px;
  }

  .title-content {
    width: 400px;
    overflow: hidden;
    font-size: 14px;
    color: #333;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .line {
    display: inline-block;
    width: 3px;
    height: 15px;
    background: var(--el-color-primary);
    border-radius: 5px;
  }

  .footer-submit {
    position: fixed;
    bottom: 0;
    left: 0;
    z-index: 12;
    width: calc(100% - 24px);
    padding: 12px;
    background: #fff;
    box-shadow: 0 0 5px #666;

    .w-88 {
      width: 88px;
    }
  }
}
</style>
