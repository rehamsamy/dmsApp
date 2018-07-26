package com.dmsegypt.dms.rest.model.Response;

import java.util.List;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Provider;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseProviders {

	@SerializedName("Message")
	@Expose
	private Message message;

	@SerializedName("List")
	@Expose
	private List<Provider> list;

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	public void setList(List<Provider> list){
		this.list = list;
	}

	public List<Provider> getList(){
		return list;
	}

	@Override
 	public String toString(){
		return 
			"ResponseProviders{" +
			"message = '" + message + '\'' + 
			",list = '" + list + '\'' + 
			"}";
		}
}