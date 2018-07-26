package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class NotificationItem {

	@SerializedName("NotificationDetails")
	private String notificationDetails;

	@SerializedName("NotificationDate")
	private String notificationDate;

	public String getNotificationDetails(){
		return notificationDetails;
	}

	public String getNotificationDate(){
		return notificationDate;
	}

	@Override
 	public String toString(){
		return 
			"NotificationItem{" +
			"notificationDetails = '" + notificationDetails + '\'' + 
			",notificationDate = '" + notificationDate + '\'' + 
			"}";
		}
}