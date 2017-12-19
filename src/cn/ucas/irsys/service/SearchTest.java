package cn.ucas.irsys.service;

import java.util.List;

import org.apache.lucene.document.Document;
import org.junit.Test;

import cn.ucas.irsys.dao.impl.ArticleIndexDao;
import cn.ucas.irsys.domain.QueryResult;
import cn.ucas.irsys.utils.DocumentArticleUtil;

public class SearchTest {
	
	@Test
	public void testSearch() throws Exception{
		ArticleIndexDao aDao = new ArticleIndexDao();
		String queryString = "十九大";
		QueryResult queryResult = aDao.search(queryString, 0, 10);
		List<Document> lists = queryResult.getList();
		for(Document doc : lists) {
			System.out.println(DocumentArticleUtil.document2Article(doc));
		}
		System.out.println("==================="+queryResult.getCount()+"================");
	}
}
