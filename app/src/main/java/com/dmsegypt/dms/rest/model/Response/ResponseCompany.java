package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.Company;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 31/12/17.
 */

public class ResponseCompany {

    @SerializedName("Message")
    private Message message;

    @SerializedName("List")
    private List<Company> list;

    public Message getMessage(){
        return message;
    }

    public List<Company> getList(){
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
