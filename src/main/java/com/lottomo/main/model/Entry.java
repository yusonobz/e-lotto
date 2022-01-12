package com.lottomo.main.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Entries")
public class Entry implements Serializable {
	
	private static final long serialVersionUID = -1120558450553258911L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long entryid;
	String mainagent;
	String subagent;
	String playername;
	Integer num1;
	Integer num2;
	Integer num3;
	Integer amount;
	Integer freebet;
	Integer returnbet;
	LocalDateTime datelastmodified;
	Integer category;
	String status;
	@Transient
	String orderedNums;
	
	public Entry() {}

	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderedNums() {
		return orderedNums;
	}

	public void setOrderedNums(String orderedNums) {
		this.orderedNums = orderedNums;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public Long getEntryid() {
		return entryid;
	}
	public void setEntryid(Long entryid) {
		this.entryid = entryid;
	}
	public String getMainagent() {
		return mainagent;
	}
	public void setMainagent(String mainagent) {
		this.mainagent = mainagent;
	}
	public String getSubagent() {
		return subagent;
	}
	public void setSubagent(String subagent) {
		this.subagent = subagent;
	}
	public String getPlayername() {
		return playername;
	}
	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public LocalDateTime getDatelastmodified() {
		return datelastmodified;
	}
	public void setDatelastmodified(LocalDateTime datelastmodified) {
		this.datelastmodified = datelastmodified;
	}
	public Integer getNum1() {
		return num1;
	}
	public void setNum1(Integer num1) {
		this.num1 = num1;
	}
	public Integer getNum2() {
		return num2;
	}
	public void setNum2(Integer num2) {
		this.num2 = num2;
	}
	public Integer getNum3() {
		return num3;
	}
	public void setNum3(Integer num3) {
		this.num3 = num3;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getFreebet() {
		return freebet;
	}
	public void setFreebet(Integer freebet) {
		this.freebet = freebet;
	}
	public Integer getReturnbet() {
		return returnbet;
	}
	public void setReturnbet(Integer returnbet) {
		this.returnbet = returnbet;
	}
	@Override
	public String toString() {
		return "Entry [entryid=" + entryid + ", mainagent=" + mainagent + ", subagent=" + subagent + ", playername="
				+ playername + ", num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + ", amount=" + amount + ", freebet=" + freebet +
				", returnbet=" + returnbet + ", datelastmodified=" + datelastmodified + "]";
	}
	
	
}
