package com.PowerZZJ.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.PowerZZJ.SpiderFinal.JobBean;


public class DBUtils {
	
	/**将JobBean容器存入数据库（有筛选）
	 * @param conn 数据库的连接
	 * @param jobBeanList jobBean容器
	 */
	public static void insert(Connection conn, List<JobBean> jobBeanList) {
		System.out.println("正在插入数据");
		PreparedStatement ps;
		for(JobBean j: jobBeanList) {
			//命令生成
			String command = String.format("insert into jobInfo values('%s','%s','%s','%s','%s','%s')",
					j.getJobName(),
					j.getCompany(),
					j.getAddress(),
					j.getSalary(),
					j.getDate(),
					j.getJobURL());
			
			try {
				ps = conn.prepareStatement(command);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("存入数据库筛选有误信息:"+j.getJobName());
			}
		}
		System.out.println("插入数据完成");

	}
	
	/**将JobBean容器，取出
	 * @param conn 数据库的连接
	 * @return jobBean容器
	 */
	public static List<JobBean> select(Connection conn){
		PreparedStatement ps;
		ResultSet rs;
		List<JobBean> jobBeanList  = new ArrayList<JobBean>();

		String command = "select * from jobInfo";
		try {
			ps = conn.prepareStatement(command);
			rs = ps.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			while(rs.next()) {
				JobBean jobBean = new JobBean(rs.getString(1), 
							rs.getString(2), 
							rs.getString(3), 
							rs.getString(4),
							rs.getString(5),
							rs.getString(6));

				jobBeanList.add(jobBean);
			}
			return jobBeanList;
		} catch (Exception e) {
			System.out.println("数据库查询失败");
		}
		return null;
	}
}
