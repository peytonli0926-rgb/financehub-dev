
const state = () => ({
  btnPermission: {}
})
const getters = {
  // dictMapping: (state) => state.btnPermission,
  getBtnPermission: state => state.btnPermission
}
const mutations = {

  SET_PERMISSION: (state, list) => {
    state.btnPermission = list
  }

}
const actions = {
  setPermission ({ commit }, data) {
    if (Object.keys(data).length !== 1) {
      commit('SET_PERMISSION', data)
    } else {
      commit('SET_PERMISSION', {})
    }
  }
}

export default { state, getters, mutations, actions }
