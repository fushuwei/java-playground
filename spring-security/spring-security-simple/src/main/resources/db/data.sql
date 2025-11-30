
-- 选择数据库
use spring_security;

-- 默认用户 (admin/admin)
INSERT INTO `user` (`id`, `username`, `nickname`, `realname`, `password`, `age`, `email`, `create_time`, `update_time`) VALUES ('1001', 'admin', '系统管理员', '系统管理员', '{bcrypt}$2a$10$QsR7kxzTPMs/.SOuSf3HCOAdKpEh6lGfQ8tZfCh3.oZLcZ/BQqvIC', 18, 'admin@example.com', now(), now());

-- 该用户用于测试密码加密方式自动升级案例
DELETE FROM `user` WHERE username = 'test';
INSERT INTO `user` (`id`, `username`, `nickname`, `realname`, `password`, `age`, `email`, `create_time`, `update_time`) VALUES ('1002', 'test', '测试人员', '测试人员', '{noop}test', 20, 'test@example.com', now(), now());
