package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Account;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahmoud on 2/12/2018.
 */

public class ResponseUserAccount {
    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<Account> list;

    public Message getMessage() {
        return message;
    }

    public List<Account> getList() {
        return list;
    }

    @Override
    public String toString() {
        return
                "ResponseAreas{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}
