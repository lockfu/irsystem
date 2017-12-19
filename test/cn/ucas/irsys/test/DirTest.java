package cn.ucas.irsys.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.junit.Test;

public class DirTest {

	@Test
	public void test() throws Exception{
		String str = "差		a	7254";
		System.out.println(str.split("\t")[0]);
	}
	
	@Test
	public void test1() throws Exception{
		File temp = new File("/home/lockjk/nplseg/nplseg/library/default.dic");
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(temp));
		BufferedReader bufr = new BufferedReader(new InputStreamReader(bin, "utf-8"), 1024*1024);
		FileWriter fw = new FileWriter(new File("/home/lockjk/tempfile/mydic"));
		while(bufr.ready()) {
			String line = bufr.readLine();
			fw.append(line.split("\t")[0] + "\n");
		}
		fw.flush();
		bufr.close();
	}
	
}
