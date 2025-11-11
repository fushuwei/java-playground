
-- 选择数据库
use spring_security;

-- 默认用户 (admin/admin)
INSERT INTO `user` (`id`, `username`, `nickname`, `realname`, `password`, `age`, `email`) VALUES ('1001', 'admin', '系统管理员', '系统管理员', '$2a$10$QsR7kxzTPMs/.SOuSf3HCOAdKpEh6lGfQ8tZfCh3.oZLcZ/BQqvIC', 18, 'admin@example.com');
