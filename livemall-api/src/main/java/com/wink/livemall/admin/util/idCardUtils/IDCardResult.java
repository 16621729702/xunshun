package com.wink.livemall.admin.util.idCardUtils;

import java.util.Map;

public class IDCardResult {
    private Long log_id;
    private String image_status;

    private Integer direction;
    private String risk_type;
    private String edit_tool;
    private Map<String,KeyDetail> words_result;
    private Integer words_result_num;

    public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
    }

    public String getImage_status() {
        return image_status;
    }

    public void setImage_status(String image_status) {
        this.image_status = image_status;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getRisk_type() {
        return risk_type;
    }

    public void setRisk_type(String risk_type) {
        this.risk_type = risk_type;
    }

    public String getEdit_tool() {
        return edit_tool;
    }

    public void setEdit_tool(String edit_tool) {
        this.edit_tool = edit_tool;
    }

    public Map<String, KeyDetail> getWords_result() {
        return words_result;
    }

    public void setWords_result(Map<String, KeyDetail> words_result) {
        this.words_result = words_result;
    }

    public Integer getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(Integer words_result_num) {
        this.words_result_num = words_result_num;
    }

    @Override
    public String toString() {
        return "{" +
                "log_id=" + log_id +
                ", image_status='" + image_status + '\'' +
                ", direction=" + direction +
                ", risk_type='" + risk_type + '\'' +
                ", edit_tool='" + edit_tool + '\'' +
                ", words_result=" + words_result +
                ", words_result_num=" + words_result_num +
                '}';
    }
}
