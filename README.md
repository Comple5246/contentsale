# contentsale
## 项目描述
本项目为网易互联网Java技术岗入职前练习项目，同时也是网易云课堂Java Web微课程课程作业。本项目前端使用课程前端模板，做了相应修改，后端使用SSM框架搭建。

## 项目构建
Maven项目管理

Spring+SpringMVC+MyBatis后端框架

MySQL数据库

## 软件版本
MySQL 5.6

Tomcat 8.0.33

## 部署相关
### 数据库配置
url: jdbc:mysql://localhost:3306/contentsale

用户名：root

密码：123456

配置文件:schema.sql
### 项目部署
项目打包文件为 target/ROOT##2.war,部署时直接将该文件复制到Tomcat webapps，不需要对Tomcat默认配置进行修改。打包文件名保证项目路径为根路径，即访问路径为localhost:8080
###Tips
1、用户登录名、密码：buyer/reyub、seller/relles
2、在线购物车内容会在session销毁时写入数据库，如果直接关闭Tomcat会导致购物车内容没有写回数据库。
