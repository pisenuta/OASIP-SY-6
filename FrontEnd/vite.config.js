import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()], 
  base: '/sy6/',
  server: {
    proxy: {
      '/api' : {
        target: 'http://intproj21.sit.kmutt.ac.th',
        // target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})


