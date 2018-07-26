package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.ProviderSimpleItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseProvidersSimple {

	@SerializedName("Message")
	@Expose
	private Message message;

	@SerializedName("List")
	@Expose
	private List<ProviderSimpleItem> list;

	public void setMessage(Message message){
		this.message = message;
	}

	public Message getMessage(){
		return message;
	}

	public void setList(List<ProviderSimpleItem> list){
		this.list = list;
	}

	public List<ProviderSimpleItem> getList(){
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