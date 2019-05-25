package com.PowerZZJ.SpiderFinal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.PowerZZJ.Utils.DBUtils;
import com.PowerZZJ.Utils.JobBeanUtils;


public class TestMain {
	public static void main(String[] args) {
		List<JobBean> jobBeanList = new ArrayList<>();
		//大数据+上海
		String strURL = "https://search.51job.com/list/020000,000000,0000,00,9,99,%25E5%25A4%25A7%25E6%2595%25B0%25E6%258D%25AE,2,1.html?lang=c&stype=1&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";		
		
//		//Java+上海
//		String strURL = "https://search.51job.com/list/020000,000000,0000,00,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";		
		//所有功能测试
		//爬取的对象
		Spider jobSpider = new Spider(strURL);
		jobSpider.spider();
		//爬取完的JobBeanList
		jobBeanList = jobSpider.getJobBeanList();
		
		//调用JobBean工具类保存JobBeanList到本地
		JobBeanUtils.saveJobBeanList(jobBeanList);
	
		//调用JobBean工具类从本地筛选并读取,得到JobBeanList
		jobBeanList = JobBeanUtils.loadJobBeanList();
	
		//连接数据库,并获取连接
		ConnectMySQL cm = new ConnectMySQL();
		Connection conn = cm.getConn();
		
		//调用数据库工具类将JobBean容器存入数据库
		DBUtils.insert(conn, jobBeanList);
		
//		//调用数据库工具类查询数据库信息，并返回一个JobBeanList
//		jobBeanList = DBUtils.select(conn);
//		
//		for(JobBean j: jobBeanList) {
//			System.out.println(j);
//		}
	}
}
