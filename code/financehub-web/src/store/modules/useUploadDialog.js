/**
 * 上传文件弹框
 * @returns
 */

const state = () => ({
  uploadConfig: {
    open: false,
    // 弹出层标题（用户导入）
    title: '',
    // 是否禁用上传
    isUploading: false,
    // 附带额外的参数
    data: {},
    // 模版下载
    templateUrl: '',
    // 设置上传的请求头部
    headers: {},
    // 上传的地址
    url: ''
  },
  uploadStatus: {}
})
const getters = {
  // 获取用户信息
  getUploadConfig: (state) => state.uploadConfig,
  // 上传成功的状态
  getUploadSuccess: (state) => state.uploadStatus
}
const mutations = {
  SET_UPLOAD_CONFIG: (state, config) => {
    state.uploadConfig = config
  },
  UPLOAD_SUCCESS: (state, status) => {
    state.uploadStatus = status
  }
}
const actions = {
  setUploadConfig ({ commit }, data) {
    commit('SET_UPLOAD_CONFIG', data)
  },
  setUploadSuccess ({ commit }, data) {
    commit('UPLOAD_SUCCESS', data)
  }

}

export default { state, getters, mutations, actions }
