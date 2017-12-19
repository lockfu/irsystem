package cn.ucas.irsys.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListTest {
	@Test
	public void testList() throws Exception{
		List<String> lists = new ArrayList<>();
		lists.add("aaa");
		lists.add("bbb");
		lists.add("ccc");
		lists.add("ddd");
		lists.add("eee");
		
		System.out.println(lists.get(lists.size()-1));
		
		
	}
}	
