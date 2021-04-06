package com.wink.livemall.admin.util.idCardUtils.Bean;

import java.util.List;

public class JsonRootBean {

    private String conclusion;
    private long log_id;
    private List<DataBean> data;
    private int conclusionType;
    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
    public String getConclusion() {
        return conclusion;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }
    public long getLog_id() {
        return log_id;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
    public List<DataBean> getData() {
        return data;
    }

    public void setConclusionType(int conclusionType) {
        this.conclusionType = conclusionType;
    }
    public int getConclusionType() {
        return conclusionType;
    }

}