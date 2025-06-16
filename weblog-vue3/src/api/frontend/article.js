import axios from "@/axios"

export function getArticleDetail(articleId) {
    return axios.post("/article/detail", {articleId})
}

export function generateArticleSummary(articleId) {
    // ȷ��articleIdΪ��ֵ����
    return axios.post(`/article/${articleId}/summary`)
}


