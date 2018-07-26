package com.dmsegypt.dms.rest.model.Response;

import com.dmsegypt.dms.rest.model.BatchPharmacy;
import com.dmsegypt.dms.rest.model.Item;
import com.dmsegypt.dms.rest.model.Message;
import com.google.gson.annotations.Expose;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
<<<<<<< Updated upstream
 * Created by mahmoud on 6/12/17.
=======
 * Created by amr on 05/12/2017.
>>>>>>> Stashed changes
 */

public class ResponseBatchPharmacy {
    @SerializedName("Message")
    @Expose
    private Message message;

    @SerializedName("List")
    @Expose
    private List<BatchPharmacy> list;


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<BatchPharmacy> getList() {
        return list;
    }

    public void setList(List<BatchPharmacy> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return
                "ResponseProviderTypes{" +
                        "message = '" + message + '\'' +
                        ",list = '" + list + '\'' +
                        "}";
    }
}
