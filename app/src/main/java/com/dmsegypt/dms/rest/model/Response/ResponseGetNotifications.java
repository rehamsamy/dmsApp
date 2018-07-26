package com.dmsegypt.dms.rest.model.Response;

import java.util.List;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.NotificationItem;
import com.google.gson.annotations.SerializedName;

public class ResponseGetNotifications{

	@SerializedName("Message")
	private Message message;

	@SerializedName("List")
	private List<NotificationItem> list;

	public Message getMessage(){
		return message;
	}

	public List<NotificationItem> getList(){
		return list;
	}

	@Override
 	public String toString(){
		return 
			"ResponseGetNotifications{" + 
			"message = '" + message + '\'' + 
			",list = '" + list + '\'' + 
			"}";
		}
}