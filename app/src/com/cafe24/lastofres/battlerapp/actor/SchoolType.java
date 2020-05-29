package com.cafe24.lastofres.battlerapp.actor;

public enum SchoolType {
	MILITARY(Military.class);
	
	private Class<? extends School> school;
	
	private SchoolType(Class<? extends School> school) {
		this.school = school;
	}
	
	public Class<? extends School> getSchool() {
		return school;
	}
}
