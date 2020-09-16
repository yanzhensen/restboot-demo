-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.16-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 restbootdb 的数据库结构
CREATE DATABASE IF NOT EXISTS `restbootdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `restbootdb`;

-- 导出  表 restbootdb.databasechangeloglock 结构
CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  restbootdb.databasechangeloglock 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES
	(1, b'0', NULL, NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;

-- 导出  表 restbootdb.info_photo 结构
CREATE TABLE IF NOT EXISTS `info_photo` (
  `photo_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `photo_table_id` int(11) unsigned DEFAULT NULL COMMENT '外表ID',
  `photo_table_type` varchar(50) DEFAULT NULL COMMENT '外表类型',
  `photo_random` varchar(50) DEFAULT NULL COMMENT '随机数',
  `photo_mime_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `photo_url` varchar(100) DEFAULT NULL COMMENT '图片路径',
  `photo_name` varchar(100) DEFAULT NULL COMMENT '图片名称',
  `photo_bucket` varchar(100) DEFAULT NULL COMMENT '存储空间类型',
  `photo_status` varchar(100) DEFAULT NULL COMMENT '图片状态',
  `photo_index` int(11) unsigned DEFAULT NULL COMMENT '优先级',
  `photo_user_id` int(11) unsigned DEFAULT NULL COMMENT '上传人ID',
  `photo_create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='图片表';

-- 正在导出表  restbootdb.info_photo 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `info_photo` DISABLE KEYS */;
INSERT INTO `info_photo` (`photo_id`, `photo_table_id`, `photo_table_type`, `photo_random`, `photo_mime_type`, `photo_url`, `photo_name`, `photo_bucket`, `photo_status`, `photo_index`, `photo_user_id`, `photo_create_time`) VALUES
	(9, NULL, '用户', '846515616', '图片', 'http://pho.pub.com/profile/302f9683cae52ee25110f7f9067d48c8.png', '人头1', '公有', '临时', NULL, 6, '2020-09-07 15:29:08'),
	(10, 1, '用户', NULL, '图片', 'http://pho.prv.com/profile/724124655a82af34aede4dc4500cb17b.jpg', '9.jpg', '私有', '正常', NULL, 1, '2020-09-08 15:06:55');
