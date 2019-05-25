package com.PowerZZJ.Utils;

import java.io.*;
import java.util.*;

import com.PowerZZJ.SpiderFinal.JobBean;

/**实现
 * 1。将JobBean容器存入本地
 * 2.从本地文件读入文件为JobBean容器(有筛选)
 * @author PowerZZJ
 *
 */
public class JobBeanUtils {
	
	/**保存JobBean到本地功能实现
	 * @param job
	 */
	public static void saveJobBean(JobBean job) {
		try(BufferedWriter bw =
				new BufferedWriter(
						new FileWriter("JobInfo.txt",true))){
			String jobInfo = job.toString();
			bw.write(jobInfo);
			bw.newLine();
			bw.flush();
		}catch(Exception e) {
			System.out.println("保存JobBean失败");
			e.printStackTrace();
		}
	}
	
	/**保存JobBean容器到本地功能实现
	 * @param jobBeanList JobBean容器
	 */
	public static void saveJobBeanList(List<JobBean> jobBeanList) {
		System.out.println("正在备份容器到本地");
		for(JobBean jobBean : jobBeanList) {
			saveJobBean(jobBean);
		}
		System.out.println("备份完成,一共"+jobBeanList.size()+"条信息");
	}
	
	/**从本地文件读入文件为JobBean容器(有筛选)
	 * @return jobBean容器
	 */
	public static List<JobBean> loadJobBeanList(){
		List<JobBean> jobBeanList = new ArrayList<>();
		try(BufferedReader br = 
				new BufferedReader(
						new FileReader("JobInfo.txt"))){
			String str = null;
			while((str=br.readLine())!=null) {
				//筛选，有些公司名字带有","不规范，直接跳过
				try {
					String[] datas = str.split(","); 
					String jobName = datas[0].substring(8);
					String company = datas[1].substring(9);
					String address = datas[2].substring(9);
					String salary = datas[3].substring(8);
					String date = datas[4].substring(6);
					String jobURL = datas[5].substring(8);
					//筛选，全部都不为空,工资是个区间,URL以https开头，才建立JobBean
					if (jobName.equals("") || company.equals("") || address.equals("") || salary.equals("")
							|| !(salary.contains("-"))|| date.equals("") || !(jobURL.startsWith("http")))
						continue;
					JobBean jobBean = new JobBean(jobName, company, address, salary, date, jobURL);
					//放入容器
					jobBeanList.add(jobBean);
				}catch(Exception e) {
					System.out.println("本地读取筛选：有问题需要跳过的数据行："+str);
					continue;
				}
			}
			System.out.println("读取完成,一共读取"+jobBeanList.size()+"条信息");
			return jobBeanList;
		}catch(Exception e) {
			System.out.println("读取JobBean失败");
			e.printStackTrace();
		}
		return jobBeanList;
	}
}
