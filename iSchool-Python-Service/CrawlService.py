import logging
import re
import time
import traceback

from toollib.guid import SnowFlake

import requests
from pyquery import PyQuery as pq
from urllib.parse import urlparse, parse_qs
from CrawlDao import MyDatabase

JSESSIONID = None  # sessionid
MAX_REQUEST_RETRY_COUNTS = 3  # 请求的最大重试次数
TIME_WAIT_SECONDS = 1  # 正常爬取等待几秒
TIME_WAIT_BLOCKED_SECONDS = 5  # 被墙一个等待几秒
IS_FIRST = True  # 是不是第一次增量爬取
END_ARTICLE_ID = 478  # 结束的文章id
"""
        模版方法：
            1：创建数据库对象
            2：进行数据爬取（结束条件可以抽出来也可以直接在爬取时实现）
            3：处理数据库操作失败的结果
            4：统计信息写日志，关闭数据库连接
        策略模式：
            1：全量爬取或增量爬取
            2：不同学校
        健壮性：
            1：请求数据为空，被block，添加重试机制，再失败，添加错误日志
            2：插入数据失败，重试机制，写入错误日志
"""


# 获取JESSIONID
def get_jsessionid():
    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        'Cache-Control': 'max-age=0',
        'Proxy-Connection': 'keep-alive',
        'Referer': 'https://www.bing.com/',
        'Upgrade-Insecure-Requests': '1',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0',
    }

    response = requests.get('http://jwzx.hrbust.edu.cn/homepage/index.do', headers=headers, verify=False)
    return response.cookies.get("JSESSIONID")


# 全量获取文章数据
def get_hrbust_articles_by_full(page_num, page_size):
    """
    全量爬取哈尔滨理工大学的数据
    :param JSESSIONID:  sessionid
    :param page_num:    爬取的页数
    :param page_size:   爬取的页长
    :return: 爬取的文章列表和是否结束爬取
    """
    global JSESSIONID

    cookies = {
        'JSESSIONID': JSESSIONID,
    }

    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        # 'Cookie': 'JSESSIONID=0E1CE59A9078E7E9460987FBBC9BA9A3.TH',
        'Proxy-Connection': 'keep-alive',
        'Referer': f'http://jwzx.hrbust.edu.cn/homepage/infoArticleList.do;JSESSIONID={JSESSIONID}?columnId=354',
        'Upgrade-Insecure-Requests': '1',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0',
    }

    response = requests.get(
        f'http://jwzx.hrbust.edu.cn/homepage/infoArticleList.do;JSESSIONID={JSESSIONID}?sortColumn=publicationDate&pagingNumberPer={page_size}&columnId=354&sortDirection=-1&pagingPage={page_num}&',
        cookies=cookies,
        headers=headers,
        verify=False,
    )
    html = response.content.decode("UTF-8")

    # 获取每个文章的详细内容
    doc = pq(html)
    if doc is None:  # 说明被墙了，重试一下
        print(response)
        time.sleep(TIME_WAIT_BLOCKED_SECONDS)
        retry_counts = 0
        while doc is None and retry_counts < MAX_REQUEST_RETRY_COUNTS:
            JSESSIONID = get_jsessionid()
            response = requests.get(
                f'http://jwzx.hrbust.edu.cn/homepage/infoArticleList.do;JSESSIONID={JSESSIONID}?sortColumn=publicationDate&pagingNumberPer={page_size}&columnId=354&sortDirection=-1&pagingPage={page_num}&',
                cookies=cookies,
                headers=headers,
                verify=False,
            )
            html = response.content.decode("UTF-8")
            doc = pq(html)
            retry_counts += 1

    # 获取当前页数和总页数做比较，如果相等就停止爬取
    articles = doc("#thirdcontent > div.articleList > ul > li > div > a")
    article_list = []
    for article in articles.items():
        article_href = article.attr("href")
        # 排除一些脏链接
        if not article_href.startswith("infoSingleArticle.do?"):
            continue
        article = get_hrbust_article_info(article_href)
        article_list.append(article)
    current_page = doc(
        ".classicLookSummary.Summary > b:nth-child(2)").text()
    total_page = doc(
        ".classicLookSummary.Summary > b:nth-child(3)").text()
    return article_list, current_page == total_page


