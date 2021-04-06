package com.wink.livemall.admin.util.idCardUtils.Bean;


import java.util.List;


public class DataBean {

    private String msg;
    private String conclusion;
    private List<Hits> hits;
    private int subType;
    private int conclusionType;
    private int type;
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
    public String getConclusion() {
        return conclusion;
    }

    public void setHits(List<Hits> hits) {
        this.hits = hits;
    }
    public List<Hits> getHits() {
        return hits;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }
    public int getSubType() {
        return subType;
    }

    public void setConclusionType(int conclusionType) {
        this.conclusionType = conclusionType;
    }
    public int getConclusionType() {
        return conclusionType;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

}