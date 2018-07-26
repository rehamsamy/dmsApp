package com.dmsegypt.dms.rest.model;

import android.net.Uri;

/**
 * Created by amr on 10/12/2017.
 */

public class SingleRequestImage {
    private Uri local_path;
    private String online_path;
    private int id;
    private int state=1;

    public Uri getLocal_path() {
        return local_path;
    }

    public void setLocal_path(Uri local_path) {
        this.local_path = local_path;
    }

    public String getOnline_path() {
        return online_path;
    }

    public void setOnline_path(String online_path) {
        this.online_path = online_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
