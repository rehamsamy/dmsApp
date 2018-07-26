package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseRegister {

	@SerializedName("User")
	@Expose
	private User user;

	@SerializedName("Message")
	@Expose
	private Message message;

	public User getUser(){
		return user;
	}

	public Message getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ResponseRegister{" +
			"user = '" + user + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}