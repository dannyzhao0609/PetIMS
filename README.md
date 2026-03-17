# PetIMS - 宠物管理系统

一个现代化的宠物管理系统，基于SaaS架构，支持多租户管理，具有未来科技感的UI设计。

## 🌟 功能特性

### 核心功能
- 🐾 **宠物管理** - 宠物信息的增删改查
- 💊 **健康管理** - 宠物健康记录管理
- 🍖 **饮食管理** - 宠物喂食记录
- 🛁 **卫生管理** - 宠物卫生清洁记录
- 📊 **日常监控** - 宠物日常监控数据
- 📈 **成长分析** - 宠物成长数据可视化分析
- 👥 **宠友互动** - 宠友社区交流
- 📸 **日常分享** - 宠物生活分享

### 技术特性
- 🏗️ **SaaS多租户架构** - 支持多租户数据隔离
- 🚀 **Spring Boot后端** - 现代化的Java后端框架
- ⚡ **Vue 3前端** - 渐进式JavaScript框架
- 🎨 **Element Plus** - 企业级UI组件库
- 📱 **响应式设计** - 适配各种屏幕尺寸
- 🔐 **JWT认证** - 安全的用户认证机制

## 🛠️ 技术栈

### 后端
- **框架**: Spring Boot 3.1.5
- **数据访问**: MyBatis Plus 3.5.5
- **数据库**: MySQL 8.0
- **认证**: JWT
- **工具库**: Hutool
- **API文档**: Knife4j

### 前端
- **框架**: Vue 3.4
- **构建工具**: Vite 5.0
- **UI组件**: Element Plus 2.5
- **路由**: Vue Router 4.2
- **状态管理**: Pinia 2.1
- **HTTP客户端**: Axios 1.6
- **图表**: ECharts 5.4
- **样式**: Sass

## 📁 项目结构

```
PetIMS/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/petims/
│   │   │   │   ├── common/      # 公共模块
│   │   │   │   ├── config/      # 配置类
│   │   │   │   ├── controller/  # 控制器
│   │   │   │   ├── entity/      # 实体类
│   │   │   │   ├── mapper/      # 数据访问层
│   │   │   │   └── service/     # 业务逻辑层
│   │   │   └── resources/
│   │   │       └── application.yml
│   └── pom.xml
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── api/             # API接口
│   │   ├── assets/          # 静态资源
│   │   ├── components/      # 公共组件
│   │   ├── router/          # 路由配置
│   │   ├── store/           # 状态管理
│   │   ├── views/           # 页面组件
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── database.sql             # 数据库脚本
├── ARCHITECTURE.md          # 架构文档
└── README.md
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE petims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行数据库脚本：
```bash
mysql -u root -p petims < database.sql
```

3. 修改后端配置文件 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/petims?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: your_username
    password: your_password
```

### 后端启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端服务将在 http://localhost:8080/api 启动

API文档地址：http://localhost:8080/api/doc.html

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 http://localhost:3000 启动

## 📖 使用说明

1. 访问 http://localhost:3000
2. 使用账号登录（需要先在数据库中创建用户）
3. 开始使用宠物管理系统的各项功能

## 🔧 开发指南

### 后端开发
- 遵循阿里巴巴Java开发规范
- 使用MyBatis Plus进行数据访问
- 接口遵循RESTful风格

### 前端开发
- 使用Vue 3 Composition API
- 组件化开发
- 响应式设计

## 🤝 贡献

欢迎提交Issue和Pull Request！

## 📄 许可证

MIT License

## 👥 作者

PetIMS Team

## 📧 联系方式

如有问题，请提交Issue。

---

**PetIMS** - 让宠物管理更简单！🐕‍🦺🐱
