package com.lottomo.main.model;

import java.util.List;

import javax.persistence.Transient;

public class DeleteButton {
	
	
	Long btnid;
	public Long getBtnid() {
		return btnid;
	}

	public void setBtnid(Long btnid) {
		this.btnid = btnid;
	}

	@Transient
//	List<Long> deleteList;
	List<String> deleteList;
	
	public List<String> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<String> deleteList) {
		this.deleteList = deleteList;
	}

	@Transient
    List<Long> longList;
//	public List<Long> getDeleteList() {
//		return deleteList;
//	}
//
//	public void setDeleteList(List<Long> deleteList) {
//		this.deleteList = deleteList;
//	}

	public List<Long> getLongList() {
		return longList;
	}

	public void setLongList(List<Long> longList) {
		this.longList = longList;
	}

//	public List<Entry> getDeleteList() {
//		return deleteList;
//	}
//
//	public void setDeleteList(List<Entry> deleteList) {
//		this.deleteList = deleteList;
//	}

	
}
