<template>
  <div class="rule-editor w-100">
    <el-tooltip popper-class="custom-edit-show" :content="modelValue || ''"
      :fallback-placements="['bottom', 'top', 'right', 'left']">
      <el-input @click="beforeEnter" placeholder="请选择" :modelValue="modelValue" readonly class="w-100" />
    </el-tooltip>
    <!-- :visible="visible" -->
    <el-dialog :modal="false" append-to-body title="规则编辑器" width="40%" :show-close="false"
      v-model="dialogVisible" draggable custom-class="custom-dialog">

      <template #default>
        <div class="u-p-10">
          <Editor v-if="visible" :id="editorId" v-model="content" :init="init" tag-name="div" :disabled="disabled" />
          <div class="params-conf ">
            <el-row>
              <el-col :span="12">
                <div class="conf-container" style="border-right: 0;">
                  <div class="title ">表列表</div>
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
                          <el-tag> {{ group.type === 'fun' ? '函数' : columnTypeNames[group.dataType] }}</el-tag>
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
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="submitRule">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { operateSymbol, columnTypeNames } from './conf'
import { InfoFilled } from '@element-plus/icons-vue'
// 引入tinymce编辑器
import Editor from '@tinymce/tinymce-vue'
import tinymce from 'tinymce/tinymce' // tinymce默认hidden，不引入则不显示编辑器
import 'tinymce/themes/silver' // 编辑器主题，不引入则报错
import 'tinymce/icons/default' // 引入编辑器图标icon，不引入则不显示对应图标
import { useStore } from 'vuex'

const store = useStore()
const content = ref()
const searchName = ref('')
const childrenList = ref([])
const copyChildrenList = ref([])
const currentItem = ref({})
const childrenItem = ref({})
const editorId = ref('editorId')
const visible = ref(false)
const dialogVisible = ref(false)

// 字段列表
const emit = defineEmits(['update:modelValue', 'on-click'])

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
    default: () => `fullscreen undo redo restoredraft | cut
         add subtract multiply divide equal parenthesis bracket percent and or gt lt ltequal gtequal notqual doubleequal doubleand`
  },
  height: {
    type: Number,
    default: () => 150
  }
})
const fieldStyle = 'color: #005BAC;padding: 3px 5px;margin-top:3px; display: inline-block;'
const funStyle = 'color: #871ab3;font-weight:bold;padding: 3px 5px;margin-top:3px; display: inline-block;'
const symbolStyle = 'border-radius: 3px;margin:0 2px;font-size:14px;'

// 获取字段数据
const fieldList = computed(() => {
  const _list = store.getters['useDictMapping/getEditorFieldsList']
  return _list
})
const init = {
  // selector: `#${props.editorId}`,
  base_url: '/tinymce',
  language_url: '/tinymce/langs/zh_CN.js', // 引入语言包文件
  language: 'zh_CN', // 语言类型
  skin_url: '/tinymce/skins/ui/oxide', // 皮肤：浅色
  toolbar: props.toolbar, // 工具栏配置，设为false则隐藏
  toolbar_mode: 'sliding',
  menubar: false,
  height: props.height, // 注：引入autoresize插件时，此属性失效
  placeholder: '输入表达式',
  branding: false, // tiny技术支持信息是否显示
  resize: false, // 编辑器宽高是否可变，false-否,true-高可变，'both'-宽高均可，注意引号
  statusbar: false, // 最下方的元素路径和字数统计那一栏是否显示
  elementpath: false, // 元素路径是否显示
  setup (editor) {
    operateSymbol.map(item => {
      editor.ui.registry.addButton(item.dataType, {
        text: item.name,
        onAction: () => {
          symbolClick(item)
        }
      })
    })
  }
}
// 添加符号
const symbolClick = (item) => {
  tinymce.editors[editorId.value].insertContent(`<span  style="${symbolStyle}">${item.symbol} </span>`)
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
// 获取富文本的纯内容
const getEditorText = () => {
  const activeEditor = tinymce.activeEditor
  const text = activeEditor.getContent({ format: 'text' })
  return text
}

const handlerParent = (row) => {
  currentItem.value = row
  childrenList.value = row?.children ?? []
  copyChildrenList.value = row?.children ?? []
}

const submitRule = () => {
  const text = getEditorText()
  emit('update:modelValue', text)
  cancel()
}
const cancel = () => {
  dialogVisible.value = false
}

const addTag = (data) => {
  childrenItem.value = data
  if (data.type === 'field') {
    tinymce.editors[editorId.value].insertContent(
      `<span  contenteditable="false" style="${fieldStyle}">{${data.code}}</span> `
    )
  } else {
    tinymce.editors[editorId.value].insertContent(
      `<span style="${funStyle}" contenteditable="false">${data.name}</span>(  )`
    )
  }
}

// 规则表达式回显格式化
const beforeEnter = () => {
  editorId.value = props.editorId
  visible.value = true
  dialogVisible.value = true

  const funObj = fieldList.value.find(item => item.name === '函数')
  let funList = []
  if (funObj) {
    funList = funObj.children.map(item => item.name)
  }
  // 动态创建一个正则表达式
  const reg = new RegExp(funList.join('|'), 'g')
  const _value = (props?.modelValue ?? '').replace(/{(.*?)}/g, (match) => {
    return `<span contenteditable="false" style="${fieldStyle}">${match}</span>`
  }).replace(reg, (match) => {
    return `<span contenteditable="false" style="${funStyle}">${match}</span>`
  })
  content.value = _value
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
</style>
