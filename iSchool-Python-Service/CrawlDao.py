import pymysql

"""
    将数据写入到数据库
"""


class MyDatabase:

    # 初始化数据库连接环境
    def __init__(self):
        self.db = pymysql.connect(host='localhost', user='root', password='123456', database='ischool')
        self.cursor = self.db.cursor()

    def save_data(self, datas):
        insert_sql = """
        INSERT INTO info(id,article_id,url,title,content,school,pub_time) VALUES (%s,%s,%s,%s,%s,%s,%s)
        """
        try:
            for data in datas:
                values = (
                    data.get('id'),
                    data.get('article_id'),
                    data.get('url'),
                    data.get('title'),
                    data.get('content'),
                    data.get('school', "HRBUST"),
                    data.get('pub_time')
                )
                self.cursor.execute(insert_sql, values)
            self.db.commit()
            return 'success'
        except Exception as e:
            self.db.rollback()
            return e

    # 幂等性需要进行删除
    def delete_data_by_id(self, end_article_id):
        delete_sql = """
        delete from info where article_id>%s
        """
        try:
            self.cursor.execute(delete_sql, (end_article_id,))
            self.db.commit()
            return 'success'
        except Exception as e:
            self.db.rollback()
            return e

    # 删除数据库所有数据
    def delete_all_data(self):
        delete_sql = """
        delete from info
        """
        try:
            self.cursor.execute(delete_sql)
            self.db.commit()
            return 'success'
        except Exception as e:
            self.db.rollback()
            return e

    # 关闭数据库连接
    def close(self):
        self.cursor.close()
        self.db.close()
