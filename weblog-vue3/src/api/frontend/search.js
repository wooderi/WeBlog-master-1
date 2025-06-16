import axios from "@/axios"

/**
 * 搜索文章
 * @param {*} data 
 * @returns 
 */
export function searchArticles(data) {
    return axios.post('/article/search', data)
} 