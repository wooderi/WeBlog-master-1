<template>
    <el-card shadow="never" :body-style="{ padding: '20px' }">
        <el-form :model="form" label-width="160px" :rules="rules">
            <el-form-item label="博客名称" prop="blogName">
                <el-input v-model="form.blogName" clearable />
            </el-form-item>
            <el-form-item label="作者名" prop="author">
                <el-input v-model="form.author" clearable />
            </el-form-item>
            <el-form-item label="作者头像" prop="avatar">
                <el-upload class="avatar-uploader" action="#" :on-change="handleTitleImageChange" :auto-upload="false"
                    :show-file-list="false">
                    <MinioImage 
                        v-if="form.avatar" 
                        :src="form.avatar" 
                        :alt="'作者头像'" 
                        className="avatar" 
                    />
                    <el-icon v-else class="avatar-uploader-icon">
                        <Plus />
                    </el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="介绍语">
                <el-input v-model="form.introduction" type="textarea" />
            </el-form-item>
            <el-form-item label="开启 GihHub 访问">
                <el-switch v-model="isGithubCheck" inline-prompt :active-icon="Check" :inactive-icon="Close" @change="githubSwitchChange"/>
            </el-form-item>
            <el-form-item label="GitHub 主页访问地址" v-if="isGithubCheck">
                <el-input v-model="form.githubHome" clearable placeholder="请输入 GitHub 主页访问的 URL" />
            </el-form-item>
            <el-form-item label="开启 CSDN 访问">
                <el-switch v-model="isCSDNCheck" inline-prompt :active-icon="Check" :inactive-icon="Close" @change="csdnSwitchChange"/>
            </el-form-item>
            <el-form-item label="CSDN 主页访问地址" v-if="isCSDNCheck">
                <el-input v-model="form.csdnHome" clearable placeholder="请输入 CSDN 主页访问的 URL" />
            </el-form-item>
            <el-form-item label="开启 Gitee 访问">
                <el-switch v-model="isGiteeCheck" inline-prompt :active-icon="Check" :inactive-icon="Close" @change="giteeSwitchChange"/>
            </el-form-item>
            <el-form-item label="Gitee 主页访问地址" v-if="isGiteeCheck">
                <el-input v-model="form.giteeHome" clearable placeholder="请输入 Gitee 主页访问的 URL" />
            </el-form-item>
            <el-form-item label="开启知乎访问">
                <el-switch v-model="isZhihuCheck" inline-prompt :active-icon="Check" :inactive-icon="Close" @change="zhihuSwitchChange"/>
            </el-form-item>
            <el-form-item label="知乎主页访问地址" v-if="isZhihuCheck">
                <el-input v-model="form.zhihuHome" clearable placeholder="请输入知乎主页访问的 URL" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">保存</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { Check, Close, Plus, User } from '@element-plus/icons-vue'
import { uploadFile } from '@/api/admin/file'
import { showMessage } from '@/composables/util'
import { getBlogSettingDetail, updateBlogSetting } from '@/api/admin/blogsetting'
import { useStore } from 'vuex'
import { getAdminInfo } from '@/api/admin/user'
import MinioImage from '@/components/MinioImage.vue'

const store = useStore()

const isGithubCheck = ref(false)
const isCSDNCheck = ref(false)
const isGiteeCheck = ref(false)
const isZhihuCheck = ref(false)

// 处理图片加载错误
const handleImageError = (e) => {
    console.error('图片加载失败', e);
    // 可以设置一个默认图片
}

const form = reactive({
    blogName: '',
    author: '',
    avatar: '',
    introduction: '',
    githubHome: '',
    giteeHome: '',
    csdnHome: '',
    zhihuHome: '',
})

const rules = {
    blogName: [{ required: true, message: '请输入博客名称', trigger: 'blur' }],
    author: [{ required: true, message: '请输入作者名称', trigger: 'blur' }],
    avatar: [{ required: true, message: '请选择作者头像', trigger: 'blur' }],
}