/*!40000 ALTER TABLE `info_photo` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_menu 结构
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `mid` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `menu_name` varchar(32) NOT NULL COMMENT '菜单名称',
  `path` varchar(64) DEFAULT NULL COMMENT '路径',
  `menu_type` smallint(2) NOT NULL COMMENT '类型:0:目录,1:菜单,2:按钮',
  `icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `router` varchar(64) DEFAULT NULL COMMENT '路由',
  `alias` varchar(64) DEFAULT NULL COMMENT '别名',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` smallint(2) NOT NULL COMMENT '状态 0：禁用 1：正常',
  `create_uid` int(11) NOT NULL COMMENT '创建者ID',
  `update_uid` int(11) NOT NULL COMMENT '修改者ID',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`mid`) USING BTREE,
  UNIQUE KEY `alias` (`alias`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 正在导出表  restbootdb.sys_menu 的数据：~17 rows (大约)
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`mid`, `parent_id`, `menu_name`, `path`, `menu_type`, `icon`, `router`, `alias`, `sort`, `status`, `create_uid`, `update_uid`, `update_time`, `create_time`) VALUES
	(1, 0, '系统管理', '', 0, 'el-icon-setting', NULL, 'sys_ss', 1, 1, 1, 1, '2018-11-27 15:11:15', '2018-11-27 14:52:10'),
	(2, 1, '用户管理', 'userInfo', 1, NULL, '/userInfo', 'sys:user:list', 1, 1, 1, 1, '2018-12-12 15:39:18', '2018-11-27 15:10:32'),
	(3, 1, '角色管理', 'roleInfo', 1, NULL, '/roleInfo', 'sys:role:list', 2, 1, 1, 1, '2018-12-12 15:40:03', '2018-11-27 15:16:59'),
	(4, 1, '菜单管理', 'menuInfo', 1, NULL, '/menuInfo', 'sys:menu:list', 3, 1, 1, 1, '2018-12-12 15:37:35', '2018-11-27 15:17:59'),
	(5, 1, '资源管理', 'resourceInfo', 1, NULL, '/resourceInfo', 'sys:resource:list', 4, 1, 1, 1, '2018-12-12 15:35:38', '2018-11-27 15:18:31'),
	(6, 1, '权限管理', 'roleManager', 1, NULL, '/roleManager', 'sys_role_manager', 5, 1, 1, 1, '2020-07-27 16:18:58', '2020-07-27 16:18:59'),
	(10, 26, '刷新资源', '', 2, NULL, NULL, 'sys:resource:refresh', NULL, 1, 1, 1, '2018-12-12 15:35:14', '2018-11-27 15:19:15'),
	(11, 4, '添加', '', 2, NULL, NULL, 'sys:menu:add', NULL, 1, 1, 1, '2018-12-12 15:45:47', '2018-11-27 15:20:06'),
	(12, 4, '修改', '', 2, NULL, NULL, 'sys:menu:edit', NULL, 1, 1, 1, '2018-12-12 15:36:51', '2018-11-27 15:20:27'),
	(13, 4, '删除', '', 2, NULL, NULL, 'sys:menu:delete', NULL, 1, 1, 1, '2018-12-12 15:35:49', '2018-11-27 15:21:14'),
	(14, 3, '添加', '', 2, NULL, NULL, 'sys:role:add', NULL, 1, 1, 1, '2018-12-12 15:38:07', '2018-11-27 15:20:06'),
	(15, 3, '修改', '', 2, NULL, NULL, 'sys:role:edit', NULL, 1, 1, 1, '2018-12-12 15:44:19', '2018-11-27 15:20:27'),
	(16, 3, '删除', '', 2, NULL, NULL, 'sys:role:delete', NULL, 1, 1, 1, '2018-12-12 15:36:07', '2018-11-27 15:21:14'),
	(17, 2, '添加', '', 2, NULL, NULL, 'sys:user:add', NULL, 1, 1, 1, '2018-12-12 15:44:04', '2018-11-27 15:20:06'),
	(18, 2, '修改', '', 2, NULL, NULL, 'sys:user:edit', NULL, 1, 1, 1, '2018-12-12 15:39:36', '2018-11-27 15:20:27'),
	(19, 2, '重置密码', '', 2, NULL, NULL, 'sys:user:resetpwd', NULL, 1, 1, 1, '2018-12-12 15:38:48', '2018-11-27 15:21:14'),
	(20, 24, '菜单授权', NULL, 2, NULL, NULL, 'sys:role:perm', NULL, 1, 1, 1, '2018-12-12 15:41:52', '2018-12-08 23:58:42');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_menu_resource 结构
CREATE TABLE IF NOT EXISTS `sys_menu_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单ID',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COMMENT='菜单资源关系表';

-- 正在导出表  restbootdb.sys_menu_resource 的数据：~26 rows (大约)
/*!40000 ALTER TABLE `sys_menu_resource` DISABLE KEYS */;
INSERT INTO `sys_menu_resource` (`id`, `menu_id`, `resource_id`) VALUES
	(70, 27, 'f45f1b577d72dcd86b84c6f033682b53'),
	(71, 26, '829a851334028a6e47b59f8dea0cf7cb'),
	(72, 30, 'f15f7b01ffe7166b05c3984c9b967837'),
	(73, 33, '6692d9d95184977f82d3252de2f5eac7'),
	(74, 29, 'a11e2191656cb199bea1defb17758411'),
	(75, 29, '6fd51f02b724c137a08c28587f48d7f3'),
	(76, 29, '2c654f1264fc85ac80516245672f3c47'),
	(77, 29, 'a5529264d2645996c83bba2e961d0ec3'),
	(80, 25, '6d1170346960aa8922b9b4d08a5bf71b'),
	(81, 25, '30218613e987e464b13e0c0b8721aec5'),
	(83, 31, 'd82de0a17f2c63106f98eb2f88d166e9'),
	(85, 36, '7baa5b852bc92715d7aa503c0a0d8925'),
	(87, 23, '579e469e8ac850de1ca0adc54d01acba'),
	(88, 23, 'b4770c0fe93fce7e829463328c800f20'),
	(89, 35, '30386fd7b8a4feb9c59861e63537acde'),
	(90, 35, '8a3b4dc05867f5946235ba5fbc492b76'),
	(91, 24, '04972e9f8e65b0364d9862687666da36'),
	(93, 42, 'a826bca352908155da4ca6702edfa2ed'),
	(94, 42, '30218613e987e464b13e0c0b8721aec5'),
	(95, 34, 'a71cb59835c613f39bd936deb20cdd27'),
	(96, 34, 'd9d6f7163b313b950710a637682354d1'),
	(97, 32, 'eaee955f405f6b96beab5136bfa3e29b'),
	(98, 32, 'd9d6f7163b313b950710a637682354d1'),
	(99, 28, '8cb1442c7814f65ce0d796e1ef93c715'),
	(100, 28, 'a5529264d2645996c83bba2e961d0ec3'),
	(101, 28, '2c654f1264fc85ac80516245672f3c47');
