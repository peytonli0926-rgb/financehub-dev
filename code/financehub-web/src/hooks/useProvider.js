
import { getCommonTableList } from '@/api/common'
function useProvider () {
  const getCustomList = async () => {
    const apiURL = [
      { url: '/engine/scene/list', keyValue: { label: 'sceneName', value: 'sceneCode' } },
      { url: '/engine/scene/account-period/queryAll', keyValue: { label: 'periodName', value: 'periodCode' } },
      { url: '/engine/scene/account/listAll', keyValue: { label: 'accountName', value: 'accountCode' } }
    ]
    const [sceneList, periodList, subjectList] = await Promise.all(apiURL.map(async ({ url, keyValue }) => {
      const { data } = await getCommonTableList({ url, method: 'post' })
      return data.map(item => {
        return {
          label: item[keyValue.label],
          value: item[keyValue.value]
        }
      })
    }))
    return { sceneList, periodList, subjectList }
  }

  return {
    getCustomList
  }
}

export default useProvider