const githubSwitchChange = (e) => {
    if (e == false) {
        form.githubHome = ''
    }
}

const csdnSwitchChange = (e) => {
    if (e == false) {
        form.csdnHome = ''
    }
}

const giteeSwitchChange = (e) => {
    if (e == false) {
        form.giteeHome = ''
    }
}

const zhihuSwitchChange = (e) => {
    if (e == false) {
        form.zhihuHome = ''
    }
}

// 处理图片URL的函数
const getImageUrl = (url) => {
    console.log('原始图片URL:', url);
    
    if (!url) {
        console.log('URL为空，返回空字符串');
        return '';
    }
    
    // 如果已经是完整URL，则直接返回
    if (url.startsWith('http://') || url.startsWith('https://')) {
        // 检查是否是Minio URL，如果是，可能需要特殊处理
        if (url.includes('127.0.0.1:9005')) {
            // 将API端点替换为公共端点
            const convertedUrl = url.replace('127.0.0.1:9005', '127.0.0.1:9000');
            console.log('转换Minio URL:', url, '->', convertedUrl);
            return convertedUrl;
        }
        
        console.log('URL已经是完整URL，直接返回:', url);
        return url;
    }
    
    // 获取基础API URL
    const baseUrl = import.meta.env.VITE_APP_BASE_API || '/api';
    
    // 确保URL路径正确
    let processedUrl = url;
    if (!url.startsWith('/')) {
        processedUrl = '/' + url;
    }
    
    // 移除baseUrl结尾的/（如果有）
    const cleanBaseUrl = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl;
    
    const finalUrl = cleanBaseUrl + processedUrl;
    console.log('拼接后的URL:', finalUrl);
    return finalUrl;
};

const handleTitleImageChange = (file) => {
    console.log('开始上传文件')
    console.log(file)
    let formData = new FormData()
    formData.append("file", file.raw);
    uploadFile(formData).then((e) => {
        if (e.success == false) {
            let message = e.message
            showMessage(message, 'error', 'message')
            return
        }
        console.log('头像上传成功，返回的URL:', e.data.url);
        form.avatar = e.data.url
        // 立即更新用户信息，确保右上角头像同步更新
        store.dispatch('getAdminInfo')
        showMessage('头像上传成功', 'success', 'message')
    }).catch(err => {
        console.error('文件上传失败:', err);
        showMessage('文件上传失败', 'error', 'message');
    });
}

function initBlogSetting() {
    getBlogSettingDetail().then((e) => {
        if (e.success == true) {
            form.blogName = e.data.blogName
            form.author = e.data.author
            form.avatar = e.data.avatar
            form.introduction = e.data.introduction
            if (e.data.githubHome) {
                isGithubCheck.value = true
                form.githubHome = e.data.githubHome
            }
            if (e.data.giteeHome) {
                isGiteeCheck.value = true
                form.giteeHome = e.data.giteeHome
            }
            if (e.data.csdnHome) {
                isCSDNCheck.value = true
                form.csdnHome = e.data.csdnHome
            }
            if (e.data.zhihuHome) {
                isZhihuCheck.value = true
                form.zhihuHome = e.data.zhihuHome
            }
        }
    })
}
initBlogSetting()

const onSubmit = () => {
    console.log('提交内容' + form.content)
    updateBlogSetting(form).then((e) => {
        console.log(e)
        if (e.success == false) {
            var message = e.message
            showMessage(message, 'warning', 'message')
            return
        }

        showMessage('更新成功', 'success', 'message')
        initBlogSetting()
        // 更新成功后，重新获取用户信息并更新Vuex中的用户状态
        store.dispatch('getAdminInfo')
    }).catch(err => {
        console.error('更新失败:', err);
        showMessage('更新失败', 'error', 'message');
    });
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.image-error-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 20px;
}
</style>