import json
import logging
import os
from datetime import datetime
from logging.handlers import RotatingFileHandler

from flask import Flask, render_template, request, make_response  # pip install Flask
from flask_cors import CORS, cross_origin
from CrawlService import full_data, incr_data
import re

app = Flask(__name__)
CORS(app)

# 使用basicConfig设置基本参数（不包括处理器）
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
# 写入日志文件的配置
"""
class NoColorFormatter(logging.Formatter):
    def format(self, record):
        ansi_escape = re.compile(r'\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])')
        record.msg = ansi_escape.sub('', record.msg)
        return super().format(record)


# 创建一个带有UTF-8编码的RotatingFileHandler
handler = RotatingFileHandler('logfile.log', maxBytes=1 * 1024 * 1024, backupCount=3, encoding='utf-8')
handler.setLevel(logging.INFO)

# 创建一个格式器并将其添加到处理器
formatter = NoColorFormatter('%(asctime)s - %(levelname)s - %(message)s', datefmt='%Y-%m-%d %H:%M:%S')
handler.setFormatter(formatter)

# 获取根日志记录器并添加处理器
logger = logging.getLogger()
logger.addHandler(handler)
"""

"""
    一共开发两个接口，一个全量，一个增量，返回格式 error|ok
    flask教程：https://blog.csdn.net/wly55690/article/details/131683846
"""


@app.route("/", methods=['GET'])
# @cross_origin() # 允许所有域跨域访问这个路由
def hello():
    return 'hello'


# 全量获取
# @param school 大学
@app.route("/full_sync_data", methods=['POST'])
def full_sync_data():
    try:
        # 获取json数据
        json_data = request.get_json()
        # 取出school
        school = json_data.get('school', 'HRBUST')

        logging.info(f"全量同步大学{school}数据")

        is_success = full_data(school)  # 获取同步成功或者失败
        if is_success:
            response = make_response('ok', 200)
        else:
            response = make_response('error', 500)
            logging.info(f"全量同步大学{school}数据失败")

        return response
    except:
        return make_response('error', 500)


# 增量获取
# @param school 大学
# @param end_article_id 获取的截止id
@app.route("/incr_sync_data", methods=['POST'])
def incr_sync_data():
    try:
        # 获取json数据
        json_data = request.get_json()
        # 取出school和end_article_id
        school = json_data.get('school', 'HRBUST')
        end_article_id = json_data.get('end_article_id', 4720)
        logging.info(
            f"增量同步大学{school}数据，截止公告为{end_article_id}")
        is_success, article_id = incr_data(school, end_article_id)  # 获取同步成功或者失败
        if is_success:
            data = {}
            data['msg'] = 'ok'
            data['articleId'] = article_id
            response = make_response(json.dumps(data), 200)
        else:
            data = {}
            data['msg'] = 'error'
            response = make_response(json.dumps(data), 500)
            logging.error(
                f"增量同步大学{school}数据失败，截止公告为{end_article_id}")
        response.headers['Content-Type'] = 'application/json'
        return response
    except:
        data = {}
        data['msg'] = 'error'
        response = make_response(json.dumps(data), 500)
        response.headers['Content-Type'] = 'application/json'
        return response


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=10086)
