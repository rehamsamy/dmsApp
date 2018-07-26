package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Runner;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 28/12/17.
 */

public class ResponseRunner {
    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<Runner> list;

    public Message getMessage() {
        return message;
    }

    public List<Runner> getList() {
        return list;
    }

    @Override
    public String toString() {
        return
                "ResponseRunner{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}
