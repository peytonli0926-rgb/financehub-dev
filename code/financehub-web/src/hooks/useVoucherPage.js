/*
 * @Author: Nathan
 * @Email: charlecai@deloitte.com.cn
 * @Date: 2024-05-14 20:35:36
 * @LastEditTime: 2024-06-06 21:34:36
 * @LastEditors: ${lastAuthor}
 * @Description: ${description}
 */
/**
 * 设置自定义标题
 */
import { useRouter } from '@toystory/lotso'
import { ElMessage } from 'element-plus'
// import { useStore } from 'vuex'
import Qs from 'qs'

function useVoucherPage () {
  const { router } = useRouter()
  const setVoucherPage = (
    { batchType, batchId, voucherIdList = [], interfaceDataId, ...result },
    type
  ) => {
    if (batchType || batchId || voucherIdList || interfaceDataId) {
      const resultStr = Qs.stringify({
        interfaceDataId,
        batchType,
        batchId,
        voucherIdList: voucherIdList.toString(),
        ...result
      })
      let url = `/searchBusiness/voucherBusiness?${resultStr}`
      if (type === 'total') {
        url = `/searchBusiness/voucherSummary?${resultStr}`
      }
      router.push(url)
      return
    }
    ElMessage.error('该条数据凭证存在错误，请检查！')
  }
  return {
    setVoucherPage
  }
}
export default useVoucherPage
