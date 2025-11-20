# 1. 基础镜像：根据你的 JDK 版本选择 (这里以 JDK 17 为例，如果是 JDK 8 请换成 openjdk:8-jdk-alpine)
FROM eclipse-temurin:17-jdk-alpine

# 2. 作者/维护者信息 (可选)
LABEL maintainer="Conpom_sp"

# 3. 将 target 目录下的 jar 包复制到容器内，并重命名为 app.jar
# 注意：如果你 target 下有多个 jar，这里最好写确定的名字，比如 target/demo-0.0.1.jar
COPY target/*.jar app.jar

# 4. 暴露端口 (只是文档作用，实际映射要在 run 命令指定)
# 注意：这里要跟你 application.yml 里的 server.port 保持一致
EXPOSE 8081

# 5. 启动命令
ENTRYPOINT ["java", "-jar", "/app.jar"]