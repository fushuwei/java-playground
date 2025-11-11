
-- 选择数据库
use spring_security;

-- 默认用户
INSERT INTO `user` (`id`, `username`, `nickname`, `realname`, `password`, `age`, `email`) VALUES ('1001', 'shangsan', '张三三', '张三', '{bcrypt}$2a$10$Q.QnZJjXbH8c3Y7fC4ZlE.O7b2GqI3eM2xG0pD7.tT5u6r9p2m', 28, 'zhangsan@example.com');
INSERT INTO `user` (`id`, `username`, `nickname`, `realname`, `password`, `age`, `email`) VALUES ('1002', 'lisi', '李四四', '李四', '{bcrypt}$2a$10$R.RnZKkXbI9d4Y8gD5ZlF.P8c3H0qK4fM3yH1qE0qU6s9t0o3n', 35, 'lisi@example.com');
