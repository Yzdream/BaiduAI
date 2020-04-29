package com.yz.data.bean;

import java.text.DecimalFormat;

public class Result {

    private String score;

    private String name;

    private BaikeInfo baike_info;

    private String root;

    private String keyword;

    public String getScore() {
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return decimalFormat.format(Double.parseDouble(score) * 100) + "%";
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name == null ? getKeyword() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaikeInfo getBaike_info() {
        return baike_info;
    }

    public void setBaike_info(BaikeInfo baike_info) {
        this.baike_info = baike_info;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getKeyword() {
        return keyword + "("+getRoot()+")";
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
