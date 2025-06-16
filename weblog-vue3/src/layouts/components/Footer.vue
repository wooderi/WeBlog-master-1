<template>
    <footer
        class="footer-container mt-12 w-full p-2 bg-skyblue-gradient border-t border-gray-200 dark:bg-gray-800 dark:border-gray-600">
        <div class="max-w-screen-xl mx-auto p-4 flex flex-col md:flex-row md:items-center md:justify-between">
            <span class="text-sm text-gray-600 sm:text-center dark:text-gray-400">© 2025 <a
                    href="https://github.com/weiwosuoai/WeBlog" target="_blank" class="hover:underline">Weblog™</a>. All
                Rights Reserved.
            </span>
            <span class="text-sm text-gray-600 mt-2 md:mt-0 sm:text-center dark:text-gray-400">
                Provided by 
                <a href="#" class="hover:underline">雷 & 莫 & 王</a>
            </span>
        </div>
    </footer>
</template>

<style scoped>
.footer-container {
    margin-top: auto;
    position: relative;
    bottom: 0;
    left: 0;
    right: 0;
}

/* 确保主内容区域有足够的最小高度，使页脚始终在底部 */
html, body, #app {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

/* 确保主内容区域占据剩余空间 */
main {
    flex: 1;
}
</style>