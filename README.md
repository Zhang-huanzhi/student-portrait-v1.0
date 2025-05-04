# 学生用户画像系统 v1.0

## 项目简介

学生用户画像系统是一个基于 Spring Boot 和 Vue.js 构建的全栈应用，旨在帮助教育工作者更好地了解和管理学生信息，通过数据分析和知识库管理，为学生提供个性化的教育服务和支持。

## 系统功能

- **学生信息管理**：记录和管理学生的基本信息和学习状况
- **用户画像分析**：基于学生数据构建用户画像，分析学习行为和特点
- **知识段管理**：对教学知识进行分段管理和组织
- **智能问答服务**：基于 Dify AI 提供的智能问答功能，为学生提供学习支持

## 技术栈

### 后端
- **框架**：Spring Boot
- **数据库**：[根据配置使用]
- **API**：RESTful API
- **集成**：Dify Knowledge Base 服务

### 前端
- **框架**：Vue.js 3
- **UI 组件库**：Element Plus
- **构建工具**：Vite
- **网络请求**：Axios

## 项目结构

```
├── frontend/                  # 前端 Vue.js 项目
│   ├── public/                # 静态资源
│   ├── src/                   # 源代码
│   │   ├── components/        # 组件
│   │   ├── services/          # API 服务
│   │   ├── App.vue            # 主应用组件
│   │   └── main.js            # 应用入口
│   ├── index.html             # HTML 入口
│   └── vite.config.js         # Vite 配置
├── src/                       # 后端 Spring Boot 项目
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/studentportrait/
│   │   │       ├── config/    # 配置类
│   │   │       ├── controller/# 控制器
│   │   │       ├── entity/    # 实体类
│   │   │       ├── repository/# 数据仓库
│   │   │       └── service/   # 服务层
│   │   └── resources/
│   │       └── application.yml# 应用配置
└── pom.xml                    # Maven 项目配置
```

## 安装与运行

### 环境要求
- JDK 17 或更高版本
- Node.js 16 或更高版本
- Maven 3.6 或更高版本

### 后端部署
1. 克隆项目到本地
```bash
git clone https://github.com/Zhang-huanzhi/student-portrait-v1.0.git
cd student-portrait-v1.0
```

2. 使用 Maven 构建项目
```bash
mvn clean install
```

3. 运行 Spring Boot 应用
```bash
mvn spring-boot:run
```

### 前端部署
1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 开发模式运行
```bash
npm run dev
```

4. 构建生产版本
```bash
npm run build
```

## API 文档

系统提供以下主要 API 接口：

- `/api/students` - 学生信息管理
- `/api/chat` - 智能问答服务
- `/api/dify` - Dify 知识库代理服务

## 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进这个项目。

## 许可证

[MIT 许可证](LICENSE)