# 获取文章详细信息
def get_hrbust_article_info(article_href):
    global JSESSIONID
    # 解析URL以获取查询字符串部分
    parsed_url = urlparse(article_href)
    query_params = parse_qs(parsed_url.query)

    # 提取articleId和columnId
    article_id = query_params.get('articleId')[0]
    column_id = query_params.get('columnId')[0]

    cookies = {
        'JSESSIONID': JSESSIONID,
    }

    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        'Cache-Control': 'max-age=0',
        # 'Cookie': 'JSESSIONID=0E1CE59A9078E7E9460987FBBC9BA9A3.TH',
        'Proxy-Connection': 'keep-alive',
        'Referer': 'http://jwzx.hrbust.edu.cn/homepage/index.do',
        'Upgrade-Insecure-Requests': '1',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0',
    }

    params = {
        'articleId': article_id,
        'column_id': column_id
    }

    response = requests.get(
        f'http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do;JSESSIONID={JSESSIONID}',
        params=params,
        cookies=cookies,
        headers=headers,
        verify=False,
    )
    html = response.content.decode("UTF-8")

    doc = pq(html)
    if doc is None:  # 说明被墙了，重试一下
        print(response)
        time.sleep(TIME_WAIT_BLOCKED_SECONDS)
        retry_counts = 0
        while doc is None and retry_counts < MAX_REQUEST_RETRY_COUNTS:
            JSESSIONID = get_jsessionid()
            response = requests.get(
                f'http://jwzx.hrbust.edu.cn/homepage/infoSingleArticle.do;JSESSIONID={JSESSIONID}',
                cookies=cookies,
                headers=headers,
                verify=False,
            )
            html = response.content.decode("UTF-8")
            doc = pq(html)
            retry_counts += 1
    # 移除前面的内容
    doc("script").remove()  # 移除计算时间的脚本
    doc(".center_menu_l1").remove()  # 移除导航栏
    doc(".Date").remove()  # 移除提示的日期
    doc("#location").remove()  # 移除当前所在系统的位置
    doc("#clear").remove()  # 移除清空按钮（这bug也能写是逆天了）
    doc("#thirdfoot").remove()  # 移除下面的版权声明
    # 处理下载的url，指向大学的网址
    a_items = doc('a')
    for a in a_items.items():
        a_href = a.attr("href")
        if a_href is not None and a_href.startswith("/homepage/downloadTheolFile.do"):
            a.attr("href", "http://jwzx.hrbust.edu.cn" + a_href)  # 修改原始节点的属性值
    # with open(f'{article_id}.html', mode='w', encoding='UTF-8') as f:
    #     f.write(str(doc))
    # 拼装数据，返回出去
    url = "http://jwzx.hrbust.edu.cn/homepage/" + article_href
    # 雪花算法生成id
    unique_id = SnowFlake(worker_id=1, datacenter_id=1).gen_uid()
    article = {}
    article['id'] = unique_id
    article['article_id'] = article_id
    article['url'] = url
    article['title'] = doc("#article h2").text()
    article['content'] = str(doc)
    article['pub_time'] = format_date(doc("#articleInfo ul li:first-child").text())
    article['school'] = 'HRBUST'
    return article


# 增量获取文章数据
def get_hrbust_articles_by_incr(page_num, page_size, end_article_id):
    global JSESSIONID
    global END_ARTICLE_ID
    global IS_FIRST
    """
    增量爬取哈尔滨理工大学的数据
    :param JSESSIONID:  sessionid
    :param page_num:    爬取的页数
    :param page_size:   爬取的页长
    :param end_article_id:  结束爬取的文章id
    :return: 爬取的文章列表和是否结束爬取
    """
    cookies = {
        'JSESSIONID': JSESSIONID,
    }

    headers = {
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        # 'Cookie': 'JSESSIONID=0E1CE59A9078E7E9460987FBBC9BA9A3.TH',
        'Proxy-Connection': 'keep-alive',
        'Referer': f'http://jwzx.hrbust.edu.cn/homepage/infoArticleList.do;JSESSIONID={JSESSIONID}?columnId=354',
        'Upgrade-Insecure-Requests': '1',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 Edg/122.0.0.0',
    }

    response = requests.get(
        f'http://jwzx.hrbust.edu.cn/homepage/infoArticleList.do;JSESSIONID={JSESSIONID}?sortColumn=publicationDate&pagingNumberPer={page_size}&columnId=354&sortDirection=-1&pagingPage={page_num}&',
        cookies=cookies,
        headers=headers,
        verify=False,
    )
    html = response.content.decode("UTF-8")

    # 获取每个文章的详细内容
    doc = pq(html)
    if doc is None:  # 说明被墙了，重试一下
        print(response)
        time.sleep(TIME_WAIT_BLOCKED_SECONDS)
        retry_counts = 0
        while doc is None and retry_counts < MAX_REQUEST_RETRY_COUNTS:
            JSESSIONID = get_jsessionid()
            response = requests.get(
                f'http://jwzx.hrbust.edu.cn/homepage/infoArticleList.do;JSESSIONID={JSESSIONID}?sortColumn=publicationDate&pagingNumberPer={page_size}&columnId=354&sortDirection=-1&pagingPage={page_num}&',
                cookies=cookies,
                headers=headers,
                verify=False,
            )
            html = response.content.decode("UTF-8")
            doc = pq(html)
            retry_counts += 1

    # 获取当前页数和总页数做比较，如果相等就停止爬取
    articles = doc("#thirdcontent > div.articleList > ul > li > div > a")
    article_list = []
    for article in articles.items():
        article_href = article.attr("href")
        # 排除一些脏链接
        if not article_href.startswith("infoSingleArticle.do?"):
            continue
        # 判断增量爬取是否结束
        # 解析URL以获取查询字符串部分
        parsed_url = urlparse(article_href)
        query_params = parse_qs(parsed_url.query)
        # 提取articleId
        article_id = int(query_params.get('articleId')[0])
        # 记录结束的articleId
        if IS_FIRST and article_id != end_article_id:
            END_ARTICLE_ID = article_id
            IS_FIRST = False
        elif IS_FIRST:
            END_ARTICLE_ID = end_article_id
            IS_FIRST = False
        # print(article_id, end_article_id, type(article_id), type(end_article_id))
        if article_id <= end_article_id:  # 说明当前已经越界了
            return article_list, True
        article = get_hrbust_article_info(article_href)
        article_list.append(article)
    return article_list, False


