package cn.ucas.irsys.db;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ucas.irsys.domain.Article;
import cn.ucas.irsys.utils.DBCPUtil;

public class OpData2TempFile {
	private static String[] filepaths;
	static {
		try {
			InputStream in = OpData2TempFile.class.getClassLoader().getResourceAsStream("dbdatasource.properties");
			Properties prop = new Properties();
			prop.load(in);
			Set<Object> keys = prop.keySet();
			filepaths = new String[keys.size()];
			Iterator<Object> iterator = keys.iterator();
			int count = 0;
			while(iterator.hasNext()) {
				String key = (String)iterator.next();
				filepaths[count++] = prop.getProperty(key);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		for(String filepath : filepaths) {
			analyzerDatasource(filepath);
		}
	}
	
	public static void analyzerDatasource(String filepath) throws Exception{
		Properties gprop = DBCPUtil.getGProperties();
		String tempfile = gprop.getProperty("tempfile");
		String encoding = gprop.getProperty("encoding");
		Random rand = new Random();
		String url = "";
		File dump = new File(filepath);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dump));
		BufferedReader bufr = new BufferedReader(new InputStreamReader(bis, encoding),10*1024*1024);
		FileWriter fw = new FileWriter(new File(tempfile),true);
		String urlregex = ".*/2017.*.s?html?";
		boolean ifmatch = false;  // 可用的文章
		boolean isfirst = true;   // 第一次匹配 用来截取title
		List<Article> articles = new ArrayList<Article>();
		
		while(bufr.ready()) {
			Article article = new Article();
			String line = bufr.readLine();
//====================================对URL进行处理==============================================================			
			if(line.contains("URL:: ") ) {
				if(line.matches(urlregex)) {
					url = line.split("URL:: ")[1];
					fw.append("url: "+ url + "\n");
					ifmatch = true;   
					isfirst = true;
				}else {
					ifmatch = false;
					continue;
				}
			}else {
				if(ifmatch) {
					if( line.contains("ParseText::") || line.contains("Recno::") || line.length() < 110) {
						continue;
					}
					String title = "";
					String date = "";
					if(isfirst) {
//==========================================对日期的处理======================================================
						String regex = "\\d{4}-\\d{2}-\\d{2}";
						String regexc = "\\d{4}年\\d{2}月\\d{2}日";
						Pattern p1 = Pattern.compile(regex);
						Pattern p2 = Pattern.compile(regexc);
						
						Matcher matcher = p1.matcher(line);
						if(matcher.find()) {
							date = line.substring(matcher.start(),matcher.end());
						}else {
							matcher = p2.matcher(line);
							if(matcher.find()) {
								date = line.substring(matcher.start(),matcher.end());
							}
						}
						fw.append("date: " + date + "\n");
//====================================================对标题的处理=================================================
						// 对chinanews中浙江新闻的特殊处理
						if(line.contains("中国新闻网-浙江新闻")) {
							int index = line.indexOf("中新记者看浙江");
							title = line.substring(index+1, index+50);
							fw.append("title: " + title + "\n");
							fw.append("content: " + filterEmoji(line) + "\n");
							continue;
						}
						
						String templine = line;
						line = line.substring(0, 100);
						title = line;
						String[] sregex = {"_","-","|","—"};
						int[] aindex = new int[sregex.length];
						for(int i = 0;i<aindex.length;i++) {
							int index = line.indexOf(sregex[i]);
							if(index < 0) {
								aindex[i] = rand.nextInt(500)+1000;
							}else {
								aindex[i] = index;
							}
						}
						Arrays.sort(aindex);
						int min = aindex[0];
						for(int i = 0;i<sregex.length;i++) {  // 取最小的分隔符
							if(min == line.indexOf(sregex[i])) {
								title = line.substring(0,min);
								break;
							}
						}
						// 如果没有分割title成功 就选择第二个空格前的字符串作为标题
						if(title.equals(line)) {
							int sindex = line.indexOf(" ");
							int enindex = line.indexOf(" ", sindex+1);
							try {
								title = line.substring(0, enindex);
							}catch(Exception e) {
								title = line;
							}
						}
						// 对特殊title的特殊处理
						title = delWithSpecialTitle(title,url,line);
						fw.append("title: " + filterEmoji(title) + "\n");
						String content = filterEmoji(templine);
						fw.append("content: " + content + "\n");
						isfirst = false;
					}
				}else {
					continue;
				}
			}
		}
		bufr.close();
		System.out.println("analyzer success!!!!");
	}
	
	// 过滤特殊字符
	public static String filterEmoji(String source) { 
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find())
            {
                source = emojiMatcher.replaceAll("*");
                return source ;
            }
        return source;
       }
       return source; 
    }
	
	// 对特殊的title进行处理
	private static String delWithSpecialTitle(String title,String url,String line) {
		String result = title;
		// 对chinanews中shipin文章的特殊处理
		if(url.contains("chinanews") && url.contains("shipin")) {
			try {
				result = line.substring(7,line.indexOf("|"));
			}catch(Exception e) {
				result = line.split("|")[0];
			}
		}
		// 对chinanews中湖北新闻的特殊处理
		if(title.contains("湖北新闻网")) {
			try {
				result = line.substring(line.indexOf("湖北新闻网")+"湖北新闻网".length(),line.indexOf(">"));
			}catch(Exception e) {
			}
		}
		return result;
	}
}
