package cn.ucas.irsys.test;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import cn.ucas.irsys.domain.Article;
import cn.ucas.irsys.utils.DBCPUtil;

public class TestDbcpUtil {
	@Test
	public void test() throws Exception{
		QueryRunner qr = new QueryRunner(DBCPUtil.getDatasource());
		try {
			Article a = qr.query("select * from article where id = ?", "000c9710-0745-4f1f-aa3b-f41a223a3b22",new BeanHandler<Article>(Article.class));
			System.out.println(a.getTitle() + " --- " +a.getUrl());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