# 格式化时间
def format_date(pub_date):
    # 提取日期
    date_match = re.search(r'\d{4}-\d{2}-\d{2}', pub_date)
    if date_match:
        formatted_date = date_match.group(0)
    else:
        formatted_date = '2024-2-31'
    return formatted_date


# 全量获取数据
def full_data(schoool_name):
    print("全量更新数据~~~")
    global JSESSIONID
    """
    全量爬取，模版方法和策略模式
    :param schoool_name: 学校名称
    :return:   是否执行成功
    """
    JSESSIONID = get_jsessionid()  # 获取sessionid
    # 创建数据库对象
    my_db = MyDatabase()
    del_status = my_db.delete_all_data()
    if del_status != 'success':
        logging.error(f"删除info表所有数据失败，失败原因：{del_status}")
        return False
    page_num = 1
    PAGE_SIZE = 12
    retry_counts = 0
    MAX_RETRY_COUNTS = 3
    count = 0
    start_time = time.time()
    while True:
        try:
            if schoool_name == 'HRBUST':
                articles, ended = get_hrbust_articles_by_full(page_num, PAGE_SIZE)
            else:
                articles, ended = get_hrbust_articles_by_full(page_num, PAGE_SIZE)
            count += len(articles)
            msg = my_db.save_data(articles)
            # 写入数据库失败的处理逻辑
            if msg != 'success' and retry_counts <= MAX_RETRY_COUNTS:
                # todo: 写入数据库操作失败原因 msg
                logging.error(f"全量同步写入数据库时失败，失败信息为{msg}")
                retry_counts += 1
                return False
            else:  # 重试
                my_db.save_data(articles)
            # 爬取完成
            print(f"当前正在爬取第{page_num}页，共爬取了{count}条数据")
            if ended:
                break
            page_num += 1  # 移动指针
        except Exception as e:
            # todo:添加错误日志
            logging.error(f"全量同步失败，失败信息为{e}")
            return False
    end_time = time.time()
    my_db.close()
    # todo: 写入执行的详细信息
    logging.info(f"爬取完成，一共爬取{count}个数据，用时{end_time - start_time}s")
    return True


# 增量获取数据
def incr_data(school_name, end_article_id):
    global JSESSIONID
    """
    增量爬取,后续跟全量合并成模版方法
    :param school_name: 学校名称
    :param end_article_id:   截止爬取的文章id
    :return: 是否爬取成功
    """
    JSESSIONID = get_jsessionid()  # 获取sessionid
    # 创建数据库对象
    my_db = MyDatabase()
    del_status = my_db.delete_data_by_id(end_article_id)
    if del_status != 'success':
        logging.error(f"删除info表{end_article_id}之后的数据失败，失败原因：{del_status}")
        return False
    page_num = 1
    PAGE_SIZE = 12
    retry_counts = 0  # 重试次数
    MAX_RETRY_COUNTS = 3  # 最大重试次数
    start_time = time.time()
    count = 0
    while True:
        try:
            if school_name == 'HRBUST':
                articles, ended = get_hrbust_articles_by_incr(page_num, PAGE_SIZE, end_article_id)
            else:
                articles, ended = get_hrbust_articles_by_incr(page_num, PAGE_SIZE, end_article_id)
            count += len(articles)
            msg = my_db.save_data(articles)
            # 写入数据库失败的处理逻辑
            if msg != 'success' and retry_counts >= MAX_RETRY_COUNTS:
                # todo: 写入数据库操作失败原因 msg
                logging.error(f"增量同步写入数据库时失败，失败信息为{msg}")
                my_db.save_data(articles)
                retry_counts += 1
                return False
            if ended:
                break
            page_num += 1  # 移动指针
        except Exception as e:
            # todo：添加错误日志
            traceback.print_exc()
            logging.error(f"增量同步失败，失败信息为{e}")
            return False
    end_time = time.time()
    logging.info(f"爬取完成，一共爬取{count}个数据，用时{end_time - start_time}s")
    my_db.close()
    # todo: 写入执行的详细信息
    return True, END_ARTICLE_ID
