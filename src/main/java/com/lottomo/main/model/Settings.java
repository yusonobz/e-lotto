package com.lottomo.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Settings")
public class Settings {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long settingsid;
	Integer category;
	Integer voidmaxlimit;
	@Transient
	String definition;
	
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public Long getSettingsid() {
		return settingsid;
	}
	public void setSettingsid(Long settingsid) {
		this.settingsid = settingsid;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public Integer getVoidmaxlimit() {
		return voidmaxlimit;
	}
	public void setVoidmaxlimit(Integer voidmaxlimit) {
		this.voidmaxlimit = voidmaxlimit;
	}
	
	@Override
	public String toString() {
		return "Settings [settingsid=" + settingsid + ", category=" + category + ", voidmaxlimit=" + voidmaxlimit + "]";
	}
}
