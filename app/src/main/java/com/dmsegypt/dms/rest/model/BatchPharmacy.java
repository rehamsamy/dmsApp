package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 6/12/17.
=======
import com.google.gson.annotations.SerializedName;

/**
 * Created by amr on 05/12/2017.
>>>>>>> Stashed changes
 */

public class BatchPharmacy {
    @SerializedName("Prv_no")
    @Expose
    private String id;

    @SerializedName("Prv_name")
    @Expose
    private String name;

    public BatchPharmacy(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return getName() + ":" + id;

    }
}