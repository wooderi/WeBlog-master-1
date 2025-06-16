<template>
  <img 
    :src="processedSrc" 
    :alt="alt" 
    :class="className"
    @error="handleImageError" 
    v-bind="$attrs"
  />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { fixMinioImageUrl, handleImageError as minioHandleImageError } from '@/composables/minioImageFixer';

const props = defineProps({
  src: {
    type: String,
    required: true
  },
  alt: {
    type: String,
    default: ''
  },
  className: {
    type: String,
    default: ''
  },
  fallbackSrc: {
    type: String,
    default: ''
  }
});

// 使用计算属性处理图片URL
const processedSrc = computed(() => {
  return fixMinioImageUrl(props.src);
});

// 错误处理函数
const handleImageError = (event) => {
  // 如果提供了备用图片，则使用备用图片
  if (props.fallbackSrc) {
    event.target.src = props.fallbackSrc;
    return;
  }
  
  // 否则使用通用错误处理
  minioHandleImageError(event);
};
</script> 