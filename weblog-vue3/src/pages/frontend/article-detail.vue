<template>
    <Header></Header>

    <!-- 文章详情 -->
    <div class="container mx-auto max-w-screen-xl mt-5 mb-3">
        <div class="grid grid-cols-4">
            <!-- 左边栏 -->
            <div class="col-span-4 px-3 mb-3 md:col-span-3 sm:col-span-4">
                <div class="bg-white border border-gray-200 p-5 rounded-lg dark:bg-gray-800 dark:border-gray-700">
                    <!-- 面包屑 -->
                    <nav class="flex mb-4" aria-label="Breadcrumb">
                        <ol class="inline-flex items-center space-x-1 md:space-x-3">
                            <li class="inline-flex items-center">
                                <a @click="router.push('/')"
                                    class="cursor-pointer inline-flex items-center text-sm font-medium text-gray-500 hover:text-blue-600 dark:text-gray-400 dark:hover:text-white">
                                    <svg class="w-3 h-3 mr-2.5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg"
                                        fill="currentColor" viewBox="0 0 20 20">
                                        <path
                                            d="m19.707 9.293-2-2-7-7a1 1 0 0 0-1.414 0l-7 7-2 2a1 1 0 0 0 1.414 1.414L2 10.414V18a2 2 0 0 0 2 2h3a1 1 0 0 0 1-1v-4a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v4a1 1 0 0 0 1 1h3a2 2 0 0 0 2-2v-7.586l.293.293a1 1 0 0 0 1.414-1.414Z" />
                                    </svg>
                                    首页
                                </a>
                            </li>
                            <li aria-current="page">
                                <div class="flex items-center text-gray-400">
                                    /
                                    <span
                                        class="ml-1 text-sm font-medium text-gray-500 md:ml-4 dark:text-gray-400">正文</span>
                                </div>
                            </li>
                        </ol>
                    </nav>


                    <!-- 文章主体 -->
                    <article>
                        <h1 class="title mt-2">{{ article.title }}</h1>
                        <div class="text-gray-400 text-sm flex items-center article-mata">
                            <svg class="inline w-3 h-3 mr-2 text-gray-400 dark:text-white" aria-hidden="true"
                                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 20">
                                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M5 1v3m5-3v3m5-3v3M1 7h18M5 11h10M2 3h16a1 1 0 0 1 1 1v14a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Z" />
                            </svg>
                            发表于 {{ article.updateTime }}

                            <svg class="inline w-3 h-3 ml-5 mr-2 text-gray-400 dark:text-white" aria-hidden="true"
                                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 18 18">
                                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M1 5v11a1 1 0 0 0 1 1h14a1 1 0 0 0 1-1V6a1 1 0 0 0-1-1H1Zm0 0V2a1 1 0 0 1 1-1h5.443a1 1 0 0 1 .8.4l2.7 3.6H1Z" />
                            </svg>
                            分类于&nbsp;<a @click="goCatagoryArticleListPage(article.categoryId, article.categoryName)"
                                class="text-gray-500 hover:underline">{{ article.categoryName }}</a>

                            <svg class="inline w-3 h-3 ml-5 mr-2 text-gray-400 dark:text-white" aria-hidden="true"
                                xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 14">
                                <g stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                                    <path d="M10 10a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z" />
                                    <path d="M10 13c4.97 0 9-2.686 9-6s-4.03-6-9-6-9 2.686-9 6 4.03 6 9 6Z" />
                                </g>
                            </svg> 阅读量 {{ article.readNum }}
                        </div>

                        <div class="article-content" v-viewer v-html="article.content" v-highlight>
                        </div>
                        
                        <!-- 文章摘要 -->
                        <div v-if="summary" class="mt-6 mb-6 p-6 text-sm rounded-lg bg-blue-50 dark:bg-gray-800 dark:text-blue-400 border border-blue-100 dark:border-gray-700" role="alert">
                            <div class="font-medium mb-3 text-blue-800 dark:text-blue-400 text-base border-b border-blue-100 dark:border-gray-700 pb-2">
                                <svg class="w-5 h-5 inline-block mr-2 -mt-1" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0015 18c-2.305 0-4.408.867-6 2.292m0-14.25v14.25" />
                                </svg>
                                文章摘要
                            </div>
                            <div class="summary-content" v-html="formattedSummary"></div>
                        </div>
                        
                        <div class="mt-4 mb-4">
                            <button @click="generateSummary" 
                                    :disabled="isSummaryLoading"
                                    class="inline-flex items-center px-4 py-2 text-sm font-medium text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                                <svg v-if="isSummaryLoading" class="w-4 h-4 mr-2 text-white animate-spin" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                </svg>
                                <svg v-else class="w-4 h-4 mr-2 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z" />
                                </svg>
                                {{ isSummaryLoading ? '生成中...' : '自动生成摘要' }}
                            </button>
                        </div>
                        
                        <!-- 标签 -->
                        <div class="mt-5 mb-5">
                            <div @click="goTagArticleListPage(item.id, item.name)" v-for="(item, index) in article.tags"
                                :key="index"
                                class="inline-block rounded-full bg-green-100 text-green-800 text-sm font-medium mr-3 mb-2 px-2.5 py-0.5 rounded dark:bg-green-900 dark:text-green-300 hover:bg-green-200 hover:text-green-900">
                                # {{ item.name }}
                            </div>
                        </div>
                    </article>
                    <!-- 上下篇 -->
                    <div class="article-footer flex">
                        <div class="cursor-pointer">
                            <a v-if="article.preArticleId" @click="goArticleDetail(article.preArticleId)">
                                <span class="desc">
                                    <svg class="inline w-2 h-2 mr-1 mb-3px text-gray-500 dark:text-white" aria-hidden="true"
                                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 8 14">
                                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                            stroke-width="2" d="M7 1 1.3 6.326a.91.91 0 0 0 0 1.348L7 13" />
                                    </svg>
                                    上一篇</span>
                                <span
                                    class="hover:text-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-700 focus:text-blue-700">{{
                                        article.preArticleTitle }}</span>
                            </a>
                        </div>
                        <div class="cursor-pointer">
                            <a v-if="article.nextArticleId" @click="goArticleDetail(article.nextArticleId)">
                                <span class="desc">
                                    下一篇
                                    <svg class="inline w-2 h-2 ml-1 mb-3px text-gray-500 dark:text-white" aria-hidden="true"
                                        xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 8 14">
                                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                            stroke-width="2" d="m1 13 5.7-5.326a.909.909 0 0 0 0-1.348L1 1" />
                                    </svg>
                                </span>
                                <span
                                    class="hover:text-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-700 focus:text-blue-700">{{
                                        article.nextArticleTitle }}</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 右边栏 -->
            <div class="col-span-4 px-3 md:col-span-1 sm:col-span-4">
                <div class="sticky top-21">
                    <UserInfoCard></UserInfoCard>

                    <!-- 文章分类 -->
                    <div
                        class="mb-3 w-full font-medium p-5 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700">
                        <h2 class="mb-2 font-bold text-gray-900 uppercase dark:text-white">分类</h2>
                        <div
                            class="text-sm font-medium text-gray-900 bg-white rounded-lg dark:bg-gray-700 dark:border-gray-600 dark:text-white">
                            <a @click="goCatagoryArticleListPage(item.id, item.name)" v-for="(item, index) in categories"
                                :key="index"
                                class="flex items-end block w-full px-4 py-2 rounded-lg cursor-pointer hover:bg-gray-100 hover:text-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-700 focus:text-blue-700 dark:border-gray-600 dark:hover:bg-gray-600 dark:hover:text-white dark:focus:ring-gray-500 dark:focus:text-white">
                                <svg class="w-4 h-4 mr-2 mb-2px text-gray-800 inline dark:text-white" aria-hidden="true"
                                    xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 21 18">
                                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
                                        stroke-width="0.9"
                                        d="M2.539 17h12.476l4-9H5m-2.461 9a1 1 0 0 1-.914-1.406L5 8m-2.461 9H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.443a1 1 0 0 1 .8.4l2.7 3.6H16a1 1 0 0 1 1 1v2H5" />
                                </svg>
                                {{ item.name }}
                            </a>
                        </div>
                    </div>

                    <!-- 文章标签 -->
                    <div
                        class="mb-3 w-full font-medium p-5 bg-white border border-gray-200 rounded-lg dark:bg-gray-800 dark:border-gray-700">
                        <h2 class="mb-2 font-bold text-gray-900 uppercase dark:text-white">标签</h2>
                        <div @click="goTagArticleListPage(item.id, item.name)" v-for="(item, index) in tags" :key="index"
                            class="inline-block bg-green-100 text-green-800 text-xs font-medium mr-2 mb-1 px-2.5 py-0.5 rounded hover:bg-green-200 hover:text-green-900 dark:hover:bg-green-800 dark:hover:text-green-300 dark:bg-green-900 dark:text-green-300">
                            {{ item.name }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <Footer></Footer>
</template>

<script setup>
import Header from '@/layouts/components/Header.vue'
import Footer from '@/layouts/components/Footer.vue'
import UserInfoCard from '@/components/UserInfoCard.vue'
import { useRoute, useRouter } from 'vue-router';
import { getArticleDetail } from '@/api/frontend/article';
import { ref, reactive, computed, watch } from 'vue'
import { getCategories } from '@/api/frontend/category'
import { getTags } from '@/api/frontend/tag'
import { generateArticleSummary } from '@/api/frontend/article';
import '@/assets/css/summary.css'
import { handleImageError } from '@/composables/minioImageFixer';

const router = useRouter()
const route = useRoute()
const article = reactive({
    title: '',
    content: '',
    updateTime: '',
    readNum: 0,
    categoryId: null,
    categoryName: '',
    preArticleId: null,
    preArticleTitle: '',
    nextArticleId: null,
    nextArticleTitle: '',
    tags: [],
    titleImage: '',
})

function queryArticleDetail(articleId) {
    console.log('调用获取详情接口...' + route.query.articleId)
    getArticleDetail(articleId).then((e) => {
        article.title = e.data.title
        article.content = e.data.content
        article.updateTime = e.data.updateTime
        article.categoryId = e.data.categoryId
        article.categoryName = e.data.categoryName
        article.readNum = e.data.readNum
        article.tags = e.data.tags
        
        // 处理标题图片URL
        if (e.data.titleImage) {
            // 尝试修复标题图片URL
            if (e.data.titleImage.includes(':9000/')) {
                article.titleImage = e.data.titleImage.replace(':9000/', ':9005/');
                console.log('修复标题图片URL: 9000 -> 9005', article.titleImage);
            } else if (e.data.titleImage.includes(':9005/')) {
                article.titleImage = e.data.titleImage.replace(':9005/', ':9000/');
                console.log('修复标题图片URL: 9005 -> 9000', article.titleImage);
            } else {
                article.titleImage = e.data.titleImage;
            }
        }
        
        if (e.data.preArticle) {
            console.log('上一篇...')
            console.log(e.data.preArticle)
            article.preArticleId = e.data.preArticle.id
            article.preArticleTitle = e.data.preArticle.title
        } else {
            article.preArticleId = null
        }

        if (e.data.nextArticle) {
            article.nextArticleId = e.data.nextArticle.id
            article.nextArticleTitle = e.data.nextArticle.title
        } else {
            article.nextArticleId = null
        }
    })
}
queryArticleDetail(route.query.articleId);

const goArticleDetail = (articleId) => {
    console.log('跳转详情页' + articleId)
    router.push({ path: '/article/detail', query: { articleId: articleId } })
    queryArticleDetail(articleId)
}


// 获取分类
const categories = ref([])
getCategories().then((e) => {
    console.log('获取分类数据')
    console.log(e)
    categories.value = e.data
})


// 获取标签
const tags = ref([])
getTags().then((e) => {
    console.log('获取标签数据')
    console.log(e)
    tags.value = e.data
})

const goCatagoryArticleListPage = (id, name) => {
    router.push({ path: '/category/list', query: { id: id, name: name } })
}

const goTagArticleListPage = (id, name) => {
    router.push({ path: '/tag/list', query: { id: id, name: name } })
}

const summary = ref('');
const isSummaryLoading = ref(false);

const formattedSummary = computed(() => {
  if (!summary.value) return '';
  
  // Format the summary with better styling
  let formatted = summary.value
    // Convert headers
    .replace(/^#\s+(.*?)$/gm, '<h2 class="text-lg font-bold mb-2 mt-3">$1</h2>')
    .replace(/^##\s+(.*?)$/gm, '<h3 class="text-base font-semibold mb-2 mt-2">$1</h3>')
    .replace(/^###\s+(.*?)$/gm, '<h4 class="font-medium mb-1 mt-1">$1</h4>')
    
    // Format bullet points
    .replace(/^(\s*)-\s+(.*?)$/gm, '<div class="flex mb-1"><span class="mr-2">•</span><span>$2</span></div>')
    
    // Format numbered lists
    .replace(/^(\s*)(\d+)\.\s+(.*?)$/gm, '<div class="flex mb-1"><span class="mr-2 text-blue-700">$2.</span><span>$3</span></div>')
    
    // Format code sections
    .replace(/```(.*?)```/gs, '<pre class="bg-gray-100 p-2 my-2 rounded text-sm overflow-auto dark:bg-gray-700">$1</pre>')
    .replace(/`(.*?)`/g, '<code class="px-1 py-0.5 bg-gray-100 rounded text-sm dark:bg-gray-700">$1</code>')
    
    // Format bold text
    .replace(/\*\*(.*?)\*\*/g, '<strong class="text-blue-700 dark:text-blue-400">$1</strong>')
    
    // Format paragraphs
    .replace(/^([^<].*?)$/gm, '<p class="mb-2">$1</p>')
    
    // Format blockquotes
    .replace(/^>\s+(.*?)$/gm, '<blockquote class="border-l-4 border-blue-300 pl-3 py-1 my-2 text-gray-600 dark:text-gray-400">$1</blockquote>');
  
  return formatted;
});

const generateSummary = () => {
    if (isSummaryLoading.value) return;
    
    isSummaryLoading.value = true;
    summary.value = '';
    
    const articleId = route.query.articleId;
    
    generateArticleSummary(articleId)
        .then((res) => {
            console.log('摘要生成响应:', res);
            // 检查响应格式
            if (res && res.success === true && res.data) {
                summary.value = res.data.summary;
            } else {
                console.error('生成摘要响应格式异常', res);
                summary.value = '摘要生成失败: ' + (res?.message || '返回数据格式异常');
            }
        })
        .catch((err) => {
            console.error('生成摘要请求错误', err);
            summary.value = '摘要生成失败: ' + (err.message || '请求错误');
        })
        .finally(() => {
            isSummaryLoading.value = false;
        });
};

// 添加图片错误处理函数
const processArticleContent = (content) => {
    // 当文章内容加载完成后，为所有图片添加错误处理
    setTimeout(() => {
        const articleContent = document.querySelector('.article-content');
        if (articleContent) {
            const images = articleContent.querySelectorAll('img');
            images.forEach(img => {
                // 为图片添加错误处理
                img.addEventListener('error', handleImageError);
                
                // 如果图片URL包含特定端口，尝试预先修复
                if (img.src && (img.src.includes(':9000/') || img.src.includes(':9005/'))) {
                    const originalSrc = img.src;
                    if (img.src.includes(':9000/')) {
                        img.src = originalSrc.replace(':9000/', ':9005/');
                    } else if (img.src.includes(':9005/')) {
                        img.src = originalSrc.replace(':9005/', ':9000/');
                    }
                }
            });
        }
    }, 500); // 给DOM一些时间来渲染
}

// 监听文章内容变化
watch(() => article.content, (newContent) => {
    if (newContent) {
        processArticleContent(newContent);
    }
});

</script>

<style scoped>
.title {
    padding-bottom: 20px;
    margin-bottom: 0;
    line-height: 1.3;
    word-wrap: break-word;
    font-size: 32px;
    font-weight: 700;
    color: #292525;
}

.article-mata {
    margin-bottom: 20px;
}

:deep(pre) {
    background: #21252b;
    color: #f8f8f2;
    border-radius: 5px;
    padding: 10px 0 0;
    font-size: 17px;
    padding-left: 15px;
}

:deep(pre code.hljs) {
    display: block;
    overflow-x: auto;
    padding: 1em;
    padding-left: 0!important;
    padding-top: 25px!important;
}

:deep(pre:before) {
    background: #fc625d;
    border-radius: 50%;
    box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b;
    content: ' ';
    height: 10px;
    margin-top: 5px;
    position: absolute;
    width: 10px;
}

:deep(.article-content p) {
    letter-spacing: 0.3px;
    margin: 0 0 20px 0;
    line-height: 30px;
    color: #4c4e4d;
    font-weight: 400;
    word-break: normal;
    word-wrap: break-word;
    font-family: -apple-system, BlinkMacSystemFont, PingFang SC, Hiragino Sans GB, Microsoft Yahei, Arial, sans-serif;
}

:deep(.article-content h1, .article-content h2, .article-content h3, .article-content h4, .article-content h5, .article-content h6)  {
    margin: 30px 0 10px 0;
    color: #292525;
    line-height: 150%;
    font-family: PingFang SC,Helvetica Neue,Helvetica,Hiragino Sans GB,Microsoft YaHei,"\5FAE\8F6F\96C5\9ED1",Arial,sans-serif;
}

:deep(.article-content h3) {
    font-size: 20px;
    margin-top: 40px;
    margin-bottom: 16px;
    font-weight: 600;
}

:deep(.image-caption) {
    min-width: 20%;
    max-width: 80%;
    min-height: 43px;
    display: block;
    padding: 10px;
    margin: 0 auto;
    /* border-bottom: 1px solid #eee; */
    font-size: 13px;
    color: #999;
    text-align: center;
}

:deep(code:not(pre code)) {
    padding: 2px 4px;
    margin: 0 2px;
    font-size: 95%!important;
    border-radius: 4px;
    color: rgb(41, 128, 185);
    background-color: rgba(27, 31, 35, 0.05);
    font-family: Operator Mono, Consolas, Monaco, Menlo, monospace;
}

:deep(pre code) {
    display: block;
    font-size: 95% !important;
    background-color: rgba(27, 31, 35, 0.05);
    font-family: Operator Mono, Consolas, Monaco, Menlo, monospace;
    /* color: #fff; */
}

:deep(article ul) {
    padding-left: 40px;
}

:deep(article ul li) {
    list-style-type: disc;
    padding-top: 5px;
    padding-bottom: 5px;
    font-size: 16px;
}



:deep(blockquote) {
    /* margin: 20px 0; */
    border-left: 2.3px solid rgb(52, 152, 219);
    quotes: none;
    background: rgb(236, 240, 241);
    color: #777;
    font-size: 16px;
    /* padding: 10px 15px 10px 15px; */
    margin: 2em 0;
    padding: 24px 24px;
    position: relative;
}

:deep(blockquote p:last-child) {
    margin-bottom: 0;
}

:deep(table tr) {
    background-color: #fff;
    border-top: 1px solid #c6cbd1;
}

:deep(table) {
    border-collapse: collapse;
    margin-bottom: 1rem;
}

:deep(table th) {
    padding: 6px 13px;
    border: 1px solid #dfe2e5;
}

:deep(table td) {
    padding: 6px 13px;
    border: 1px solid #dfe2e5;
}

:deep(.article-content a) {
    color: #167bc2;
}

:deep(.article-content h2) {
    /* margin: 1em auto; */
    font-size: 22px;
    line-height: 1.5;
    font-weight: bold;
    font-synthesis: style;
    /* border-bottom: 1px solid rgba(0,0,0,.1); */
    padding-bottom: 16px;
    /* border-left: 3px solid #167bc2; */
    padding-bottom: 0;
    font-size: 24px;
    margin-top: 40px;
    margin-bottom: 26px;
    line-height: 140%;
    border-bottom: 1px solid #e5e5e5;
    padding-bottom: 15px;
}

:deep(.article-content svg) {
    display: inline;
}

:deep(.article-content img) {
    position: relative;
    max-width: 100%;
    overflow: hidden;
    display: block;
    margin: 0 auto;
    cursor: -webkit-zoom-in;
    cursor: zoom-in;
}

:deep(strong) {
    color: rgb(52, 152, 219);
}

:deep(table tr:nth-child(2n)) {
    background-color: #f6f8fa;
}

.el-breadcrumb__inner a {
    font-weight: 400;
    color: #606266;
}

.el-breadcrumb__inner a:hover {
    font-weight: 400;
    color: #606266;
    text-decoration: underline;
}

.article-footer {
    border-top: 1px solid #e4e7ed;
    background-color: var(--el-fill-color-blank);
    justify-content: space-between;
    padding-top: 1rem;
}

.desc {
    display: block;
    font-size: 12px;
    color: rgba(60, 60, 60, .7);
    ;
}

.cursor-pointer {
    cursor: pointer;
}

.category-item:hover {
    text-decoration: underline;
    cursor: pointer;
}

.tag-item:hover {
    cursor: pointer;
}

.el-tag:hover {
    background-color: var(--el-color-info-light-8);
}</style>