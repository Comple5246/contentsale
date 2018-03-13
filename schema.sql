DROP DATABASE IF EXISTS contentsale;

CREATE DATABASE contentsale;
USE contentsale;
--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码md5加密',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `user_type` tinyint(3) DEFAULT NULL COMMENT '类型，买家0，卖家1',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;

INSERT INTO `user` VALUES (1,'buyer','37254660e226ea65ce6f1efd54233424','buyer',0)
  ,(2,'seller','981c57a5cfb0f868e064904b8745766f','seller',1);

UNLOCK TABLES;


DROP TABLE IF EXISTS `content`;

CREATE TABLE `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `seller_id` int(11) NOT NULL COMMENT '卖家ID',
  `price` double DEFAULT NULL COMMENT '当前价格',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '摘要',
  `content` longtext DEFAULT NULL COMMENT '正文',
  `image` varchar(200) DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`seller_id`) REFERENCES user(`id`)
) ENGINE=InnoDB AUTO_INCREMENT= 3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `content`
--

LOCK TABLES `content` WRITE;

INSERT INTO `content` VALUES(1, 2, 1.11, '测试Title1', '测试Summary1', '测试Content1', '/image/default.jpg'),
  (2,2,2.22,'测试Title2', '测试Summary2', '测试Content2', '/image/default.jpg');
UNLOCK TABLES;



DROP TABLE IF EXISTS `trx`;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trx` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content_id` int(11) DEFAULT NULL COMMENT '内容ID',
  `buyer_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `buy_price` double DEFAULT NULL COMMENT '购买价格',
  `num` int(11) DEFAULT NULL COMMENT '购买数量',
  `buy_time` bigint(20) DEFAULT NULL COMMENT '购买时间',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`content_id`) REFERENCES content(`id`),
  FOREIGN KEY (`buyer_id`) REFERENCES user(`id`)
) ENGINE=InnoDB AUTO_INCREMENT = 2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `trx`

# LOCK TABLES `trx` WRITE;
#
# INSERT INTO `trx` VALUES(1,1,1,6.66,2,0);
# UNLOCK TABLES;

DROP TABLE IF EXISTS cart;

CREATE TABLE cart(
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `buyer_id` INT(11) NOT NULL COMMENT '购买者ID',
  `content_id` INT(11) NOT NULL COMMENT '内容ID',
  `num` INT(11) NOT NULL COMMENT '购买数量',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`buyer_id`) REFERENCES user(`id`),
  FOREIGN KEY (`content_id`) REFERENCES content(`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8;
