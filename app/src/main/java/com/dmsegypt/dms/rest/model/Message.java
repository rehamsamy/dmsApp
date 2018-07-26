package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message{

	@SerializedName("Details")
	@Expose
	private String details;

	@SerializedName("Userid")
	@Expose
	private String userid;

	@SerializedName("Code")
	@Expose
	private int code;

	public String getDetails(){
		return details;
	}

	public int getCode(){
		return code;
	}

	public String getUserid() {
		return userid;
	}

	@Override
 	public String toString(){
		return 
			"Message{" + 
			"details = '" + details + '\'' + 
			",code = '" + code + '\'' + 
			",Userid='"+userid+ '\''+
			"}";
		}
}