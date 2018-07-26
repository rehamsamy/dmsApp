package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseLookups {

    @SerializedName("Message")
    private Message message;

    @SerializedName("ProviderTypes")
    private List<ProviderType> providerTypes;

    @SerializedName("Cities")
    private List<City> cities;

    public Message getMessage() {
        return message;
    }

    public List<ProviderType> getProviderTypes() {
        return providerTypes;
    }

    public List<City> getCities() {
        return cities;
    }

    @Override
    public String toString() {
        return
                "ResponseLookups{" +
                        "message = '" + message + '\'' +
                        ",providerTypes = '" + providerTypes + '\'' +
                        ",cities = '" + cities + '\'' +
                        "}";
    }
}