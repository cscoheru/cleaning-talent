# Cleaning Talent LMS Backend

Spring Boot 后端服务，提供用户认证、课程管理、学习记录等核心功能。

## 技术栈

- **Spring Boot** 3.2.0
- **Java** 17
- **MyBatis-Plus** 3.5.5
- **PostgreSQL** 15+
- **Spring Security** + JWT

## 项目结构

```
backend/
├── src/main/java/com/lms/
│   ├── LmsApplication.java      # 主应用类
│   ├── config/                   # 配置类
│   │   ├── CorsConfig.java
│   │   ├── SecurityConfig.java
│   │   ├── MyBatisPlusConfig.java
│   │   ├── JwtAuthenticationFilter.java
│   │   └── MetaObjectHandler.java
│   ├── controller/               # 控制器
│   │   ├── AuthController.java
│   │   ├── CourseController.java
│   │   └── LearningController.java
│   ├── service/                  # 服务层
│   │   ├── AuthService.java
│   │   ├── CourseService.java
│   │   └── LearningService.java
│   ├── mapper/                   # MyBatis Mapper
│   │   ├── UserMapper.java
│   │   ├── CourseMapper.java
│   │   ├── CategoryMapper.java
│   │   ├── ChapterMapper.java
│   │   ├── UserLearningRecordMapper.java
│   │   └── PointsRecordMapper.java
│   ├── entity/                   # 实体类
│   │   ├── User.java
│   │   ├── Course.java
│   │   ├── Category.java
│   │   ├── Chapter.java
│   │   ├── UserLearningRecord.java
│   │   └── PointsRecord.java
│   ├── dto/                      # 数据传输对象
│   │   ├── ApiResponse.java
│   │   ├── PageResponse.java
│   │   └── ... (各种请求/响应DTO)
│   ├── utils/                    # 工具类
│   │   └── JwtUtil.java
│   └── exception/                # 异常处理
│       ├── GlobalExceptionHandler.java
│       ├── BusinessException.java
│       └── ResourceNotFoundException.java
└── src/main/resources/
    └── application.yml           # 应用配置
```

## 环境要求

- JDK 17+
- Maven 3.6+
- PostgreSQL 15+

## 构建和运行

### 1. 安装 Maven（如果未安装）

```bash
# macOS
brew install maven

# Linux
sudo apt-get install maven

# 验证安装
mvn --version
```

### 2. 编译项目

```bash
cd backend
mvn clean package
```

### 3. 运行项目

```bash
# 方式1：使用 Maven 插件
mvn spring-boot:run

# 方式2：使用 JAR 文件
java -jar target/cleaning-talent-backend-0.0.1-SNAPSHOT.jar
```

### 4. 验证服务启动

访问健康检查端点：
```bash
curl http://localhost:8080/actuator/health
```

## API 端点

### 认证模块

| 端点 | 方法 | 描述 |
|------|------|------|
| `/api/v1/auth/login` | POST | 用户登录 |
| `/api/v1/auth/logout` | POST | 用户登出 |
| `/api/v1/auth/refresh` | POST | 刷新 Token |
| `/api/v1/auth/me` | GET | 获取当前用户信息 |

### 课程模块

| 端点 | 方法 | 描述 |
|------|------|------|
| `/api/v1/courses` | GET | 获取课程列表 |
| `/api/v1/courses/{id}` | GET | 获取课程详情 |
| `/api/v1/courses` | POST | 创建课程（需要 ADMIN 权限） |
| `/api/v1/courses/{id}` | PUT | 更新课程（需要 ADMIN 权限） |
| `/api/v1/courses/{id}` | DELETE | 删除课程（需要 ADMIN 权限） |

### 学习模块

| 端点 | 方法 | 描述 |
|------|------|------|
| `/api/v1/learning/progress` | GET | 获取学习进度 |
| `/api/v1/learning/record` | POST | 记录学习行为 |
| `/api/v1/learning/complete` | POST | 完成课程 |
| `/api/v1/learning/history` | GET | 获取学习历史 |

## 测试登录

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@lms.demo","password":"password123"}'
```

响应示例：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "admin@lms.demo",
      "name": "管理员",
      "avatarUrl": null,
      "points": 0,
      "role": "ADMIN"
    }
  },
  "timestamp": 1709356800000
}
```

## 环境变量

可以通过环境变量覆盖配置：

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/cleaning_talent_demo
export DATABASE_USER=postgres
export DATABASE_PASSWORD=your_password
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000
```

## 数据库配置

默认连接到远程数据库：
- Host: 139.224.42.111
- Port: 5432
- Database: cleaning_talent_demo
- User: postgres

本地开发时，可以修改 `application.yml` 或使用环境变量。

## 开发说明

### 统一响应格式

所有 API 返回统一格式：
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1709356800000
}
```

### 认证方式

使用 JWT Token 认证：
```
Authorization: Bearer {token}
```

### 错误码

- 200: 成功
- 400: 请求参数错误
- 401: 未认证
- 403: 无权限
- 404: 资源不存在
- 1001-1099: 用户相关错误
- 2001-2099: 课程相关错误
- 3001-3099: 学习相关错误
- 4001-4099: 积分相关错误

## 开发者

- R03: 后端工程师

## 许可证

MIT
