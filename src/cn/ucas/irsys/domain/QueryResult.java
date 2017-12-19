package cn.ucas.irsys.domain;

import java.util.List;

public class QueryResult {
	
	private List list;
	private int count;
	
	public QueryResult(List list, int count) {
		super();
		this.list = list;
		this.count = count;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
