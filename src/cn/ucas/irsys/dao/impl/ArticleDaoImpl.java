package cn.ucas.irsys.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.ucas.irsys.dao.ArticleDao;
import cn.ucas.irsys.domain.Article;
import cn.ucas.irsys.utils.DBCPUtil;
import cn.ucas.irsys.utils.IdGenerator;


public class ArticleDaoImpl implements ArticleDao {
	private QueryRunner qr = new QueryRunner(DBCPUtil.getDatasource());

	public void save(Article article) {
		article.setId(IdGenerator.idGenerator());
		try {
			qr.update("insert into article(id,title,date,url,content) values(?,?,?,?,?)", article.getId(), article.getTitle(),
					article.getDate(), article.getUrl(),article.getContent());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 批量添加数据到数据库
	 */
	public void batchSave(List<Article> articles) {
		try {
			Object[][] params = new Object[articles.size()][];
			for(int i = 0;i<articles.size();i++) {
				Article a = articles.get(i);
				a.setId(IdGenerator.idGenerator());
				params[i] = new Object[] {a.getId(),a.getTitle(),a.getDate(),a.getUrl(),a.getContent()};
			}
			qr.batch("insert into article(id,title,date,url,content) values(?,?,?,?,?)", params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteArticleById(String id) {
		try {
			qr.update("delete from article where id = ?", id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void updateArticle(String id, Article article) {

	}

	public Article findArticleById(String id) {
		Article article;
		try {
			article = qr.query("select * from article where id = ?", id, new BeanHandler<Article>(Article.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return article;
	}

	public List<Article> findArticleByIds(String[] ids) {
		return null;
	}

}
