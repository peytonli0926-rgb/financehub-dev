import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import legacy from '@vitejs/plugin-legacy'
import jsx from '@vitejs/plugin-vue-jsx'
import { viteMockServe } from 'vite-plugin-mock'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import viteImagemin from 'vite-plugin-imagemin'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

import compileConfig from './src/config/compile.config.js'

const path = require('path')

const {
  base,
  publicDir,
  outDir,
  assetsDir,
  sourcemap,
  cssCodeSplit,
  host,
  port,
  strictPort,
  open,
  cors,
  brotliSize,
  logLevel,
  clearScreen,
  dropConsole,
  dropDebugger,
  chunkSizeWarningLimit,
  proxy,
  useMock
} = compileConfig

const mockPath = './mock'

const spaFallbackPlugin = () => ({
  name: 'financehub-spa-fallback',
  configureServer(server) {
    server.middlewares.use((req, res, next) => {
      const url = req.url || ''
      if (
        req.method === 'GET' &&
        !url.startsWith('/api/') &&
        !url.startsWith('/@') &&
        !url.includes('.') &&
        !url.startsWith('/__')
      ) {
        req.url = '/index.html'
      }
      next()
    })
  }
})

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }) => {
  // const env = loadEnv(mode, process.cwd())
  return {
    root: process.cwd(),
    base,
    publicDir,
    logLevel,
    clearScreen,
    plugins: [
      vue(),
      jsx(),
      legacy({
        polyfills: ['es.promise.finally', 'es/map', 'es/set'],
        modernPolyfills: ['es.promise.finally']
      }),
      Components({
        resolvers: [ElementPlusResolver()]
      }),
      spaFallbackPlugin(),
      viteMockServe({
        mockPath,
        supportTs: false,
        localEnabled: useMock
      }),
      createSvgIconsPlugin({
        // 指定需要缓存的图标文件夹
        iconDirs: [path.resolve(process.cwd(), 'src/icons')],
        // 指定symbolId格式
        symbolId: 'icon-[dir]-[name]'
      }),
      // viteImagemin({
      //   gifsicle: {
      //     optimizationLevel: 7,
      //     interlaced: false
      //   },
      //   optipng: {
      //     optimizationLevel: 7
      //   },
      //   mozjpeg: {
      //     quality: 20
      //   },
      //   pngquant: {
      //     quality: [0.8, 0.9],
      //     speed: 4
      //   },
      //   svgo: {
      //     plugins: [
      //       {
      //         name: 'removeViewBox'
      //       },
      //       {
      //         name: 'removeEmptyAttrs',
      //         active: false
      //       }
      //     ]
      //   }
      // })
    ],

    server: {
      host,
      port,
      cors,
      strictPort,
      open,
      fs: {
        strict: false
      },
      proxy
    },

    resolve: {
      // 设置别名
      alias: {
        views: path.resolve(__dirname, 'src/views'),
        styles: path.resolve(__dirname, 'src/styles'),
        '@': path.resolve(__dirname, 'src'),
        '~pkg': path.resolve(__dirname, 'packages')
      },
      dedupe: ['vue']
    },

    css: {
      preprocessorOptions: {
        // 引入公用的样式
        scss: {
          additionalData: '@use "@/styles/index.scss" as *;'
        }
      }
    },

    corePlugins: {
      preflight: false
    },

    build: {
      target: 'es2015',
      outDir: outDir,
      assetsDir,
      sourcemap,
      cssCodeSplit,
      brotliSize,
      // rollupOptions: {
      //   output: {
      //     // chunkFileNames: 'static/js/[name]-[hash].js',
      //     // entryFileNames: 'static/js/[name]-[hash].js',
      //     // assetFileNames: 'static/[ext]/[name]-[hash].[ext]',
      //   },
      // },
      minify: 'terser',
      terserOptions: {
        compress: {
          keep_infinity: true,
          drop_console: dropConsole,
          drop_debugger: dropDebugger
        }
      },
      chunkSizeWarningLimit
    },

    optimizeDeps: {
      // 检测需要预构建的依赖项
      entries: []
    },

    define: {
      __VUE_PROD_DEVTOOLS__: true
    }
  }
})
