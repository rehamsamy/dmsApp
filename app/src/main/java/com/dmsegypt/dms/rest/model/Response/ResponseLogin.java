package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {

	@SerializedName("User")
	@Expose
	private User user;

	@SerializedName("Message")
	@Expose
	private Message message;

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ResponseLogin{" +
			"user = '" + user + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}