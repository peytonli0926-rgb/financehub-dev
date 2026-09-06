<template>
  <div class="rule-editor w-100">
    <el-tooltip popper-class="custom-edit-show" :content="modelValue || ''"
      :fallback-placements="['bottom', 'top', 'right', 'left']">
      <el-input @click="beforeEnter" placeholder="请选择" :modelValue="modelValue" readonly class="w-100" />
    </el-tooltip>
    <!-- :visible="visible" -->
    <el-dialog :modal="false" append-to-body title="" width="45%" :show-close="false" v-model="dialogVisible"
      draggable class="custom-dialog">

      <template #default>
        <div class="u-p-10">
          <div class="u-m-b-10">
            <el-tag effect="plain" style="cursor: pointer;" size="small" @click="symbolClick(item)" class="u-m-r-10" v-for="(item, index) in operateSymbol" :key="index">{{item.name}}</el-tag>
          </div>
          <code-mirror   basic :id="editorId" @change="changeCodeMirror" @ready="readyCodeMirror"
            placeholder="请输入规则表达式" v-model="content" />

          <div class="params-conf ">
            <el-row>
              <el-col :span="12">
                <div class="conf-container" style="border-right: 0;">
                  <div class="title " style="height: 24px;">表列表</div>
                  <div class="content">
                    <ul>
                      <li class="item" :class="[currentItem.name === group.name ? 'active' : '']"
                        @click="handlerParent(group)" v-for="(group, group_idx) in fieldList" :key="group_idx">
                        <div class="name">{{ group.name }}</div>
                      </li>
                    </ul>
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="conf-container">
                  <div class="title u-flex u-col-center">
                    <span style="flex:100px 0 0">字段列表 </span>
                    <el-input size="small" v-model="searchName" @input="searchNameList" placeholder="请输入字典名称进行查询" />
                  </div>
                  <div class="content">
                    <ul>
                      <li class="item u-flex u-row-between" @click="addTag(group)"
                        :class="[childrenItem.name === group.name ? 'active' : '']"
                        v-for="(group, group_idx) in childrenList" :key="group_idx">
                        <div class="name">{{ group.name }}
                          <el-tooltip class="box-item" raw-content effect="dark" v-if="group.desc">
                            <el-icon>
                              <InfoFilled />
                            </el-icon>
                            <template #content>
                              <div v-html="group.desc"></div>
                            </template>
                          </el-tooltip>

                        </div>
                        <div class="type">
                          <el-tag> {{ group.type === 'fun' ? '函数' : dictMappingLabel(dictData,'sys_database_column_type',group.dataType) }}</el-tag>
                        </div>
                      </li>
                    </ul>
                  </div>
                </div>
              </el-col>

            </el-row>
          </div>
        </div>
        <div class="u-p-10 u-text-right">
          <el-button @click="validateFun" type="warning">语法校验</el-button>
          <el-button @click="cleanCode" type="danger">清空</el-button>
          <el-button @click="cancel">取消</el-button>

          <el-button type="primary" @click="submitRule">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { operateSymbol } from './conf'
import CodeMirror from 'vue-codemirror6'
import { InfoFilled } from '@element-plus/icons-vue'
import { useStore } from 'vuex'
import { validateSyntax } from '@/api/common'
import { ElMessage } from 'element-plus'
import { dictMappingLabel } from '@/utils'

const store = useStore()
const content = ref('')
const searchName = ref('')
const childrenList = ref([])
const copyChildrenList = ref([])
const currentItem = ref({})
const childrenItem = ref({})
const editorId = ref('editorId')
const visible = ref(false)
const dialogVisible = ref(false)
const viewControl = ref()

// 字段列表
const emit = defineEmits(['update:modelValue', 'on-click'])
const dictData = store.getters['useDictMapping/dictMapping']

const props = defineProps({
  modelValue: {
    type: String,
    default: () => ''
  },
  editorId: {
    type: String,
    default: () => ''
  },
  disabled: {
    type: Boolean,
    default: () => false
  },
  toolbar: {
    type: [String, Array],
    default: () => ` fullscreen undo redo restoredraft | cut
         add subtract multiply divide equal parenthesis bracket percent and or gt lt ltequal gtequal notqual doubleequal doubleand code codesample`
  },
  height: {
    type: Number,
    default: () => 250
  }
})

const changeCodeMirror = (dd) => {
  console.log(dd)
}
const readyCodeMirror = ({ view }) => {
  viewControl.value = view
}

// 获取字段数据
const fieldList = computed(() => {
  const _list = store.getters['useDictMapping/getEditorFieldsList']
  return _list
})

// 添加符号
const symbolClick = (item) => {
  insertTextToBox(item.symbol)
}

const validateFun = async () => {
  return new Promise((resolve, reject) => {
    validateSyntax({
      script: content.value
    }).then(({ code, msg }) => {
      if (code === 200) {
        ElMessage.success({ zIndex: 999999, message: '语法校验成功' })
        resolve()
      } else {
        ElMessage.error({ zIndex: 999999, message: msg })
        reject(new Error(msg))
      }
    })
  })
}
// 开启只读模式
// const setEditMode = (type) => {
//   tinymce.editors.myedit.setMode(type) // 开启只读模式
// }
const searchNameList = (keyword) => {
  const _tempKeyword = keyword.toLocaleUpperCase()
  if (keyword) {
    childrenList.value = childrenList.value.filter(item => item.name.includes(_tempKeyword))
    return
  }
  childrenList.value = copyChildrenList.value
}

const handlerParent = (row) => {
  currentItem.value = row
  childrenList.value = row?.children ?? []
  copyChildrenList.value = row?.children ?? []
}

const submitRule = async () => {
  await validateFun()
  const text = content.value
  emit('update:modelValue', text)
  cancel()
}
const cancel = () => {
  dialogVisible.value = false
}

const addTag = (data) => {
  childrenItem.value = data
  let label = `${data.name}(  )`
  if (data.type === 'field') {
    label = `{${data.code}}`
  }
  insertTextToBox(label)
}

const insertTextToBox = (label) => {
  const { from, to } = viewControl.value.state.selection.ranges[0]
  viewControl.value.dispatch({
    changes: { from, to, insert: label }
  })
}

// 规则表达式回显格式化
const beforeEnter = () => {
  editorId.value = props.editorId
  visible.value = true
  dialogVisible.value = true

  // const funObj = fieldList.value.find(item => item.name === '函数')
  // let funList = []
  // if (funObj) {
  //   funList = funObj.children.map(item => item.name)
  // }
  // 动态创建一个正则表达式
  // const reg = new RegExp(funList.join('|'), 'g')
  const _value = (props?.modelValue ?? '')
  content.value = _value
}

const cleanCode = () => {
  const dd = document.querySelectorAll('.cm-line')
  for (const item of dd) {
    item.remove()
  }
}

</script>

<style lang="scss" scoped>
.params-conf {
  margin-top: 10px;

  .content {
    text-align: left;
    overflow: auto;
    height: 200px;

    ul {
      margin: 0;
      padding: 0;

      .item {
        cursor: pointer;
        color: #4a538a;
        padding: 10px 5px;
        border-bottom: 1px solid #ddd;

        &.active {
          background: #eee;
        }
      }

    }
  }

  .conf-container {
    height: 250px;
    overflow: hidden;
    border: 1px solid #e9e9e9;

    // border-radius: 6px;
    .title {
      padding: 10px;
      background: #f4f4f4;
      border-bottom: 1px solid #ddd;
    }
  }
}

:deep(.cm-scroller) {
  height: 200px;
}
</style>
