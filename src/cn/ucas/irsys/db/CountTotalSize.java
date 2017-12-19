package cn.ucas.irsys.db;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.Test;


public class CountTotalSize {
	@Test
	public void testlinner() throws Exception{
		File dump = new File("/home/lockjk/tempfile/temp");
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dump));
		BufferedReader bufr = new BufferedReader(new InputStreamReader(bis, "utf-8"),10*1024*1024);
		int count = 0;
		while(bufr.ready()) {
			String line = bufr.readLine();
			if(line.contains("url: ")) {
				count++;
			}
		}
		System.out.println("totalcount: " + count);
	}
}
