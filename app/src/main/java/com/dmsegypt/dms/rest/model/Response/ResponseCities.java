package com.dmsegypt.dms.rest.model.Response;

import java.util.List;

import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

public class ResponseCities{

	@SerializedName("Message")
	private Message message;

	@SerializedName("List")
	private List<City> list;

	public Message getMessage(){
		return message;
	}

	public List<City> getList(){
		return list;
	}

	@Override
 	public String toString(){
		return 
			"ResponseCities{" + 
			"message = '" + message + '\'' + 
			",list = '" + list + '\'' + 
			"}";
		}
}