import axios from "@/axios"

export function getArticleDetail(articleId) {
    return axios.post("/article/detail", {articleId})
}

export function generateArticleSummary(articleId) {
    // 确保articleId为数值类型
    return axios.post(`/article/${articleId}/summary`)
}


