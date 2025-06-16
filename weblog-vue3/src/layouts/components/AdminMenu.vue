<template>
    <div class="meun shadow-md fixed bg-light-50 transition-all duration-300" :style="{ width: $store.state.menuWidth }">
        <div class="flex items-center justify-center h-[64px]">
            <img v-if="$store.state.menuWidth == '250px'" src="@/assets/weblog-logo.png" class="h-[60px]">
            <img v-else src="@/assets/weblog-logo-mini.png" class="h-[60px]">
        </div>

        <el-menu :collapse="isCollapse"  class="border-0 admin-el-menu"
        :default-active="defaultActive"
        :collapse-transition="false"
        unique-opened
         @select="handleSelect">
            <el-menu-item 
                v-for="(item, index) in menus" 
                :key="index"
                :index="item.path" 
                class="admin-el-menu-item">
                <el-icon>
                    <component :is="item.icon"></component>
                </el-icon>
                <span>{{ item.name }}</span>
            </el-menu-item>
        </el-menu>
    </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';
import { computed, ref } from 'vue';
import { useStore } from 'vuex';

const router = useRouter()
const route = useRoute()
const store = useStore()

const defaultActive = ref(route.path)

// 是否折叠
const isCollapse = computed(() =>  !(store.state.menuWidth == '250px'))

const menus = [{
    'name': '仪表盘',
    'icon': 'Monitor',
    'path': '/admin',
    'child': []
},
{
    'name': '文章管理',
    'icon': 'Document',
    'path': '/admin/article/list',
    'child': []
},
{
    'name': '分类管理',
    'icon': 'FolderOpened',
    'path': '/admin/category/list',
    'child': []
},
{
    'name': '标签管理',
    'icon': 'PriceTag',
    'path': '/admin/tag/list',
    'child': []
},
// {
//     'name': '轮播图管理',
//     'icon': 'Picture',
//     'path': '/admin/carousel/list',
//     'child': []
// },
{
    'name': '博客设置',
    'icon': 'Setting',
    'path': '/admin/blog/setting',
    'child': []
}
]

const handleSelect = (e) => {
    console.log(defaultActive)
    console.log(route.path)

    router.push(e)
}
</script>

<style>
.meun {
    transition: all 0.3s;
    width: 250px;
    top: 0;
    bottom: 0;
    left: 0;
    overflow-y: auto;
    overflow-x: hidden;
    background: linear-gradient(to bottom, #1867c0, #1e4976)!important;
}

.admin-el-menu {
    background-color: transparent!important;
    border-right: 0;
}

.admin-el-menu-item {
    color: #e6f0ff!important;
}

.el-menu-item.is-active {
    background-color: rgba(255, 255, 255, 0.15)!important;
}

.el-menu-item.is-active:before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 3px;
    height: 100%;
    background-color: #4a90e2;
}

.el-menu-item:hover {
    background-color: rgba(255, 255, 255, 0.1);
}

.meun::-webkit-scrollbar {
    width: 0;
}

.logo {
    height: 64px;
    background: linear-gradient(to right, #1867c0, #4a90e2);
    color: #fff;
    @apply flex justify-center items-center text-xl font-thin;
}

/* 添加阴影效果增强立体感 */
.el-menu-item {
    transition: all 0.3s ease;
}

.el-menu-item:hover {
    box-shadow: 0 0 10px rgba(255, 255, 255, 0.1);
}

.el-menu-item.is-active {
    box-shadow: 0 0 15px rgba(255, 255, 255, 0.15) inset;
}
</style>