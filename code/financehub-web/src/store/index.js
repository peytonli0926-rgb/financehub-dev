import getters from './getters'

const modulesFiles = import.meta.globEager('./modules/*.js')

const modules = {}
for (const path in modulesFiles) {
  const moduleName = path.replace(/(.*\/)*([^.]+).*/gi, '$2')
  modules[moduleName] = modulesFiles[path].default
}

Object.keys(modules).forEach((key) => {
  modules[key].namespaced = true
})

export default {
  modules,
  getters
}
