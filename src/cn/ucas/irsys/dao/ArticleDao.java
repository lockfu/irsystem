package cn.ucas.irsys.dao;

import java.util.List;

import cn.ucas.irsys.domain.Article;


public interface ArticleDao {
	
	/**
	 * save article
	 * @param article
	 */
	public void save(Article article);
	
	/**
	 * delete article by article id
	 * @param id
	 */
	public void deleteArticleById(String id);
	
	
	/**
	 * update article 
	 * @param id   if there is not a article
	 * @param article
	 */
	public void updateArticle(String id,Article article);
	
	/**
	 * get article by article id
	 * @param id
	 * @return
	 */
	public Article findArticleById(String id);
	
	/**
	 * find articles by article ids
	 * @param ids
	 * @return
	 */
	public List<Article> findArticleByIds(String[] ids);
	
	/**
	 * batch add articles
	 * @param articles
	 */
	public void batchSave(List<Article> articles);
}
