import { getAllNMappingDict } from '@/api/common'
import { getSceneFieldsList, getEditorFieldsList } from '@/api/engineConfig/sceneKeyConfig'
import localforage from 'localforage'
import moment from 'moment'
import { ElMessage } from 'element-plus'

// 数据字典
const state = () => ({
  dictMapping: {},
  sceneFieldsList: [],
  editorFieldsList: []
})
const getters = {
  dictMapping: (state) => state.dictMapping,
  getFieldsList: (state) => state.sceneFieldsList,
  getEditorFieldsList: (state) => state.editorFieldsList
}
const mutations = {
  SET_DICT_MAPPING: (state, dict) => {
    state.dictMapping = dict
  },
  SET_SCENE_FIELDS_LIST: (state, list) => {
    state.sceneFieldsList = list
  },
  SET_EDITOR_FIELDS_LIST: (state, list) => {
    state.editorFieldsList = list
  }
}
const actions = {
  // 获取全局的数据字典映射
  async getDictMapping ({ commit }) {
    try {
      const dictObj = await localforage.getItem('SET_DICT_MAPPING')
      if (dictObj && Reflect.has(dictObj, 'dict') && Object.keys(dictObj.dict).length > 0) {
        commit('SET_DICT_MAPPING', dictObj.dict)
        const d = moment().diff(dictObj.updateTime, 'minute')
        if (d >= 3) {
          const { dict } = await getLocalforage()
          commit('SET_DICT_MAPPING', dict)
        } else {
          commit('SET_DICT_MAPPING', dictObj.dict)
        }
      } else {
        const { dict } = await getLocalforage()
        commit('SET_DICT_MAPPING', dict)
      }
      //
    } catch (error) {
      commit('SET_DICT_MAPPING', {})
    }
  },
  // 强制从服务端刷新字典，供配置类页面在字典调整后立即使用
  async refreshDictMapping ({ commit }) {
    try {
      const { dict } = await getLocalforage()
      commit('SET_DICT_MAPPING', dict)
      return dict
    } catch (error) {
      commit('SET_DICT_MAPPING', {})
      return {}
    }
  },
  // 获取抽屉中的数据
  getFieldsList: async ({ commit }, param) => {
    try {
      const { data } = await getSceneFieldsList(param)
      commit('SET_SCENE_FIELDS_LIST', data)
    } catch (error) {
      // ElMessage.error('系统错误')
    }
  },
  // 获取规则表达式中的数据
  getEditorFields: async ({ commit }, param) => {
    try {
      const { data } = await getEditorFieldsList(param)
      commit('SET_EDITOR_FIELDS_LIST', data)
    } catch (error) {
      // ElMessage.error('系统错误')
    }
  }
}
// 获取离线数据
const getLocalforage = () => {
  return new Promise((resolve) => {
    getDictData().then(data => {
      localforage.setItem('SET_DICT_MAPPING', { dict: data, updateTime: (+new Date()) }).then((res) => {
        console.log('数据更新成功', res)
        resolve(res)
      }).catch(() => {
        console.log('数据更新失败')
      })
    })
  })
}

const getDictData = () => {
  return new Promise((resolve) => {
    getAllNMappingDict().then(({ msg, code, data }) => {
      if (code === 200) {
        resolve(data)
      } else {
        ElMessage.error(msg)
        resolve({})
      }
    })
  })
}

export default { state, getters, mutations, actions }
