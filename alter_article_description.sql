-- 修改t_article表中的description字段为TEXT类型
-- 执行此SQL前请确保已备份数据库
ALTER TABLE t_article MODIFY COLUMN description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章描述';

-- 验证修改是否生效
DESC t_article; 