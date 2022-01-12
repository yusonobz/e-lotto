package com.lottomo.main.model;

public class BetInput {

	String inputbox;
	Integer category;

	@Override
	public String toString() {
		return "BetInput [inputbox=" + inputbox + ", category=" + category + "]";
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getInputbox() {
		return inputbox;
	}

	public void setInputbox(String inputbox) {
		this.inputbox = inputbox;
	}
	
	
	
	
}
