<template>
  <div>
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>规则测试</span>
        </div>
      </template>
      <code-mirror @ready="readyCodeMirror" @change="changeCodeMirror" v-model="content" v-show="show" :lang="lang" basic
        placeholder="请输入JSON...">

      </code-mirror>
      <div class="u-p-t-10 u-text-center">
        <el-button @click="hideMirror">{{ show ? '隐藏' : '展开' }}</el-button>
        <el-button @click="formart" type="warning">格式化</el-button>
        <el-button @click="cleanCode" type="success">清空</el-button>
        <el-button @click="submit" type="primary">立即校验测试</el-button>

      </div>
    </el-card>
    <UTable ref="utableRef" :autoLoad="false" :options="options" />

  </div>
</template>
<script setup>
import { ref } from 'vue'
import CodeMirror from 'vue-codemirror6'
import { optionsConfig } from './config'
import { useRouter } from '@toystory/lotso'
import { json } from '@codemirror/lang-json'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'

const { router } = useRouter()
const store = useStore()
const dictData = store.getters['useDictMapping/dictMapping']
const options = optionsConfig(router, dictData)
const utableRef = ref()
const content = ref('')
const show = ref(true)
const lang = json()
// const linter = jsonParseLinter({});
const viewControl = ref(null)

const readyCodeMirror = ({ view }) => {
  viewControl.value = view
}
const changeCodeMirror = (dd) => {
}
// 提交
const submit = () => {
  if (content.value === '') {
    ElMessage.error('请输入json')
    return
  }
  try {
    const param = JSON.parse(content.value)
    // debugger
    utableRef.value.requestBefore({ param })
  } catch (error) {
    ElMessage.error('数据JSON格式不正确，请检查校验')
  }
}

const hideMirror = () => {
  show.value = !show.value
}
const formart = () => {
  try {
    content.value = JSON.stringify(JSON.parse(content.value), null, 2)
  } catch (error) {
    ElMessage.error('数据JSON格式不正确，请检查校验')
  }
}
const cleanCode = () => {
  const dd = document.querySelectorAll('.cm-line')
  for (const item of dd) {
    item.remove()
  }
}

</script>

<style lang="scss" scoped>
:deep(.cm-scroller) {
  height: 400px;
  border: 1px solid #ccc
}
</style>
