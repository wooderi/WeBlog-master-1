<template>
    <Header></Header>

    <div class="container mx-auto max-w-screen-xl mt-8">
        <div class="grid grid-cols-4">
            <!-- 左边栏 -->
            <div class="col-span-4 px-3 md:col-span-3 sm:col-span-4">
                <div class="content-card">
                    <h2 class="page-title">分类</h2>
                    
                    <div class="category-grid">
                        <a @click="goCatagoryArticleListPage(item.id, item.name)" 
                           v-for="(item, index) in categories" 
                           :key="index"
                           class="category-card"
                           :style="`background: linear-gradient(135deg, ${getGradientColor(index)} 0%, #ffffff 100%);`">
                            <div class="category-icon-wrapper" 
                                 :style="`background: linear-gradient(135deg, ${getGradientColor(index)} 0%, #ffffff 100%);`">
                                <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true"
                                    xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 21 18">
                                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                        stroke-width="0.9"
                                        d="M2.539 17h12.476l4-9H5m-2.461 9a1 1 0 0 1-.914-1.406L5 8m-2.461 9H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.443a1 1 0 0 1 .8.4l2.7 3.6H16a1 1 0 0 1 1 1v2H5" />
                                </svg>
                            </div>
                            <span class="category-name">{{ item.name }}</span>
                            <div class="category-decoration mt-2 w-12 h-1 rounded-full mx-auto" 
                                 :style="`background: ${getGradientColor(index)};`"></div>
                        </a>
                    </div>
                </div>
            </div>
            <!-- 右边栏 -->
            <div class="col-span-4 px-3 md:col-span-1 sm:col-span-4">
                <UserInfoCard></UserInfoCard>
            </div>
        </div>
    </div>

    <Footer></Footer>
</template>

<script setup>
import Header from '@/layouts/components/Header.vue'
import Footer from '@/layouts/components/Footer.vue'
import UserInfoCard from '@/components/UserInfoCard.vue'
import { useRouter } from 'vue-router'
import { getCategories } from '@/api/frontend/category'
import { ref } from 'vue'

const router = useRouter()

const goCatagoryArticleListPage = (id, name) => {
    router.push({ path: '/category/list', query: { id: id, name: name } })
}

const categories = ref([])
getCategories().then((e) => {
    if (e.success) {
        // 打印API返回的数据结构，查看每个分类对象的字段
        console.log('Categories API response:', e.data);
        if (e.data && e.data.length > 0) {
            console.log('First category object:', e.data[0]);
        }
        categories.value = e.data
    }
})

// 为分类卡片生成渐变色
const getGradientColor = (index) => {
    const colors = [
        '#4ADE80', '#06B6D4', '#6366F1', '#EC4899', 
        '#F59E0B', '#10B981', '#3B82F6', '#8B5CF6',
        '#EF4444', '#F97316', '#84CC16', '#14B8A6'
    ];
    return colors[index % colors.length];
}
</script>

<style scoped>
.category-title::after {
    content: '';
    position: absolute;
    bottom: -8px;
    left: 50%;
    transform: translateX(-50%);
    width: 60px;
    height: 3px;
    background: linear-gradient(90deg, #3B82F6, #8B5CF6);
    border-radius: 3px;
}

.category-card {
    border: 1px solid rgba(229, 231, 235, 0.8);
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.category-icon-wrapper {
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
}

@media (max-width: 640px) {
    .category-grid {
        grid-template-columns: 1fr;
    }
}
</style>