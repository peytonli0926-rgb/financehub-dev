<template>
  <editor v-model="content" tag-name="div" :init="init" />
</template>

<script>
import tinymce from 'tinymce/tinymce'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver/theme' // 引用主题文件
import 'tinymce/icons/default' // 引用图标文件
import 'tinymce/plugins/link'
import 'tinymce/plugins/table'
import 'tinymce/plugins/lists'
import 'tinymce/plugins/advlist'
import 'tinymce/plugins/anchor'
import 'tinymce/plugins/autolink' // 锚点
import 'tinymce/plugins/autoresize'
import 'tinymce/plugins/autosave'
import 'tinymce/plugins/code' // 查看源码
import 'tinymce/plugins/codesample' // 插入代码
import 'tinymce/plugins/directionality' //
import 'tinymce/plugins/fullpage' // 页面属性编辑
import 'tinymce/plugins/fullscreen' // 全屏
import 'tinymce/plugins/help' // 帮助
import 'tinymce/plugins/hr' // 横线
import 'tinymce/plugins/image' // 图片
import 'tinymce/plugins/imagetools' // 图片工具
import 'tinymce/plugins/importcss' // 图片工具
import 'tinymce/plugins/insertdatetime' // 时间插入
import 'tinymce/plugins/pagebreak' // 分页
import 'tinymce/plugins/paste' // 粘贴
import 'tinymce/plugins/preview' // 预览
// import 'tinymce/plugins/quickbars' // 快捷菜单
import 'tinymce/plugins/searchreplace' // 查询替换
import 'tinymce/plugins/tabfocus' //
import 'tinymce/plugins/textpattern' //
import 'tinymce/plugins/toc' //
import 'tinymce/plugins/visualblocks' //
import 'tinymce/plugins/visualchars' //
import 'tinymce/plugins/wordcount' // 数字统计
import * as languageUrl from '../../../public/tinymce/zh_CN.js'// 中文语言包路径
import skinUrl from 'tinymce/skins/ui/oxide/skin.css' // 编辑器皮肤样式
import { uploadFile } from '@/api'
import { ElMessage } from 'element-plus'

export default {
  props: {
    modelValue: String
  },
  components: {
    editor: Editor
  },
  emits: { 'update:modelValue': null },
  setup (props, context) {
    console.log('props', props)
    const init = {
      language_url: languageUrl, // 中文语言包路径
      language: 'zh_CN',
      skin_url: skinUrl, // 编辑器皮肤样式
      menubar: true, // 隐藏菜单栏
      autoresize_bottom_margin: 50,
      min_height: 460,
      toolbar_mode: 'none',
      plugins:
        'wordcount visualchars visualblocks toc textpattern tabfocus searchreplace preview paste pagebreak insertdatetime importcss imagetools image hr help fullscreen fullpage directionality codesample code link code table lists advlist anchor autolink autoresize autosave', // 插件需要import进来
      toolbar:
        'formats lineheight searchreplace bold italic underline strikethrough indent aligncenter alignleft alignright alignjustify quicklink blockquote undo redo removeformat subscript superscript codesample code h2 h3 | image | hr numlist bullist link preview anchor pagebreak insertdatetime table forecolor backcolor fullscreen | fontsizeselect fontselect',
      content_style: 'p {margin: 5px 0; font-size: 14px}',
      fontsize_formats: '36pt 24pt 22pt 18pt 16pt 15pt 14pt 12pt 11pt 10.5pt 10pt 9.5pt 8pt',
      font_formats:
        '宋体=\'宋体\';黑体=\'黑体\';仿宋=\'仿宋\';仿宋GB2312=\'仿宋_GB2312\';楷体=\'楷体\';楷体GB2312=\'楷体_GB2312\';等线=\'等线\';微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;华文楷体=\'华文楷体\';Times New Roman=\'Times New Roman\'',
      branding: false,
      elementpath: false,
      resize: false, // 禁止改变大小
      statusbar: true, // 隐藏底部状态栏
      paste_data_images: false, // 图片是否可粘贴
      //   图片上传
      images_upload_handler: function (blobInfo, success, failure) {
        const params = new FormData()
        params.append('file', blobInfo.blob())
        uploadFile(params).then(res => {
          if (res && res.success) {
            success(res && res.data)
          } else {
            !res.success && ElMessage.error(res.msg)
            failure('提示信息：' + res.msg)
          }
        }).catch(err => {
          console.error(err)
          failure('提示信息：' + err)
        })
      }
    }
    tinymce.init(init) // 初始化
    const revertData = (content) => {
      context.emit('update:modelValue', content)
    }
    return {
      init, revertData
    }
  },
  data () {
    return {
      content: this.modelValue
    }
  },
  mounted () {},
  watch: {
    content () {
      this.revertData(this.content)
    },
    modelValue (val, oldVal) {
      this.$nextTick(() => {
        this.content = val
        this.revertData(this.content)
      })
    }
  }
}
</script>
