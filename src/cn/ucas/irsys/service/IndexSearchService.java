package cn.ucas.irsys.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ucas.irsys.dao.impl.ArticleIndexDao;
import cn.ucas.irsys.domain.Article;
import cn.ucas.irsys.domain.QueryResult;

@WebServlet("/IndexSearchService")
public class IndexSearchService extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ArticleIndexDao aDao = new ArticleIndexDao();
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf8");
		String queryString = request.getParameter("queryString");
		QueryResult queryResult = aDao.search(queryString, 0, 10);
		List<Article> ars = queryResult.getList();
		request.setAttribute("ars", ars);
		request.getRequestDispatcher("./WEB-INF/showIndex.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
