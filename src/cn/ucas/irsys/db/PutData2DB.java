package cn.ucas.irsys.db;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import cn.ucas.irsys.dao.ArticleDao;
import cn.ucas.irsys.dao.impl.ArticleDaoImpl;
import cn.ucas.irsys.domain.Article;
import cn.ucas.irsys.utils.DBCPUtil;


/**
 * 将临时的新闻文件加入到数据库中
 * @author lockjk
 *
 */
public class PutData2DB {
	
	@Test
	public void test()throws Exception{
		Properties gprop = DBCPUtil.getGProperties();
		String tempfile = gprop.getProperty("tempfile");
		String encoding = gprop.getProperty("encoding");
		
		ArticleDao aDao = new ArticleDaoImpl();
		File dump = new File(tempfile);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dump));
		BufferedReader bufr = new BufferedReader(new InputStreamReader(bis, encoding),10*1024*1024);
		List<Article> articles = new ArrayList<Article>();
		boolean isFull = false;
		Article article = new Article();
		while(bufr.ready()) {
			String line = bufr.readLine();
			
			if(line.contains("url: ")) {
				isFull = false;
				article.setUrl(line.substring("url: ".length()));
			}else if(line.contains("title: ")) {
				article.setTitle(line.substring("title: ".length()));
			}else if(line.contains("date: ")) {
				article.setDate(line.substring("date: ".length()));
			}else if(line.contains("content: ")) {
				article.setContent(line.substring("content: ".length()));
				isFull = true;
			}
			
			if(isFull) {
				articles.add(article);
				article = new Article();
			}
			
			if(articles.size() % 1000 == 0) {
				aDao.batchSave(articles);
				articles.clear();
			}
		}
		aDao.batchSave(articles);
		bufr.close();
	}
	
}
