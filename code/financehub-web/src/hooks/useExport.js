import { ElLoading } from 'element-plus'
import { getCommonTableList } from '@/api/common'
import { confirmEl } from '@/utils'
import { ElMessage } from 'element-plus'
/** 导出按钮操作 */
import { useStore } from 'vuex'

function useExport() {
  const store = useStore()

  const globalLaoding = () => {
    return ElLoading.service({
      lock: true,
      text: '数据下载中...',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    })
  }
  /**
       * 下载文件
       * @param {*} data  blob 对象
       * @param {*} filename  文件名称
       * @param {*} mime  类型
       * @param {*} bom
       */
  const fileDownload = (data, filename, mime, bom) => {
    const blobData = (typeof bom !== 'undefined') ? [bom, data] : [data]
    const blob = new Blob(blobData, { type: mime || 'application/octet-stream' })
    if (typeof window.navigator.msSaveBlob !== 'undefined') {
      window.navigator.msSaveBlob(blob, filename)
    } else {
      const blobURL = (window.URL && window.URL.createObjectURL) ? window.URL.createObjectURL(blob) : window.webkitURL.createObjectURL(blob)
      const tempLink = document.createElement('a')
      tempLink.style.display = 'none'
      tempLink.href = blobURL
      tempLink.setAttribute('download', filename)
      if (typeof tempLink.download === 'undefined') {
        tempLink.setAttribute('target', '_blank')
      }
      document.body.appendChild(tempLink)
      tempLink.click()
      setTimeout(function () {
        document.body.removeChild(tempLink)
        window.URL.revokeObjectURL(blobURL)
      }, 200)
    }
  }
  /**
       *  导出按钮操作
       * @param {*} params 上传参数
       * @param {*} filename 文件名称
       * @param {*} isTemplate 是否是下载模版
       * @returns
       */
  const handleExport = (params, filename, isTemplate) => {
    // 如果是模版直接下载
    let loadingService = null
    if (isTemplate) {
      loadingService = globalLaoding()
      getCommonTableList({ method: 'post', ...params, config: { responseType: 'blob' } }, filename).then(response => {
        console.log(response)
        if (response.type === 'application/json') {
          ElMessage.error('下载失败')
        } else {
          fileDownload(response, filename);
        }
        loadingService.close()

      })
      return
    }
    if (!params) return

    confirmEl('是否确认导出该数据吗?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(function () {
      loadingService = globalLaoding()
      return getCommonTableList({ method: 'post', ...params, config: { responseType: 'blob' } })
    }).then(response => {
      loadingService.close()
      if (response.type === 'application/json') {
        ElMessage.error('导出失败')
      } else {
        fileDownload(response, filename);
      }
    }).catch(function () { })
  }
  /**
       * 点击上传弹出公共弹框
       * @param {*} params
       */
  const commonUploadDialog = (params) => {
    store.dispatch('useUploadDialog/setUploadConfig', {
      ...params,
      uuid: +new Date()
    })
  }
  return {
    handleExport,
    fileDownload,
    commonUploadDialog
  }
}

export default useExport
