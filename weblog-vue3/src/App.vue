<template>
  <el-config-provider :locale="locale">
    <div class="app-container">
      <router-view></router-view>
    </div>
  </el-config-provider>
</template>

<script setup>
import { defineComponent } from 'vue'

import zhCn from 'element-plus/lib/locale/lang/zh-cn'
let locale = zhCn

</script>

<style>
body {
  font-family: -apple-system-font, BlinkMacSystemFont, "Helvetica Neue", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei UI", "Microsoft YaHei", Arial, sans-serif;
  color: #4c4e4d;
  font-size: 16px;
  background: linear-gradient(135deg, #f0f7ff 0%, #f4f4f4 50%, #e6f0ff 100%);
  background-attachment: fixed;
  line-height: 1.6;
  margin: 0;
  padding: 0;
  min-height: 100vh;
}

#nprogress .bar {
   background: var(--el-color-primary)!important;
}

/* 添加更柔和的阴影效果到卡片和容器 */
.bg-white, .dark\:bg-gray-800 {
  box-shadow: 0 4px 6px rgba(32, 66, 251, 0.05);
  transition: all 0.3s ease;
}

.bg-white:hover, .dark\:bg-gray-800:hover {
  box-shadow: 0 6px 12px rgba(32, 66, 251, 0.1);
}

/* 应用容器样式 */
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 天蓝色渐变背景 - 用于替换白色背景 */
.bg-skyblue-gradient {
  background: linear-gradient(120deg, #e0f7fa, #bbdefb, #b3e5fc);
  border-color: #b3e5fc !important;
}

/* 天蓝色渐变背景 - 深色版本 */
.bg-skyblue-gradient-dark {
  background: linear-gradient(120deg, #4fc3f7, #29b6f6, #03a9f4);
  border-color: #03a9f4 !important;
  color: white;
}

/* 替换所有bg-white类为天蓝色渐变背景 */
.bg-white {
  background: linear-gradient(120deg, #e0f7fa, #bbdefb, #b3e5fc) !important;
  border-color: #b3e5fc !important;
}
</style>
