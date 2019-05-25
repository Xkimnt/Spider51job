package com.PowerZZJ.SpiderFinal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**爬取网页信息
 * @author PowerZZJ
 *
 */
public class Spider {
	//记录爬到第几页
	private static int pageCount = 1;
	
	private String strURL;
	private String nextPageURL;
	private Document document;//网页全部信息
	private List<JobBean> jobBeanList;
	
	public Spider(String strURL) {
		this.strURL = strURL;
		nextPageURL = strURL;//下一页URL初始化为当前，方便遍历
		jobBeanList = new ArrayList<JobBean>();
		
	}
	
	/**获取网页全部信息
	 * @param 网址
	 * @return 网页全部信息
	 */
	public Document getDom(String strURL) {
		try {
			URL url = new URL(strURL);
			//解析，并设置超时
			document = Jsoup.parse(url, 4000);
			return document;
		}catch(Exception e) {
			System.out.println("getDom失败");
			e.printStackTrace();
		}
		return null;
	}
	

	/**筛选当前网页信息,转成JobBean对象，存入容器
	 * @param document 网页全部信息
	 */
	public void getPageInfo(Document document) {
		//通过CSS选择器用#resultList .el获取el标签信息
		Elements elements = document.select("#resultList .el");
		//总体信息删去
		elements.remove(0);
		//筛选信息
		for(Element element: elements) {
			Elements elementsSpan = element.select("span");
			String jobURL = elementsSpan.select("a").attr("href");
			String jobName = elementsSpan.get(0).select("a").attr("title");
			String company = elementsSpan.get(1).select("a").attr("title");
			String address = elementsSpan.get(2).text();
			String salary = elementsSpan.get(3).text();
			String date = elementsSpan.get(4).text();
			//建立JobBean对象
			JobBean jobBean = new JobBean(jobName, company, address, salary, date, jobURL);
			//放入容器
			jobBeanList.add(jobBean);
		}
	}
	
	/**获取下一页的URL
	 * @param document 网页全部信息
	 * @return 有,则返回URL
	 */
	public String getNextPageURL(Document document) {
		try {
			Elements elements = document.select(".bk");
			//第二个bk才是下一页
			Element element = elements.get(1);
			nextPageURL = element.select("a").attr("href");
			if(nextPageURL != null) {
				System.out.println("---------"+(pageCount++)+"--------");
				return nextPageURL;
			}
		}catch(Exception e) {
			System.out.println("获取下一页URL失败");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**开始爬取
	 * 
	 */
	public void spider() {
		while(!nextPageURL.equals("")) {
			//获取全部信息
			document = getDom(nextPageURL);
			//把相关信息加入容器
			getPageInfo(document);
			//查找下一页的URL
			nextPageURL = getNextPageURL(document);
		}
	}
	
	//获取JobBean容器
	public List<JobBean> getJobBeanList() {
		return jobBeanList;
	}
}
