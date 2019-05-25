package com.PowerZZJ.Analyze;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.PowerZZJ.SpiderFinal.JobBean;

public class BaseFilter {
	private List<JobBean> jobBeanList;
	//foreach遍历不可以remove，Iterator有锁
	//用新的保存要删除的，然后removeAll
	private List<JobBean> removeList;
	
	public BaseFilter(List<JobBean> jobBeanList) {
		this.jobBeanList = new ArrayList<JobBean>();
		removeList =  new ArrayList<JobBean>();
		//引用同一个对象，getJobBeanList有没有都一样
		this.jobBeanList = jobBeanList;
		printNum();
	}
	
	//打印JobBean容器中的数量
	public void printNum() {
		System.out.println("现在一共"+jobBeanList.size()+"条数据");
	}
	

	/**筛选职位名字
	 * @param containJobName 关键字保留
	 */
	public void filterJobName(String containJobName) {
		for(JobBean j: jobBeanList) {
			if(!j.getJobName().contains(containJobName)) {
				removeList.add(j);
			}
		}
		jobBeanList.removeAll(removeList);
		removeList.clear();
		printNum();
	}
	
	/**筛选日期,要当天发布的
	 * @param
	 */
	public void filterDate() {
		Calendar now=Calendar.getInstance();
		int nowMonth = now.get(Calendar.MONTH)+1;
		int nowDay = now.get(Calendar.DATE);
		
		for(JobBean j: jobBeanList) {
			String[] date = j.getDate().split("-");
			int jobMonth = Integer.valueOf(date[0]);
			int jobDay = Integer.valueOf(date[1]);
			if(!(jobMonth==nowMonth && jobDay==nowDay)) {
				removeList.add(j);
			}
		}
		jobBeanList.removeAll(removeList);
		removeList.clear();
		printNum();
	}
	
	public List<JobBean> getJobBeanList(){
		return jobBeanList;
	}
	
}
