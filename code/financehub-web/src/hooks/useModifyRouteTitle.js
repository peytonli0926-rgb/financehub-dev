/**
 * 设置自定义标题
 */
import { useRouter } from '@toystory/lotso'
import { useStore } from 'vuex'

function useModifyRouteTitle () {
  const store = useStore()
  const { router } = useRouter()

  const setCurrentTabbarTitle = (title) => {
    const currentRoute = router.currentRoute.value
    const { query, meta } = currentRoute
    const decodeTitle = query[title] || title
    meta.title = decodeTitle
    store.dispatch('tabsBar/updateVisitedRoute', currentRoute)
  }
  return {
    setCurrentTabbarTitle
  }
}
export default useModifyRouteTitle
