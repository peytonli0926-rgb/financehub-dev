
const state = () => ({
  name: '',
  avatar: ''
})
const getters = {
}
const mutations = {

  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }
}
const actions = {
  login ({ commit }, data) {
  },
  // 获取用户信息
  getInfo ({ commit }, data) {
    commit('SET_NAME', data.userName)
    commit('SET_AVATAR', data.avatar)
  }
}

export default { state, getters, mutations, actions }
