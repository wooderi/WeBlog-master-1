<template>
    <div class="page-container">
        <Header></Header>

        <main class="main-content">
            <div class="container mx-auto max-w-screen-xl mt-8">
                <div class="grid grid-cols-4">
                    <!-- 左边栏 -->
                    <div class="col-span-4 px-3 md:col-span-3 sm:col-span-4">
                        <div class="content-card">
                            <h2 class="page-title">标签</h2>
                            
                            <div class="tag-cloud">
                                <div v-for="(item, index) in tags" 
                                    :key="index"
                                    @click="goTagArticleListPage(item.id, item.name)"
                                    class="tag-item"
                                    :style="`background: ${getTagColor(index)}; color: ${getTagTextColor(index)}; font-size: ${getTagSize(index+1)}px;`">
                                    <span class="tag-name">{{ item.name }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 右边栏 -->
                    <div class="col-span-4 px-3 md:col-span-1 sm:col-span-4">
                        <UserInfoCard></UserInfoCard>
                    </div>
                </div>
            </div>
        </main>

        <Footer></Footer>
    </div>
</template>

<script setup>
import Header from '@/layouts/components/Header.vue'
import Footer from '@/layouts/components/Footer.vue'
import UserInfoCard from '@/components/UserInfoCard.vue'
import { useRouter } from 'vue-router'
import { getTags } from '@/api/frontend/tag'
import { ref } from 'vue'

const router = useRouter()

const goTagArticleListPage = (id, name) => {
    router.push({path: '/tag/list', query: {id: id, name: name}})
}

const tags = ref([])
getTags().then((e) => {
    if (e.success) {
        // 打印API返回的数据结构，查看每个标签对象的字段
        console.log('Tags API response:', e.data);
        if (e.data && e.data.length > 0) {
            console.log('First tag object:', e.data[0]);
        }
        tags.value = e.data
    }
})

// 根据索引生成标签背景颜色
const getTagColor = (index) => {
    const colors = [
        '#E9F5FE', '#EEF2FF', '#FEF3F2', '#F0FDF4', 
        '#FEF9C3', '#ECFDF5', '#F1F5F9', '#F5F3FF',
        '#FCE7F3', '#FFF7ED', '#F0FDFA', '#EFF6FF'
    ];
    return colors[index % colors.length];
}

// 根据索引生成标签文字颜色
const getTagTextColor = (index) => {
    const colors = [
        '#0369A1', '#4F46E5', '#DC2626', '#16A34A', 
        '#CA8A04', '#059669', '#334155', '#7C3AED',
        '#DB2777', '#EA580C', '#0F766E', '#2563EB'
    ];
    return colors[index % colors.length];
}

// 根据索引计算标签大小，保证随机性但一致性
const getTagSize = (index) => {
    // 基础大小为14px，最大不超过22px
    const baseSize = 14;
    const maxSize = 22;
    
    // 使用索引创建一个一致的变化
    const variationFactor = (index % 5) + 1; // 1到5之间的变化
    
    // 计算大小
    const size = Math.min(baseSize + variationFactor, maxSize);
    return size;
}
</script>

<style scoped>
.page-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.main-content {
    flex: 1;
    padding-bottom: 2rem;
}

.tag-title::after {
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

.tag-item {
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
    display: inline-flex;
    align-items: center;
    justify-content: center;
    margin: 0.5rem;
    padding: 0.5rem 1rem;
    border-radius: 0.5rem;
    cursor: pointer;
    transition: all 0.3s ease;
}

.tag-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.tag-cloud {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    padding: 1rem 0;
}

.tag-count {
    display: inline-flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
    min-width: 18px;
    height: 18px;
}

.page-title {
    font-size: 1.5rem;
    font-weight: 600;
    margin-bottom: 1.5rem;
    text-align: center;
    color: var(--el-color-primary);
}

.content-card {
    background: linear-gradient(120deg, #e0f7fa, #bbdefb, #b3e5fc);
    border-radius: 0.5rem;
    padding: 1.5rem;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    border: 1px solid #b3e5fc;
}

@media (max-width: 640px) {
    .tag-cloud {
        justify-content: flex-start;
    }
}
</style>