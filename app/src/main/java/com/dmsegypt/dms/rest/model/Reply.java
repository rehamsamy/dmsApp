package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 10/19/17.
 */

public class Reply {
    @SerializedName("Req_id")
    @Expose
    private String req_id;

    @SerializedName("Reply_notes")
    @Expose
    private String reply_notes;

    @SerializedName("Reply_id")
    @Expose
    private String reply_id;

    @SerializedName("Reply_flg")
    @Expose
    private String reply_flg;


    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getReply_notes() {
        return reply_notes;
    }

    public void setReply_notes(String reply_notes) {
        this.reply_notes = reply_notes;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getReply_flg() {
        return reply_flg;
    }

    public void setReply_flg(String reply_flg) {
        this.reply_flg = reply_flg;
    }
}
