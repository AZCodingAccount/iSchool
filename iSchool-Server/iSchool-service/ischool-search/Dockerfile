# 基础镜像
FROM openjdk:17-jdk-alpine

# 指定工作目录
WORKDIR /app

# 将 jar 包添加到工作目录
ADD target/ischool-search-1.0-SNAPSHOT.jar .

# 暴露端口
EXPOSE 9003

# 启动命令
ENTRYPOINT ["java","-jar","/app/ischool-search-1.0-SNAPSHOT.jar","--spring.profiles.active=prod"]
