package com.springcamel.demo.router;

public class Employee {

	private Long id;
	private String name;
	private String org;
	private Double sal;
	
	public Employee() {
		super();
	}

	public Employee(Long id, String name, String org, Double sal) {
		super();
		this.id = id;
		this.name = name;
		this.org = org;
		this.sal = sal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public Double getSal() {
		return sal;
	}

	public void setSal(Double sal) {
		this.sal = sal;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", org=" + org + ", sal=" + sal + "]";
	}

}
