<template>
  <div class="w-100">
    <el-input placeholder="请选择" @click="drawer = true" :modelValue="modelValue" readonly />
    <el-drawer v-model="drawer" :title="option.title || '数据源'" :size="option.size" :direction="option.direction || 'rtl'">
      <template #default>
        <div class="u-flex h-100">
          <div class="u-flex-1 h-100" style="border-right:1px solid #ddd">
            <div class="title">表名称</div>
            <div class="content">

              <ul :title="group.name" :name="group.id" v-for="(group, group_idx) in fieldList" :key="group_idx">
                <li class="item" @click="handlerParent(group)" :class="[currentItem.name === group.name ? 'active' : '']">
                  <div class="name">{{ group.name }}</div>
                </li>
              </ul>

            </div>
          </div>
          <div class="u-flex-1 h-100">
            <div class="title u-flex u-col-center">
              <span style="flex:100px 0 0">字段名称 </span>
              <el-input size="small" v-model="searchName" @input="searchNameList" placeholder="请输入字段名称进行查询" />

            </div>
            <div class="content children " style="height: 90%;">
              <ul :title="group.name" :name="group.id" v-for="(group, group_idx) in childrenList" :key="group_idx">
                <li class="item u-flex u-row-between" @dblclick="dbAddTag(group)" @click="addTag(group)"
                  :class="[childrenItem.name === group.name ? 'active' : '']">
                  <div class="name">{{ group.name }}</div>
                  <div class="type">
                    {{ group.dataTypeName }}
                  </div>
                </li>
              </ul>
            </div>
          </div>

        </div>

      </template>
      <template #footer>
        <div class="custom-footer">
          <el-button @click="clear">清空</el-button>
          <el-button @click="drawer = false">取消</el-button>
          <el-button type="primary" @click="confirmClick">确定</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'

const store = useStore()
const emit = defineEmits(['update:modelValue', 'on-click'])
const drawer = ref(false)

const searchName = ref('')
const currentItem = ref({})
const childrenList = ref([])
const childrenItem = ref({})
const copyChildrenList = ref([])

defineProps({
  option: {
    type: Object,
    default: () => { }
  },
  modelValue: {
    type: String,
    default: () => ''
  }
})

// 获取字段数据
const fieldList = computed(() => {
  const _list = store.getters['useDictMapping/getFieldsList']
  console.log(_list)
  return _list
})

const searchNameList = (keyword) => {
  const _tempKeyword = keyword.toLocaleUpperCase()
  if (keyword) {
    childrenList.value = childrenList.value.filter(item => item.name.includes(_tempKeyword))
    return
  }
  childrenList.value = copyChildrenList.value
}

// 点击父节点
const handlerParent = (row) => {
  const { children, ...result } = row
  currentItem.value = result
  childrenList.value = children
  copyChildrenList.value = children
}
// 添加字段到输入框
const addTag = (data) => {
  console.log(data)
  childrenItem.value = data
}
// 双击添加字段到输入框
const dbAddTag = (data) => {
  childrenItem.value = data
  confirmClick()
}
// 点击确认字段到输入框
const confirmClick = () => {
  if (!childrenItem.value.name) {
    ElMessage({
      message: '请选择字段',
      zIndex: 99999,
      type: 'error'
    })
    return
  }
  const text = `{${childrenItem.value.code}}`
  emit('update:modelValue', text)
  drawer.value = false
}

const clear = () => {
  currentItem.value = {}
  childrenItem.value = {}
  childrenList.value = []
  emit('update:modelValue', '')
  drawer.value = false
}

</script>

<style lang="scss" scoped>
:deep(.el-drawer__footer) {
  padding: 0 !important;

}

:deep(.el-drawer__header) {
  background: #f4f4f4;
  border-top: 1px solid #ddd;
  padding: 10px 15px;
  margin: 0;
}

:deep(.el-drawer__body) {
  padding: 0
}

.custom-footer {
  flex: auto;
  padding: 10px 15px;
  background: #f4f4f4;
  border-top: 1px solid #ddd;
}

.content {
  text-align: left;
  overflow: auto;
  height: 210px;

  ul {
    margin: 0;
    padding: 0;

    .item {
      cursor: pointer;
      color: #4a538a;
      padding: 10px 5px;
      border-bottom: 1px solid #eee;

      &.active {
        background: #eee;
      }
    }

  }
}

.title {
  padding: 5px 10px;
  // background: #f4f4f4;
  border-bottom: 1px solid #f4f4f4;
}
</style>
