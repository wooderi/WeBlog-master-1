weblog-module-admin/
├── .gitignore                  # Git版本控制忽略文件配置 
├── pom.xml                     # Maven项目依赖管理文件 
├── src/                        # 源代码根目录 
│   └── main/                   # 主程序代码目录 
│       └── java/               # Java源代码目录 
│           └── com/            # 公司域名反向包路径 
│               └── quanxiaoha/ # 项目作者包路径 
│                   └── weblog/ # 项目核心包路径 
│                       └── admin/ # 管理后台核心功能包 
│                           ├── async/                  # 异步任务处理类目录 
│                           │   └── PVIncreaseAsyncTask.java # 文章PV阅读量异步增加任务 
│                           ├── config/                 # 配置类目录 
│                           │   ├── MinioConfig.java     # Minio对象存储客户端配置类 
│                           │   ├── MinioProperties.java # Minio配置参数读取类 
│                           │   ├── ThreadPoolConfig.java # 线程池配置类 
│                           │   └── WebSecurityConfig.java # 管理后台安全配置类 
│                           ├── controller/             # 控制器（接口）类目录 
│                           │   ├── AdminUserController.java # 管理员用户接口控制器 
│                           │   └── AdminBlogSettingController.java # 博客设置接口控制器 
│                           ├── service/                # 业务逻辑服务类目录 
│                           │   ├── impl/               # 服务实现类子目录 
│                           │   │   ├── AdminArticleServiceImpl.java # 文章管理业务实现 
│                           │   │   └── AdminFileServiceImpl.java # 文件上传业务实现 
│                           ├── utils/                  # 工具类目录 
│                           │   └── MinioUtil.java       # Minio文件操作工具类 
│                           └── model/                  # 模型（VO/DTO）类目录 
│                               └── vo/                 # 视图对象（请求/响应）子目录 
└── target/                     # Maven构建输出目录 

### 后台文章管理功能实现详解 

### 一、整体架构

项目采用经典的MVC分层架构实现文章管理功能：

- Controller层 ：接收前端请求，返回响应结果

- Service层 ：处理核心业务逻辑

- DAO层 ：与数据库交互

- Model层 ：定义数据对象和视图对象 

### 二、文章发布流程

1. 请求入口 ：
   
   ```
   @PostMapping("/publish")
   @ApiOperationLog(description = "发布文章")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public Response publishArticle(@RequestBody @Validated PublishArticleReqVO 
   publishArticleReqVO) {
       return articleService.publishArticle(publishArticleReqVO);
   }
   ```
2. 业务逻辑处理 ： `AdminArticleServiceImpl.java` 中的 publishArticle 方法
   
   - 使用 TransactionTemplate 管理事务
   - 转换MinIO图片URL（ convertUrl 方法）
   - 保存文章基本信息到 article 表
   - 保存文章内容到 article_content 表
   - 处理分类关联
   - 处理标签关联（ handleTagBiz 方法）
3. 数据持久化 ：
   
   - `AdminArticleDao.java` ：文章基本信息CRUD
   - `AdminArticleContentDao.java` ：文章内容CRUD
   - `AdminArticleCategoryRelDao.java` ：文章分类关联 
### 三、文章查询流程
1. 分页查询 ：
   
   ```
   @PostMapping("/list")
   @ApiOperationLog(description = "获取文章分页数据")
   public Response queryArticlePageList(@RequestBody QueryArticlePageListReqVO 
   queryArticlePageListReqVO) {
       return articleService.queryArticlePageList(queryArticlePageListReqVO);
   }
   ```
2. 详情查询 ：
   
   ```
   @PostMapping("/detail")
   @ApiOperationLog(description = "获取文章详情")
   public Response queryArticleDetail(@RequestBody QueryArticleDetailReqVO 
   queryArticleDetailReqVO) {
       return articleService.queryArticleDetail(queryArticleDetailReqVO);
   }
   ```
   - 联表查询文章基本信息、内容和关联标签
   - 转换图片URL为公共访问地址 
### 四、文章编辑流程
1. 请求入口 ：
   
   ```
   @PostMapping("/update")
   @ApiOperationLog(description = "修改文章")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public Response updateArticle(@RequestBody @Validated UpdateArticleReqVO 
   updateArticleReqVO) {
       return articleService.updateArticle(updateArticleReqVO);
   }
   ```
2. 业务逻辑 ：
   
   - 更新文章基本信息
   - 更新文章内容
   - 处理标签变更（新增/删除标签关联）
   - 处理分类变更 
### 五、文章删除流程
1. 请求入口 ：
   
   ```
   @PostMapping("/delete")
   @ApiOperationLog(description = "删除文章")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public Response deleteArticle(@RequestBody @Validated DeleteArticleReqVO 
   deleteArticleReqVO) {
       return articleService.deleteArticle(deleteArticleReqVO);
   }
   ```
2. 业务逻辑 ：
   
   ```
   @Override
   @Transactional(rollbackFor = Exception.class)
   public Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO) {
       Long articleId = deleteArticleReqVO.getArticleId();
       articleDao.deleteById(articleId);
       articleContentDao.deleteByArticleId(articleId);
       return Response.success();
   }
   ```
   - 级联删除文章基本信息和内容
   - 事务管理确保数据一致性 
### 六、核心文件总结
1. 控制器 ：
   
   - `AdminArticleController.java` ：处理所有文章相关请求
2. 服务层 ：
   
   - `AdminArticleService.java` ：服务接口定义
   - `AdminArticleServiceImpl.java` ：业务逻辑实现
3. 数据访问层 ：
   
   - AdminArticleDao ：文章基本信息操作
   - AdminArticleContentDao ：文章内容操作
   - AdminArticleCategoryRelDao ：文章分类关联操作
   - AdminArticleTagRelDao ：文章标签关联操作
4. 数据模型 ：
   
   - 请求对象： PublishArticleReqVO 、 UpdateArticleReqVO 等
   - 响应对象： QueryArticleDetailRspVO 、 QueryArticlePageListRspVO 等
   - 数据对象： ArticleDO 、 ArticleContentDO 等