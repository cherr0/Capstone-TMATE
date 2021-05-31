package com.tmate.user.data;

import java.util.ArrayList;

public class MatchingDetailData {
    String situation;
    String id_num;
    ArrayList<TogetherRequest> togetherRequests;
    ArrayList<MatchingMember> matchingMembers;

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

}
