export default {
  // 项目部署的基础路径
  base: '/',
  // 静态资源服务的文件夹 类型 string | false
  publicDir: 'public',
  // 存储缓存文件的目录
  cacheDir: 'node_modules/.vite',
  // 输出路径
  outDir: 'dist',
  // 为开发服务器配置 CORS。默认启用并允许任何源，传递一个 选项对象 来调整行为或设为 false 表示禁用
  cors: true,
  // 生成静态资源的存放路径
  assetsDir: 'static/',
  // 构建后是否生成 source map 文件
  sourcemap: !['production', 'test'].includes(process.env.NODE_ENV),
  // chunk 大小警告的限制
  chunkSizeWarningLimit: 2000,
  // 启用/禁用 CSS 代码拆分
  // 压缩大型输出文件可能会很慢，因此禁用该功能可能会提高大型项目的构建性能。
  cssCodeSplit: true,
  // 启用/禁用 brotli 压缩大小报告
  brotliSize: false,
  // 指定服务器应该监听哪个 IP 地址
  host: '0.0.0.0',
  // 指定开发服务器端口
  port: '8899',
  // 设为 true 时若端口已被占用则会直接退出，而不是尝试下一个可用端口
  strictPort: false,
  // 服务器启动时自动在浏览器中打开应用程序 此值为字符串时，会被用作 URL 的路径名
  open: true,
  // 调整控制台输出的级别 'info' | 'warn' | 'error' | 'silent'
  logLevel: 'info',
  // 设为 false 可以避免 Vite 清屏而错过在终端中打印某些关键信息
  clearScreen: false,
  // 是否删除生产环境console
  dropConsole: process.env.NODE_ENV === 'production',
  // 是否删除生产环境debugger
  dropDebugger: process.env.NODE_ENV === 'production',
  // 代理后端地址
  proxy: {
    // 正则表达式写法
    '^/api/.*': {
      // target: 'http://testshareholding.utfinancing.com/shareholdingarch/',
      target: 'http://127.0.0.1:8888/',
      // target: 'http://10.13.14.84:8888/',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  },
  // 是否使用mock数据
  useMock: true
}
// http://172.20.10.4:19005/
