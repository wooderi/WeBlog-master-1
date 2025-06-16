import axios from "axios";
import { notification, showMessage } from '@/composables/util'
import { getToken } from '@/composables/auth'
import store from "@/store";
import { Console } from "windicss/utils";

const instance = axios.create({
    baseURL: import.meta.env.VITE_APP_BASE_API,
    // baseURL: '/',
    timeout: 30000  // ���ӵ�30�룬AIժҪ������Ҫ����ʱ��
});

// �������������
instance.interceptors.request.use(function (config) {
    // �ڷ�������֮ǰ��Щʲô
    const token = getToken()
    console.log('ͳһ��� token: ' + token)

    // ͳһ�������ͷ Token
    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token
    }
    
    // Check if the data is FormData and set the appropriate content type
    if (config.data instanceof FormData) {
        // Don't set content-type for FormData to let the browser set the correct boundary
        delete config.headers['Content-Type'];
    } else {
        // ���Content-Type��Acceptͷ��ȷ��UTF-8����
        if (config.headers['Content-Type'] === undefined) {
            config.headers['Content-Type'] = 'application/json;charset=UTF-8';
        }
    }
    
    if (config.headers['Accept'] === undefined) {
        config.headers['Accept'] = 'application/json;charset=UTF-8';
    }

    return config;
}, function (error) {
    // �����������Щʲô
    return Promise.reject(error);
});

// �����Ӧ������
instance.interceptors.response.use(function (response) {
    // ����Ӧ��������ʲô
    return response.data;
}, function (error) {
    // ����Ӧ��������ʲô
    console.log('�������:', error);
    
    if (error.response) {
        // �����������˴�����Ӧ
        let status = error.response.status;
        console.log('������Ӧ״̬��==========��' + status);
        
        if (status == 401 || status == 402) {
            console.log('401-------------');
            store.dispatch('logout').finally(() => location.reload());
        }
        
        if (error.response.data) {
            let isSuccess = error.response.data.success;
            console.log('������Ӧ�ɹ���־==========��' + isSuccess);
            
            if (isSuccess === false) {
                console.log('error: ' + error.response.data.message);
                let message = error.response.data.message || '����ʧ��';
                showMessage(message, 'error');
            }
        }
    } else if (error.request) {
        // �����ѷ��͵�û���յ���Ӧ
        console.log('����δ�յ���Ӧ:', error.request);
        showMessage('�������δ�յ���������Ӧ', 'error');
    } else {
        // ��������ʱ��������
        console.log('�������ô���:', error.message);
        showMessage('�������ô���: ' + error.message, 'error');
    }

    return Promise.reject(error);
});

// ��¶
export default instance