/*!40000 ALTER TABLE `sys_menu_resource` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_resource 结构
CREATE TABLE IF NOT EXISTS `sys_resource` (
  `resou_id` varchar(32) NOT NULL,
  `resource_name` varchar(32) NOT NULL COMMENT '资源名称',
  `mapping` varchar(64) NOT NULL COMMENT '路径映射',
  `method` varchar(6) NOT NULL COMMENT '请求方式',
  `auth_type` smallint(2) NOT NULL COMMENT '权限认证类型',
  `perm` varchar(68) NOT NULL COMMENT '权限标识',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`resou_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

-- 正在导出表  restbootdb.sys_resource 的数据：~33 rows (大约)
/*!40000 ALTER TABLE `sys_resource` DISABLE KEYS */;
INSERT INTO `sys_resource` (`resou_id`, `resource_name`, `mapping`, `method`, `auth_type`, `perm`, `update_time`, `create_time`) VALUES
	('04972e9f8e65b0364d9862687666da36', '分页所有角色', '/roles', 'GET', 3, 'GET:/roles', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('1555b8b9385387d706794ebb8b9c2572', '用户注销', '/logout', 'POST', 2, 'POST:/logout', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('1fdca9c0be171b1f20f0afa34d0bc1df', '删除七牛云图片和本地图片', '/photo', 'DELETE', 3, 'DELETE:/photo', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('30218613e987e464b13e0c0b8721aec5', '获取用户权限菜单 返回树', '/menus', 'GET', 1, 'GET:/menus', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('30386fd7b8a4feb9c59861e63537acde', '修改用户', '/users/{id}', 'PUT', 3, 'PUT:/users/{id}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('3b70f41d904b875780e0fe84ec4b8b74', '获取用户按钮权限', '/menus/buttons/aliases', 'GET', 1, 'GET:/menus/buttons/aliases', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('471795e40322fc1ab945452e0c9b29d9', '根据随机数删除临时图片', '/photo/temporaryPhoto', 'DELETE', 3, 'DELETE:/photo/temporaryPhoto', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('5004f247d045a6abdbaba9e22b38ae84', '查询授权角色菜单Id', '/roleMenu/{id}', 'GET', 3, 'GET:/roleMenu/{id}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('5413e80ba40b90906930a1ccf8ef92e5', '管理员授权所有资源', '/resources/access', 'POST', 2, 'POST:/resources/access', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('579e469e8ac850de1ca0adc54d01acba', '查询所有用户', '/users', 'GET', 3, 'GET:/users', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('6d1170346960aa8922b9b4d08a5bf71b', '设置菜单状态', '/menus/{id}/status', 'PUT', 3, 'PUT:/menus/{id}/status', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('6edd43f24fbaa7e63b2ebc58fc2f526d', '授权菜单', '/menus/access', 'POST', 2, 'POST:/menus/access', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('6fd51f02b724c137a08c28587f48d7f3', '查询单个菜单', '/menus/{id}', 'GET', 3, 'GET:/menus/{id}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('775b6f58aa3030aeca9787c575f4f9b3', '查询所有菜单', '/menus/list', 'GET', 3, 'GET:/menus/list', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('7baa5b852bc92715d7aa503c0a0d8925', '重置用户密码', '/users/{id}/password', 'PUT', 3, 'PUT:/users/{id}/password', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('829a851334028a6e47b59f8dea0cf7cb', '查询所有资源(分页)', '/resources', 'GET', 3, 'GET:/resources', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('8a3b4dc05867f5946235ba5fbc492b76', '查询单个用户', '/users/{id}', 'GET', 3, 'GET:/users/{id}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('8cb1442c7814f65ce0d796e1ef93c715', '添加菜单', '/menus', 'POST', 3, 'POST:/menus', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('a11e2191656cb199bea1defb17758411', '修改菜单', '/menus/{id}', 'PUT', 3, 'PUT:/menus/{id}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('a5529264d2645996c83bba2e961d0ec3', '查询所有资源', '/resources/resources', 'GET', 3, 'GET:/resources/resources', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('a71cb59835c613f39bd936deb20cdd27', '创建用户', '/users', 'POST', 3, 'POST:/users', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('a76d1f5d8d0f7f11a6bcd18e486d89f7', '查询图片', '/photo', 'GET', 3, 'GET:/photo', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('afab705c5e5aeb779e21f777a0e01ea8', '修改图片状态为失效', '/photo/failure/{photoTableId}', 'PUT', 3, 'PUT:/photo/failure/{photoTableId}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('b4770c0fe93fce7e829463328c800f20', '设置用户状态', '/users/{id}/status', 'PUT', 3, 'PUT:/users/{id}/status', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('b68693067e76e4b0e9cc7412a17cae70', '获取所有菜单 返回树', '/menus/trees', 'GET', 3, 'GET:/menus/trees', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('c3ba7e22b47995af304ca91e69798b87', '上传回调', '/photo/callback', 'POST', 2, 'POST:/photo/callback', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('cac73c446e3445d995ca4216f46aef7d', '用户登录', '/login', 'POST', 2, 'POST:/login', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('d2a550d29bf0c0b145e09a663d34c227', '生成手机访问的url', '/photo/getMobUploadUrl', 'GET', 3, 'GET:/photo/getMobUploadUrl', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('d30d41003a9331d2e38cac5b270b0a25', '保存图片', '/photo', 'POST', 3, 'POST:/photo', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('e0156ff3032138076c5cf3472efc190a', '恢复修改图片时删除的正常图片', '/photo/recovery/{photoTableId}', 'PUT', 3, 'PUT:/photo/recovery/{photoTableId}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('eb12e9dbef07011f2dd4d05e093c65c9', '获取图片token有回调', '/photo/token/callback', 'GET', 3, 'GET:/photo/token/callback', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('f15f7b01ffe7166b05c3984c9b967837', '删除菜单', '/menus/{id}', 'DELETE', 3, 'DELETE:/menus/{id}', '2020-09-09 10:06:05', '2020-09-08 14:26:15'),
	('f45f1b577d72dcd86b84c6f033682b53', '刷新资源', '/resources', 'PUT', 3, 'PUT:/resources', '2020-09-09 10:06:05', '2020-09-08 14:26:15');
/*!40000 ALTER TABLE `sys_resource` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_role 结构
CREATE TABLE IF NOT EXISTS `sys_role` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `update_uid` int(11) NOT NULL COMMENT '修改者ID',
  `create_uid` int(11) NOT NULL COMMENT '创建者ID',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`rid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 正在导出表  restbootdb.sys_role 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`rid`, `role_name`, `remark`, `update_uid`, `create_uid`, `update_time`, `create_time`) VALUES
	(1, '超级管理员', '超级管理员', 1, 1, '2018-11-12 15:59:47', '2018-11-12 15:59:43'),
	(2, '普通管理员', '普通管理员', 1, 1, '2018-11-12 16:00:19', '2018-11-12 16:00:17');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_role_menu 结构
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1701 DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关系表';

-- 正在导出表  restbootdb.sys_role_menu 的数据：~17 rows (大约)
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES
	(1684, 1, 1),
	(1685, 1, 2),
	(1686, 1, 3),
	(1687, 1, 4),
	(1688, 1, 5),
	(1689, 1, 6),
	(1690, 1, 10),
	(1691, 1, 11),
	(1692, 1, 12),
	(1693, 1, 13),
	(1694, 1, 14),
	(1695, 1, 15),
	(1696, 1, 16),
	(1697, 1, 17),
	(1698, 1, 18),
	(1699, 1, 19),
	(1700, 1, 20);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_role_resource 结构
CREATE TABLE IF NOT EXISTS `sys_role_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `resource_id` varchar(32) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3104 DEFAULT CHARSET=utf8mb4 COMMENT='角色资源关系表';

-- 正在导出表  restbootdb.sys_role_resource 的数据：~33 rows (大约)
/*!40000 ALTER TABLE `sys_role_resource` DISABLE KEYS */;
INSERT INTO `sys_role_resource` (`id`, `role_id`, `resource_id`) VALUES
	(3071, 1, '04972e9f8e65b0364d9862687666da36'),
	(3072, 1, '1555b8b9385387d706794ebb8b9c2572'),
	(3073, 1, '1fdca9c0be171b1f20f0afa34d0bc1df'),
	(3074, 1, '30218613e987e464b13e0c0b8721aec5'),
	(3075, 1, '30386fd7b8a4feb9c59861e63537acde'),
	(3076, 1, '3b70f41d904b875780e0fe84ec4b8b74'),
	(3077, 1, '471795e40322fc1ab945452e0c9b29d9'),
	(3078, 1, '5004f247d045a6abdbaba9e22b38ae84'),
	(3079, 1, '5413e80ba40b90906930a1ccf8ef92e5'),
	(3080, 1, '579e469e8ac850de1ca0adc54d01acba'),
	(3081, 1, '6d1170346960aa8922b9b4d08a5bf71b'),
	(3082, 1, '6edd43f24fbaa7e63b2ebc58fc2f526d'),
	(3083, 1, '6fd51f02b724c137a08c28587f48d7f3'),
	(3084, 1, '775b6f58aa3030aeca9787c575f4f9b3'),
	(3085, 1, '7baa5b852bc92715d7aa503c0a0d8925'),
	(3086, 1, '829a851334028a6e47b59f8dea0cf7cb'),
	(3087, 1, '8a3b4dc05867f5946235ba5fbc492b76'),
	(3088, 1, '8cb1442c7814f65ce0d796e1ef93c715'),
	(3089, 1, 'a11e2191656cb199bea1defb17758411'),
	(3090, 1, 'a5529264d2645996c83bba2e961d0ec3'),
	(3091, 1, 'a71cb59835c613f39bd936deb20cdd27'),
	(3092, 1, 'a76d1f5d8d0f7f11a6bcd18e486d89f7'),
	(3093, 1, 'afab705c5e5aeb779e21f777a0e01ea8'),
	(3094, 1, 'b4770c0fe93fce7e829463328c800f20'),
	(3095, 1, 'b68693067e76e4b0e9cc7412a17cae70'),
	(3096, 1, 'c3ba7e22b47995af304ca91e69798b87'),
	(3097, 1, 'cac73c446e3445d995ca4216f46aef7d'),
	(3098, 1, 'd2a550d29bf0c0b145e09a663d34c227'),
	(3099, 1, 'd30d41003a9331d2e38cac5b270b0a25'),
	(3100, 1, 'e0156ff3032138076c5cf3472efc190a'),
	(3101, 1, 'eb12e9dbef07011f2dd4d05e093c65c9'),
	(3102, 1, 'f15f7b01ffe7166b05c3984c9b967837'),
	(3103, 1, 'f45f1b577d72dcd86b84c6f033682b53');
/*!40000 ALTER TABLE `sys_role_resource` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_user 结构
CREATE TABLE IF NOT EXISTS `sys_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `age` int(11) NOT NULL COMMENT '年龄',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `login_ip` varchar(32) DEFAULT NULL COMMENT 'IP地址',
  `login_name` varchar(16) NOT NULL COMMENT '登陆名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` smallint(2) NOT NULL COMMENT '状态 0：禁用 1：正常',
  `create_uid` int(11) NOT NULL COMMENT '创建者ID',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 正在导出表  restbootdb.sys_user 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`uid`, `username`, `age`, `phone`, `email`, `login_ip`, `login_name`, `password`, `status`, `create_uid`, `update_time`, `create_time`) VALUES
	(1, 'admin', 0, '13712345678', 'caratacus@qq.com', '127.0.0.1', 'admin', '$apr1$as35ilsd$XbWgtTuHoVKHdsDUidy88/', 1, 1, '2020-09-08 15:34:46', '2018-11-05 17:19:05'),
	(18, 'crown1', 0, '13718867899', '11@qq.com', '0:0:0:0:0:0:0:1', 'crown1', '$apr1$crown1$NsepppGmlSjqtwPTlaLb1/', 0, 1, '2018-12-12 15:28:18', '2018-11-19 18:56:19'),
	(19, 'crown2', 0, '13878929833', '13878929833@163.com', NULL, 'crown2', '$apr1$crown2$R/8LgZ.REDrXB3jlpyglI0', 1, 18, '2018-12-10 15:25:57', '2018-12-10 15:25:57'),
	(20, 'admin1', 20, '13588886666', '41533@qq.com', NULL, 'admin1', 'admin1', 1, 1, '2020-07-08 11:39:04', '2020-07-08 11:39:04'),
	(21, 'admin2', 20, '13588886666', '41533@qq.com', NULL, 'admin2', 'admin2', 1, 1, '2020-07-08 11:39:35', '2020-07-08 11:39:35'),
	(22, 'yzs', 20, '13537845125', 'trt@qq.com', '0:0:0:0:0:0:0:1', 'yzs', '$apr1$as35ilsd$dtNlZc5PbP4wUEF1DX4yi1', 1, 1, '2020-07-22 16:39:32', '2020-07-22 16:36:50');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

-- 导出  表 restbootdb.sys_user_role 结构
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户角色关系表';

-- 正在导出表  restbootdb.sys_user_role 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`id`, `uid`, `role_id`) VALUES
	(24, 18, 2),
	(49, 1, 1),
	(50, 19, 2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
