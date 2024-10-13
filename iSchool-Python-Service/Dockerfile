# 使用 Python 基础镜像
FROM python:3.11.2-slim

# 设置工作目录
WORKDIR /app

# 将本地的代码复制到容器中
COPY . .

# 安装依赖包
RUN pip install -r requirements.txt

# 启动 Python 服务 (假设你的主文件是 app.py)
CMD ["python", "CrawlController.py"]
