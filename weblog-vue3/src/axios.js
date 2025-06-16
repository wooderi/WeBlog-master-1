import axios from "axios";
import { notification, showMessage } from '@/composables/util'
import { getToken } from '@/composables/auth'
import store from "@/store";
import { Console } from "windicss/utils";

const instance = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    // baseURL: '/',
    timeout: 30000  // 增加到30秒，AI摘要生成需要更长时间
});

// 添加请求拦截器
instance.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    const token = getToken()
    console.log('统一添加 token: ' + token)

    // 统一添加请求头 Token
    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
    }
    
    // Check if the data is FormData and set the appropriate content type
    if (config.data instanceof FormData) {
        // Don't set content-type for FormData to let the browser set the correct boundary
        delete config.headers['Content-Type'];
    } else {
        // 添加Content-Type和Accept头，确保UTF-8编码
        if (config.headers['Content-Type'] === undefined) {
            config.headers['Content-Type'] = 'application/json;charset=UTF-8';
        }
    }
    
    if (config.headers['Accept'] === undefined) {
        config.headers['Accept'] = 'application/json;charset=UTF-8';
    }

    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    return response.data;
}, function (error) {
    // 对响应错误做点什么
    console.log('请求错误:', error);
    
    if (error.response) {
        // 服务器返回了错误响应
        let status = error.response.status;
        console.log('错误响应状态码==========》' + status);
        
        if (status == 401 || status == 402) {
            console.log('401-------------');
            store.dispatch('logout').finally(() => location.reload());
        }
        
        if (error.response.data) {
            let isSuccess = error.response.data.success;
            console.log('错误响应成功标志==========》' + isSuccess);
            
            if (isSuccess === false) {
                console.log('error: ' + error.response.data.message);
                let message = error.response.data.message || '请求失败';
                showMessage(message, 'error');
            }
        }
    } else if (error.request) {
        // 请求已发送但没有收到响应
        console.log('请求未收到响应:', error.request);
        showMessage('网络错误，未收到服务器响应', 'error');
    } else {
        // 请求设置时发生错误
        console.log('请求设置错误:', error.message);
        showMessage('请求设置错误: ' + error.message, 'error');
    }

    return Promise.reject(error);
});

// 暴露
export default instance