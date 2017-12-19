package cn.ucas.irsys.index;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import cn.ucas.irsys.domain.Article;
import cn.ucas.irsys.utils.LuceneUtil;

public class CreatePartIndexThread implements Runnable {
	
	
	public static int count = 1;
	public static int endIndex = 1;
	private IndexWriter indexWriter;
	private List<Article> lists = new ArrayList<Article>();
	public CreatePartIndexThread(List<Article> lists) {
		this.lists = lists;
	}
	
	public void run() {
		try {
			String filePath =  "./tempIndex/indexDir"+count;
			System.out.println("ThreadName: "+ Thread.currentThread().getName() + "====== begin build index " + filePath + "================");
			Directory directory = FSDirectory.open(new File(filePath));
			indexWriter = new IndexWriter(directory, LuceneUtil.getAnalyzer(), MaxFieldLength.LIMITED);
			Article a = lists.get(0);
			
			Document doc = new Document();
			Field idField = new Field("id", a.getId(), Store.YES, Index.NOT_ANALYZED);
			doc.add(idField);
			Field titleField    =   new Field("title",   a.getTitle(), Store.YES, Index.ANALYZED);   
			doc.add(titleField);
			Field dateField     =   new Field("date",    a.getDate(), Store.YES, Index.NOT_ANALYZED);   
			doc.add(dateField);
			Field urlField      =   new Field("url",     a.getUrl(), Store.YES, Index.NOT_ANALYZED);     
			doc.add(urlField);
			Field contentField  =   new Field("content", a.getContent(), Store.YES, Index.ANALYZED); 
			doc.add(contentField);
			
			indexWriter.addDocument(doc);
			
			
			for(int i = 1;i<lists.size();i++) {
				doc = new Document();
				idField.setValue(lists.get(i).getId());
				doc.add(idField);
				
				titleField.setValue(lists.get(i).getTitle());
				doc.add(titleField);
				
				dateField.setValue(lists.get(i).getDate());
				doc.add(dateField);
				
				urlField.setValue(lists.get(i).getUrl());
				doc.add(urlField);
				
				contentField.setValue(lists.get(i).getContent());
				doc.add(contentField);
				
				indexWriter.addDocument(doc);
				
				if(i % 1000 == 0) {
//					indexWriter.optimize();
					indexWriter.commit();
				}
			}
			
			indexWriter.optimize();
			indexWriter.commit();
			indexWriter.close();
			
			System.out.println("==========end build index  " + filePath + " ============================");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
