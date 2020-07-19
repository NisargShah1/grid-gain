package com.sample.gridgain;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Employee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8143055346199229315L;
	
	@QuerySqlField(index = true)
	private Integer employeeId;
	@QuerySqlField(index = true)
	private String employeeName;
	@QuerySqlField(index = true)
	private String email;
	public Integer getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Employee() {
	}
	public Employee(Integer employeeId, String employeeName, String email) {
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.email = email;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
		result = prime * result + ((employeeName == null) ? 0 : employeeName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		if (employeeName == null) {
			if (other.employeeName != null)
				return false;
		} else if (!employeeName.equals(other.employeeName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeName=" + employeeName + ", email=" + email + "]";
	}
	
	
}
