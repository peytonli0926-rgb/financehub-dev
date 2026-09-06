<template>
  <el-row :gutter="12">
    <el-col :span="5">
      <el-card shadow="never" class="h-100" style="overflow: auto">
        <div class="interface-left">
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
                <span class="u-m-l-5 u-font-15">{{ item.sceneName }}</span>
              </div>
            </div>
          </template>

          <el-empty v-else></el-empty>
        </div>
      </el-card>
    </el-col>
    <el-col :span="19">
      <el-card shadow="never" class="h-100">
        <UTable
          ref="utableRef"
          :defaultParams="defaultParams"
          @search-form-data="searchFormData"
          :options="options"
          :autoLoad="false"
        >
          <template #operationBtn="{ row }">
            <el-button
              v-if="row.dataType === 'List'"
              link
              type="primary"
              @click="openDetailStructure(row)"
            >明细结构</el-button>
          </template>
        </UTable>

        <!-- <SceneKeyConfig ref="sceneKeyRef" :scene="currentItem" :key="currentItem.id"  v-if="currentItem.id"/> -->
        <!-- <el-empty v-else description="请选择左侧项目查询"></el-empty> -->
      </el-card>
    </el-col>
    <el-drawer
      v-model="detailVisible"
      :title="detailTitle"
      size="72%"
      destroy-on-close
    >
      <UTable
        v-if="detailVisible"
        :key="currentListField.id"
        :defaultParams="detailDefaultParams"
        :options="detailOptions"
      ></UTable>
    </el-drawer>
  </el-row>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue'
import { Folder } from '@element-plus/icons-vue'
import { getCommonTableList } from '@/api/common'
import { optionsConfig, detailOptionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'

const store = useStore()
const { router } = useRouter()
const utableRef = ref()
const defaultParams = ref({})
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const detailOptions = detailOptionsConfig(dictData)
const currentItem = ref({})
const businessList = ref([])
const detailVisible = ref(false)
const currentListField = ref({})
const detailTitle = computed(() => `${currentListField.value.fieldName || ''} - 明细结构`)
const detailDefaultParams = computed(() => ({
  parentId: currentListField.value.id,
  sceneId: currentListField.value.sceneId,
  sceneCode: currentListField.value.sceneCode,
  sceneName: currentListField.value.sceneName
}))
const request = ref({
  url: '/engine/scene/list',
  method: 'post'
})

// 点击左侧列表item
const clickHanderItem = (item) => {
  const { sceneCode, sceneName, id } = item
  console.log(item)
  const params = { sceneCode, sceneName, sceneId: id }
  defaultParams.value = params
  utableRef.value.requestBefore()
}
// 查询左侧数据
const queryBusinessList = async () => {
  const { data } = await getCommonTableList({ ...request.value, params: {} })
  // 同时展示外部业务事件和财务中台内部生成的核算事件，便于维护凭证取数字段。
  businessList.value = data || []
}

const searchFormData = (value) => {
  utableRef.value.requestBefore(value)
}

const openDetailStructure = (row) => {
  currentListField.value = row
  detailVisible.value = true
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
.interface-left {
  height: calc(100vh - 150px);
  overflow: auto;
}
</style>
