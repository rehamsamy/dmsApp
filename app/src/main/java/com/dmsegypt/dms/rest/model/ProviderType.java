package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderType {
	public ProviderType() {
	}

	public ProviderType(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@SerializedName("Id")
	@Expose
	private String id;

	@SerializedName("Name")
	@Expose
	private String name;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return getName();
//		return
//			"ProviderType{" +
//			"id = '" + id + '\'' +
//			",name = '" + name + '\'' +
//			"}";
		}
}