# Backend Dockerfile - Spring Boot Application
# 多阶段构建：先构建，后打包运行镜像
# 用途：Railway 部署或本地 Docker 运行

# ================================
# Stage 1: Build Stage
# ================================
FROM eclipse-temurin:17-jdk-alpine AS builder

# 设置工作目录
WORKDIR /build

# 复制构建文件
COPY build.gradle build.gradle.kts gradlew ./
COPY gradle gradle
COPY src src

# 构建（跳过测试以加快构建速度）
RUN ./gradlew clean build -x test --no-daemon || \
    gradle clean build -x test --no-daemon

# ================================
# Stage 2: Runtime Stage
# ================================
FROM eclipse-temurin:17-jre-alpine

# 安装时区数据和字体（可选，用于日期显示等）
RUN apk add --no-cache tzdata curl

# 设置时区
ENV TZ=Asia/Shanghai

# 创建应用用户（安全最佳实践）
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 设置工作目录
WORKDIR /app

# 从构建阶段复制 JAR 文件
COPY --from=builder /build/build/libs/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM 参数优化
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:InitialRAMPercentage=50.0 \
               -XX:+UseG1GC \
               -Djava.security.egd=file:/dev/./urandom \
               -Dspring.profiles.active=production"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
