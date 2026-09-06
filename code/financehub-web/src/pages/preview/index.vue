<template>
  <div class="preview" v-html="content">
  </div>
</template>

<script setup>
const content = localStorage.getItem('__preview') || ''
// import data from './login.json'
// import business from './business.json'
// const entries = [...data.log.entries, ...business.log.entries]
// const urlList = []
// const requestList = entries.filter(item => item._resourceType === 'xhr')
// requestList.forEach(item => {
//   const { request } = item
//   const url = request.url.replace(/\?.*/, '')
//   if (urlList.indexOf(url) === -1) {
//     urlList.push(url)
//   }
// })
// console.log(JSON.stringify(urlList))
</script>
<style lang="scss" scoped>
.preview {
  margin: 40px auto;
  width: 790px;
  // min-height: 100vh;
  border: 1px solid #EBEEF5;
  background-color: #FFF;
  color: #303133;
  border-radius: 4px;
  padding: 35px 40px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
</style>
