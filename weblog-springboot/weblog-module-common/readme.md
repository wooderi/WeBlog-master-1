f:\Code\blog\WeBlog-master1\WeBlog-master\weblog-springboot\weblog-module-common\src\main\java\com\quanxiaoha\weblog
          
# weblog-module-common模块文件及功能注释

## 根目录
- `.gitignore` - Git忽略规则配置文件
- `pom.xml` - Maven项目配置文件
- `readme.txt` - 模块说明文件

## src/main/java/com/quanxiaoha/weblog/common

### 核心类
- `PageResponse.java` - 分页响应封装类
- `Response.java` - 统一API响应封装类

### aspect包（切面相关）
- `ApiOperationLog.java` - API操作日志注解
- `ApiOperationLogAspect.java` - API操作日志切面实现

### config包（配置类）
- `EventBusConfig.java` - 事件总线配置
- `InsertBatchSqlInjector.java` - 批量插入SQL注入器
- `MyBaseMapper.java` - 自定义MyBatis-Plus基础Mapper
- `MybatisPlusConfig.java` - MyBatis-Plus配置

### constant包（常量）
- `Constants.java` - 全局静态常量

### domain包（领域模型）

#### dos子包（数据对象）
- `ArticleCategoryRelDO.java` - 文章-分类关联实体
- `ArticleContentDO.java` - 文章内容实体
- `ArticleCountDO.java` - 文章统计实体
- `ArticleDO.java` - 文章实体
- `ArticleTagRelDO.java` - 文章-标签关联实体
- `BlogSettingDO.java` - 博客设置实体
- `CategoryDO.java` - 分类实体
- `StatisticsArticlePVDO.java` - 文章PV统计实体
- `TagDO.java` - 标签实体
- `UserDO.java` - 用户实体
- `UserRoleDO.java` - 用户角色实体
- `VisitorRecordDO.java` - 访客记录实体

#### mapper子包（数据访问）
- `ArticleCategoryRelMapper.java` - 文章-分类关联Mapper
- `ArticleContentMapper.java` - 文章内容Mapper
- `ArticleMapper.java` - 文章Mapper
- `ArticleTagRelMapper.java` - 文章-标签关联Mapper
- `BlogSettingMapper.java` - 博客设置Mapper
- `CategoryMapper.java` - 分类Mapper
- `StatisticsArticlePVMapper.java` - 文章PV统计Mapper
- `TagMapper.java` - 标签Mapper
- `UserMapper.java` - 用户Mapper
- `UserRoleMapper.java` - 用户角色Mapper
- `VisitorMapper.java` - 访客Mapper

### enums包（枚举）
- `EventEnum.java` - 事件类型枚举
- `ResponseCodeEnum.java` - 响应码枚举

### eventbus包（事件总线）
- `ArticleEvent.java` - 文章事件
- `EventListener.java` - 事件监听器

### exception包（异常处理）
- `BaseExceptionInterface.java` - 基础异常接口
- `BizException.java` - 业务异常
- `GlobalExceptionHandler.java` - 全局异常处理器
- `NotAuthorizedException.java` - 未授权异常
- `ResourceNotFoundException.java` - 资源未找到异常

### model/vo包（视图对象）
- `QuerySelectListRspVO.java` - 查询下拉列表响应VO

### utils包（工具类）
- `AgentRegionUtils.java` - 用户代理区域解析工具

#Workspace
        