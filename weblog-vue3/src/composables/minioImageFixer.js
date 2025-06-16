/**
 * Minio图片URL修复工具
 * 解决Minio图片端口不一致问题，确保前端能正确显示图片
 */

/**
 * 修复Minio图片URL
 * 如果URL包含9000端口但图片无法显示，尝试替换为9005端口
 * 如果URL包含9005端口但图片无法显示，尝试替换为9000端口
 * @param {string} url 原始图片URL
 * @returns {string} 修复后的URL
 */
export function fixMinioImageUrl(url) {
  if (!url) return url;
  
  // 检查URL是否是Minio图片URL（包含weblog路径）
  if (url.includes('/weblog/')) {
    // 尝试从9000端口切换到9005端口
    if (url.includes(':9000/')) {
      console.log('修复Minio图片URL：9000 -> 9005', url);
      return url.replace(':9000/', ':9005/');
    }
    
    // 尝试从9005端口切换到9000端口
    if (url.includes(':9005/')) {
      console.log('修复Minio图片URL：9005 -> 9000', url);
      return url.replace(':9005/', ':9000/');
    }
  }
  
  return url;
}

/**
 * 懒加载图片处理，在图片加载失败时尝试使用另一个端口
 * @param {Event} event 图片加载失败事件
 */
export function handleImageError(event) {
  const img = event.target;
  const originalSrc = img.src;
  
  // 防止无限重试
  if (img.dataset.retried) {
    console.log('图片已尝试过修复，但仍然失败', originalSrc);
    return;
  }
  
  // 标记已尝试修复
  img.dataset.retried = 'true';
  
  // 尝试修复URL
  if (originalSrc.includes(':9000/')) {
    img.src = originalSrc.replace(':9000/', ':9005/');
    console.log('图片加载失败，尝试使用9005端口', img.src);
  } else if (originalSrc.includes(':9005/')) {
    img.src = originalSrc.replace(':9005/', ':9000/');
    console.log('图片加载失败，尝试使用9000端口', img.src);
  }
}

/**
 * 全局图片URL修复指令（用于Vue指令）
 */
export const MinioImageDirective = {
  mounted(el, binding) {
    // 如果是img标签
    if (el.tagName === 'IMG') {
      // 备份原始src
      const originalSrc = el.getAttribute('src');
      
      // 设置修复后的URL
      if (originalSrc) {
        el.setAttribute('src', fixMinioImageUrl(originalSrc));
      }
      
      // 添加错误处理
      el.addEventListener('error', handleImageError);
    }
    
    // 处理背景图片
    const style = getComputedStyle(el);
    if (style.backgroundImage) {
      const match = style.backgroundImage.match(/url\(['"]?([^'"]+)['"]?\)/);
      if (match && match[1]) {
        const originalUrl = match[1];
        const fixedUrl = fixMinioImageUrl(originalUrl);
        if (originalUrl !== fixedUrl) {
          el.style.backgroundImage = `url('${fixedUrl}')`;
        }
      }
    }
  },
  
  // 更新时再次检查
  updated(el, binding) {
    if (el.tagName === 'IMG') {
      const currentSrc = el.getAttribute('src');
      el.setAttribute('src', fixMinioImageUrl(currentSrc));
    }
  },
  
  // 组件卸载前移除事件监听
  beforeUnmount(el) {
    if (el.tagName === 'IMG') {
      el.removeEventListener('error', handleImageError);
    }
  }
};

export default {
  fixMinioImageUrl,
  handleImageError,
  MinioImageDirective
}; 