# 基础镜像
FROM openjdk:17-jdk-alpine

# 指定工作目录
WORKDIR /app

# 将 jar 包添加到工作目录，比如 target/ischool-community-service-0.0.1-SNAPSHOT.jar
ADD target/ischool-community-1.0-SNAPSHOT.jar .

# 暴露端口
EXPOSE 9004

# 启动命令
ENTRYPOINT ["java","-jar","/app/ischool-community-1.0-SNAPSHOT.jar","--spring.profiles.active=prod"]
