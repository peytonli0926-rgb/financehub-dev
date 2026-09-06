<template>
  <el-row :gutter="12">
    <el-col :span="5">
      <el-card shadow="never" class="h-100">
        <template v-if="businessList.length > 0">
          <div
            class="u-flex u-row-between u-p-10 hover-list-item"
            :class="[item.id === currentItem.id ? 'active' : '']"
            v-for="item of businessList"
            :key="item.id"
          >
            <div class="u-cursor-p u-flex-1" @click="clickHanderItem(item)">
              <el-icon>
                <Folder />
              </el-icon>
              <span class="u-m-l-5">{{ item.businessCode }}/{{ item.businessName }}</span>
            </div>
            <div class="color-999999">
              <span class="item-count">{{ item.sceneCount }}</span>
              <span class="item-more">
                <el-popover placement="bottom" :width="100">
                  <template #reference>
                    <el-icon color="#999">
                      <MoreFilled />
                    </el-icon>
                  </template>
                  <ul class="item-more-menu">
                    <li @click="rename(item)" v-permission="permission.rename">重命名</li>
                  </ul>
                </el-popover>
              </span>
            </div>
          </div>
        </template>
        <el-empty v-else></el-empty>
      </el-card>
    </el-col>
    <el-col :span="19">
      <el-card shadow="never" class="h-100">
        <div class="u-p-10 u-flex u-row-right">
          <el-button
            v-permission="permission.delete"
            :disabled="selectKeys.length === 0"
            @click="deleteSceneItem"
            >删除</el-button
          >
          <el-button
            v-permission="permission.add"
            type="primary"
            :disabled="!currentItem.businessCode"
            @click="visible = true"
            >新增</el-button
          >
        </div>
        <el-table
          v-loading="loading"
          border
          :data="sceneList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="序号" width="55">
            <template v-slot="{ $index }">{{ $index + 1 }}</template>
          </el-table-column>
          <el-table-column label="场景编码" prop="sceneCode" />
          <el-table-column label="场景名称" prop="sceneName" />
        </el-table>
        <div class="u-m-t-10">
          已选择<span class="u-color-primary u-p-l-5 u-p-r-5">{{ selectKeys.length }}</span
          >项
        </div>
      </el-card>
    </el-col>

    <UDialog
      title="新增场景"
      :permission="permission"
      :visible="visible"
      @update:visible="(vi) => (visible = vi)"
      @handle-submit="handleDialogSubmit"
    >
      <el-form
        ref="ruleFormRef"
        :model="ruleForm"
        :rules="rules"
        label-width="120px"
        class="demo-ruleForm"
        status-icon
      >
        <el-form-item label="场景名称" prop="sceneName">
          <USelect
            v-model="ruleForm.sceneName"
            @change="onChange"
            :keyValue="keyValue"
            :request="request"
          />
        </el-form-item>
        <el-form-item label="场景编码" prop="sceneCode">
          <el-input disabled v-model="ruleForm.sceneCode" />
        </el-form-item>
      </el-form>
    </UDialog>
  </el-row>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue'
import { Folder, MoreFilled } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStore } from 'vuex'
import {
  getBusinessList,
  getSceneList,
  saveBusinessSceneItem,
  deleteBusinessSceneItem,
  renameBusinessSceneItem
} from '@/api/engineConfig/businessConfig'
import { confirmEl } from '@/utils'

const store = useStore()
const currentItem = ref({})
const businessList = ref([])
const sceneList = ref([])
const visible = ref(false)
const loading = ref(false)
const ruleForm = ref({
  sceneName: '',
  sceneCode: '',
  businessId: '',
  sceneId: ''
})
const ruleFormRef = ref()
const selectKeys = ref([])
const rules = {
  sceneName: [
    {
      required: true,
      message: '请选择场景',
      trigger: 'change'
    }
  ]
}
const keyValue = {
  label: 'sceneName',
  value: 'sceneName'
}

const request = ref({
  url: '/engine/scene/list',
  method: 'post'
})

const permission = computed(() => {
  const btnPermissions = store.getters['useBtnPermission/getBtnPermission']
  return btnPermissions.businessConfig
})

const rename = (row) => {
  ElMessageBox.prompt('请输入业务名称', '', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    closeOnClickModal: false,
    inputValue: row.businessName,
    inputPattern: /\S+/,
    inputErrorMessage: '请输入业务名称'
  })
    .then(async ({ value }) => {
      await renameBusinessSceneItem({ ...row, businessName: value })
      queryBusinessList()
      ElMessage({
        type: 'success',
        message: '操作成功'
      })
    })
    .catch(() => {
      // ElMessage({
      //   type: 'error',
      //   message: `操作失败`
      // })
    })
}
// 点击左侧列表item
const clickHanderItem = (item) => {
  currentItem.value = item
  ruleForm.value.businessId = item.id
  querySceneList(item.id)
}
// 查询左侧数据
const queryBusinessList = async () => {
  const { data = [] } = await getBusinessList()
  businessList.value = data.filter((item) => item.businessCode !== 'default')
}
// 查询右侧数据
const querySceneList = async (id) => {
  loading.value = true
  const { data } = await getSceneList(id)
  sceneList.value = data
  loading.value = false
}
// 多选
const handleSelectionChange = (rows) => {
  selectKeys.value = rows.map((item) => item.id)
}

// 添加场景
const handleDialogSubmit = async () => {
  if (!ruleFormRef.value) return
  await ruleFormRef.value.validate(async (valid) => {
    if (!valid) return
    await saveBusinessSceneItem(ruleForm.value)
    visible.value = false
    querySceneList(currentItem.value.id)
    queryBusinessList()
    ElMessage.success('操作成功')
  })
}
// 下拉框changg事件
const onChange = (item = { sceneName: '', sceneCode: '', id: '' }) => {
  const { sceneName = '', sceneCode = '', id = '' } = item
  ruleForm.value = {
    ...ruleForm.value,
    sceneName,
    sceneCode,
    sceneId: id
  }
}
// 批量删除
const deleteSceneItem = () => {
  confirmEl('您确定要删除吗？').then(async () => {
    await deleteBusinessSceneItem(selectKeys.value)
    querySceneList(currentItem.value.id)
    queryBusinessList()
    ElMessage.success('操作成功')
  })
}

onMounted(() => {
  queryBusinessList()
})
</script>

<style lang="scss" scoped>
.title {
  font-family: PingFangSC-Medium, PingFang SC;
  font-size: 16px;
  font-weight: 400;
}

.hover-list-item {
  &.active {
    background: #dddcdc;
    border-radius: 5px;
  }

  .item-more {
    display: none;
  }

  &:hover {
    background: #f4f4f4;
    border-radius: 5px;

    .item-more {
      display: block;
    }

    .item-count {
      display: none;
    }
  }
}

.item-more-menu {
  padding: 0;
  margin: 0;
  list-style: none;

  li {
    padding: 4px 15px;
    cursor: pointer;
    border-bottom: 1px solid #eee;

    &:last-child {
      border-bottom: 0;
    }
  }
}
</style>
