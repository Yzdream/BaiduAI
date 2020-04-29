package com.yz.data.bean;

import java.util.List;

public class ResultData extends BaiduError {

    private String log_id;

    private List<Result> result;

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
}
