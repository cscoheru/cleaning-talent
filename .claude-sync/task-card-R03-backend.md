# 📋 任务卡片：R03 后端工程师

## 角色信息

| 字段 | 值 |
|------|-----|
| **角色编号** | R03 |
| **角色名称** | 后端工程师 |
| **主要职责** | 后端业务逻辑、API 接口实现 |
| **工作时长** | 4-6 小时 |
| **开始条件** | 等待 R01（API 规范）和 R02（数据库）完成 |

---

## 会话目标

实现后端核心业务逻辑，包括用户认证、课程管理、学习记录等功能的 API 接口。

---

## 依赖检查

**开始前请确认**：

```bash
# 检查 R01 API 规范是否完成
cat docs/api-spec.md

# 检查 R02 数据库是否初始化
cat .claude-sync/SESSION-LOG.md | grep "R02"

# 检查数据库是否可访问
psql -h 139.224.42.111 -U postgres -d cleaning_talent_demo -c "SELECT COUNT(*) FROM users;"
```

---

## 需要读取的文件

```bash
# API 规范
cat docs/api-spec.md

# 数据库设计
cat docs/database-design.md

# 共享上下文
cat .claude-sync/SHARED-CONTEXT.md
```

---

## 任务清单

### 第一阶段：项目初始化（1小时）

- [ ] 创建 Spring Boot 项目结构
  ```
  backend/
  ├── src/main/java/com/lms/
  │   ├── LmsApplication.java
  │   ├── config/          # 配置类
  │   ├── controller/      # 控制器
  │   ├── service/         # 业务逻辑
  │   ├── mapper/          # MyBatis Mapper
  │   ├── entity/          # 实体类
  │   └── dto/             # 数据传输对象
  └── src/main/resources/
      ├── application.yml   # 配置文件
      ├── schema.sql       # 建表 SQL
      └── data.sql         # 测试数据
  ```

- [ ] 配置依赖（pom.xml）
  - Spring Boot Starter Web
  - Spring Boot Starter Security
  - MyBatis-Plus
  - PostgreSQL Driver
  - JWT（身份认证）

### 第二阶段：基础组件（1小时）

- [ ] 配置 CORS（跨域）
- [ ] 配置全局异常处理
- [ ] 配置统一响应格式
- [ ] 配置 JWT 工具类
- [ ] 配置 MyBatis-Plus

### 第三阶段：用户认证模块（1.5小时）

- [ ] 登录接口
  ```
  POST /api/v1/auth/login
  Body: { username, password }
  Response: { token, userInfo }
  ```

- [ ] 登出接口
  ```
  POST /api/v1/auth/logout
  ```

- [ ] 获取当前用户
  ```
  GET /api/v1/users/me
  ```

- [ ] JWT 拦截器配置

### 第四阶段：课程管理模块（1.5小时）

- [ ] 课程列表
  ```
  GET /api/v1/courses?page=1&size=10
  ```

- [ ] 课程详情
  ```
  GET /api/v1/courses/{id}
  ```

- [ ] 创建课程
  ```
  POST /api/v1/courses
  ```

- [ ] 更新课程
  ```
  PUT /api/v1/courses/{id}
  ```

- [ ] 删除课程
  ```
  DELETE /api/v1/courses/{id}
  ```

### 第五阶段：学习记录模块（1小时）

- [ ] 获取学习进度
  ```
  GET /api/v1/learning/progress
  ```

- [ ] 记录学习行为
  ```
  POST /api/v1/learning/record
  Body: { courseId, progress, duration }
  ```

---

## 输出文件

### 核心代码文件

1. **`backend/pom.xml`** - Maven 依赖配置
2. **`backend/src/main/resources/application.yml`** - 应用配置
3. **`backend/src/main/java/com/lms/config/...`** - 配置类
4. **`backend/src/main/java/com/lms/controller/...`** - 控制器
5. **`backend/src/main/java/com/lms/service/...`** - 服务类
6. **`backend/src/main/java/com/lms/mapper/...`** - MyBatis Mapper
7. **`backend/src/main/java/com/lms/entity/...`** - 实体类

### 代码示例

#### 统一响应格式

```java
package com.lms.dto;

public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    // getters and setters
}
```

#### 登录接口示例

```java
package com.lms.controller;

import com.lms.dto.AuthRequest;
import com.lms.dto.AuthResponse;
import com.lms.dto.ApiResponse;
import com.lms.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword());
        return ApiResponse.success(response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        // TODO: 实现登出逻辑
        return ApiResponse.success(null);
    }
}
```

---

## 完成后要更新的文件

```bash
# 1. 更新进度看板
# 编辑 .claude-sync/PROGRESS-BOARD.md

# 2. 更新代码所有权
# 编辑 .claude-sync/CODE-OWNERSHIP.md
# 声明 /backend/src/ 为 R03 负责

# 3. 更新会话日志
# 编辑 .claude-sync/SESSION-LOG.md

# 4. 更新变更日志
# 编辑 .claude-sync/CHANGE-LOG.md
```

---

## 重要提醒

⚠️ **数据库连接**：
- URL: `jdbc:postgresql://139.224.42.111:5432/cleaning_talent_demo`
- Username: `postgres`
- Password: `WhjQTPAwInc5Vav3sDWe`

⚠️ **API 规范**：
- 所有接口以 `/api/v1` 开头
- 使用统一响应格式 `ApiResponse<T>`
- 错误码遵循 R01 定义的规范

⚠️ **安全注意事项**：
- 密码使用 BCrypt 加密存储
- JWT Token 有效期建议 7 天
- 敏感信息记录日志时脱敏

---

## 开发流程

1. **启动本地开发环境**
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. **测试接口**
   ```bash
   # 登录测试
   curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"123456"}'
   ```

3. **提交代码**
   ```bash
   git add .
   git commit -m "feat(R03): 实现用户认证模块"
   ```

---

## 验收标准

完成本会话后，应该能够：
- [ ] 后端项目可以在本地启动
- [ ] 可以成功连接到节点 A 的数据库
- [ ] 登录接口可以正常工作
- [ ] 课程 CRUD 接口可以正常工作
- [ ] 学习记录接口可以正常工作
- [ ] 所有接口返回统一的响应格式
