package com.dmsegypt.dms.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahmoud on 5/29/2018.
 */

public class Agent {

    @SerializedName("AgentName")
    private String agentName;

    @SerializedName("AgentCode")
    private String agentCode;


    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }



}
