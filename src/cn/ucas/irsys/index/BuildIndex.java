package cn.ucas.irsys.index;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import cn.ucas.irsys.domain.Article;
import cn.ucas.irsys.utils.DBCPUtil;
import cn.ucas.irsys.utils.LuceneUtil;

public class BuildIndex {
	
	private static QueryRunner qr = new QueryRunner(DBCPUtil.getDatasource());
	
	/**
	 * Build Index 
	 */
	public static void buildIndex(){
		
		int max = 10000;
		int begin = 0;
		int count = 0;
		
		// use Executors to manange Thread 
		ExecutorService exec = Executors.newCachedThreadPool();
		
		while(true) {
			count++;
			List<Article> lists = new ArrayList<Article>();
			try {
				String sql = "select id,title,date,url,content from article limit ?,?";
				lists = qr.query(sql, new BeanListHandler<Article>(Article.class),begin,max);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			begin = count * max;
			CreatePartIndexThread.count = count;
			// add Thread to CacheThreadPool
			exec.execute(new CreatePartIndexThread(lists));
			if(lists.size() < max) {
				break;
			}
		}
		
		exec.shutdown();
		
		while(true) {
			if(exec.isTerminated()) {  // If all thread terminate ? (combine Index) : (continue query)
				System.out.println("=============end thread======================");
				combineIndex();
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * combine Index from ./tempIndex
	 */
	public static void combineIndex() {
		IndexWriter indexWriter;
		File tempFile;
		try {
			Properties gprop = DBCPUtil.getGProperties();
			Directory resultIndex = FSDirectory.open(new File(gprop.getProperty("finalIndexDir")));
			indexWriter = new IndexWriter(resultIndex, LuceneUtil.getAnalyzer(), MaxFieldLength.LIMITED);
			tempFile = new File(gprop.getProperty("tempIndexDir"));
			for(File temp : tempFile.listFiles()) {
				if(temp.isDirectory() && temp.exists()) {
					Directory d = FSDirectory.open(temp);
					indexWriter.addIndexesNoOptimize(d);
				}
			}
			indexWriter.optimize();
			indexWriter.commit();
			indexWriter.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("===============-----end index building---- ==================");
	}
	
	public static void main(String[] args) {
		buildIndex();
	}
}
