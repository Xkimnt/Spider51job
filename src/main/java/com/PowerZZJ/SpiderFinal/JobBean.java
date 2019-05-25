package com.PowerZZJ.SpiderFinal;

public class JobBean {
	private String jobName;
	private String company;
	private String address;
	private String salary;
	private String date;
	private String jobURL;
	
	public JobBean(String jobName, String company, String address, String salary, String date, String jobURL) {
		super();
		this.jobName = jobName;
		this.company = company;
		this.address = address;
		this.salary = salary;
		this.date = date;
		this.jobURL = jobURL;
	}

	@Override
	public String toString() {
		return "jobName=" + jobName + ", company=" + company + ", address=" + address + ", salary=" + salary
				+ ", date=" + date + ", jobURL=" + jobURL;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getJobURL() {
		return jobURL;
	}

	public void setJobURL(String jobURL) {
		this.jobURL = jobURL;
	}
	
	
	
}
