package com.PowerZZJ.Analyze;

import static tech.tablesaw.aggregate.AggregateFunctions.max;
import static tech.tablesaw.aggregate.AggregateFunctions.mean;
import static tech.tablesaw.aggregate.AggregateFunctions.median;
import static tech.tablesaw.aggregate.AggregateFunctions.min;
import static tech.tablesaw.aggregate.AggregateFunctions.stdDev;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.PowerZZJ.SpiderFinal.ConnectMySQL;
import com.PowerZZJ.SpiderFinal.JobBean;
import com.PowerZZJ.Utils.DBUtils;

import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;


public class Analyze {
	public static void main(String[] args) {
		List<JobBean> jobBeanList = new ArrayList<>();
		
		ConnectMySQL cm = new ConnectMySQL();
		Connection conn = cm.getConn();
		jobBeanList = DBUtils.select(conn);
		
		BaseFilter bf = new BaseFilter(jobBeanList);
		bf.filterDate();
		bf.filterJobName("数据");
		int nums = jobBeanList.size();
		
		//按照工资排序
		String[] jobNames = new String[nums];
		String[] companys = new String[nums];
		String[] addresss = new String[nums];
		double[] salarys = new double[nums];
		String[] jobURLs = new String[nums];
		for(int i=0; i<nums; i++) {
			JobBean j = jobBeanList.get(i);
			String jobName = j.getJobName();
			String company = j.getCompany();
			//地址提出区名字
			String address;
			if(j.getAddress().contains("-")) {
				address = j.getAddress().split("-")[1];
			}else{
				address = j.getAddress();
			}
			
			//工资做处理
			String sSalary = j.getSalary();
			double dSalary;
			if(sSalary.contains("万/月")) {
				dSalary = Double.valueOf(sSalary.split("-")[0]);
			}else if(sSalary.contains("千/月")) {
				dSalary = Double.valueOf(sSalary.split("-")[0])/10;
				dSalary = (double) Math.round(dSalary * 100) / 100;
			}else if(sSalary.contains("万/年")) {
				dSalary = Double.valueOf(sSalary.split("-")[0])/12;
				dSalary = (double) Math.round(dSalary * 100) / 100;
			}else {
				dSalary = 0;
				System.out.println("工资转换失败");
				continue;
			}
			String jobURL = j.getJobURL();
			
			jobNames[i] = jobName;
			companys[i] = company;
			addresss[i] = address;
			salarys[i] = dSalary;
			jobURLs[i] = jobURL;
		}
		
		Table jobInfo = Table.create("Job Info")
				.addColumns(
					StringColumn.create("jobName", jobNames),
					StringColumn.create("company", companys),
					StringColumn.create("address", addresss),
					DoubleColumn.create("salary", salarys),
					StringColumn.create("jobURL", jobURLs)
						);
		
		List<Table> addressJobInfo = new ArrayList<>();
		//按照地区划分
		Table ShanghaiJobInfo = chooseByAddress(jobInfo, "上海");
		Table jingAnJobInfo = chooseByAddress(jobInfo, "静安区");
		Table puDongJobInfo = chooseByAddress(jobInfo, "浦东新区");
		Table changNingJobInfo = chooseByAddress(jobInfo, "长宁区");
		Table minHangJobInfo = chooseByAddress(jobInfo, "闵行区");
		Table xuHuiJobInfo = chooseByAddress(jobInfo, "徐汇区");
		//人数太少
//		Table songJiangJobInfo = chooseByAddress(jobInfo, "松江区");
//		Table yangPuJobInfo = chooseByAddress(jobInfo, "杨浦区");
//		Table hongKouJobInfo = chooseByAddress(jobInfo, "虹口区");
//		Table OtherInfo = chooseByAddress(jobInfo, "异地招聘");
//		Table puTuoJobInfo = chooseByAddress(jobInfo, "普陀区");
		
		addressJobInfo.add(jobInfo);
		//上海地区招聘
		addressJobInfo.add(ShanghaiJobInfo);
		addressJobInfo.add(jingAnJobInfo);
		addressJobInfo.add(puDongJobInfo);
		addressJobInfo.add(changNingJobInfo);
		addressJobInfo.add(minHangJobInfo);
		addressJobInfo.add(xuHuiJobInfo);
//		addressJobInfo.add(songJiangJobInfo);
//		addressJobInfo.add(yangPuJobInfo);
//		addressJobInfo.add(hongKouJobInfo);
//		addressJobInfo.add(puTuoJobInfo);
//		addressJobInfo.add(OtherInfo);

		for(Table t: addressJobInfo) {
			System.out.println(salaryInfo(t));
		}
		
		for(Table t: addressJobInfo) {
			System.out.println(sortBySalary(t).first(10));
		}
	}
	
	//工资平均值,最小,最大
	public static Table salaryInfo(Table t) {		
		return t.summarize("salary",mean,stdDev,median,max,min).apply();
	}
	
	//salary进行降序
	public static Table sortBySalary(Table t) {
		return t.sortDescendingOn("salary");
	}
	
	//选择地区
	public static Table chooseByAddress(Table t, String address) {
		Table t2 = Table.create(address)
				.addColumns(
					StringColumn.create("jobName"),
					StringColumn.create("company"),
					StringColumn.create("address"),
					DoubleColumn.create("salary"),
					StringColumn.create("jobURL"));
		for(Row r: t) {
			if(r.getString(2).equals(address)) {
				t2.addRow(r);
			}
		}
		return t2;
	}

}
