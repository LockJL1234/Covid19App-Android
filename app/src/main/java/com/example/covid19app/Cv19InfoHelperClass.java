package com.example.covid19app;

public class Cv19InfoHelperClass {

    private String info_title, info_desc;
    private int infoImg;

    public Cv19InfoHelperClass(String info_title, String info_desc, int infoImg) {
        this.info_title = info_title;
        this.info_desc = info_desc;
        this.infoImg = infoImg;
    }

    public String getInfo_title() {
        return info_title;
    }

    public void setInfo_title(String info_title) {
        this.info_title = info_title;
    }

    public String getInfo_desc() {
        return info_desc;
    }

    public void setInfo_desc(String info_desc) {
        this.info_desc = info_desc;
    }

    public int getInfoImg() {
        return infoImg;
    }

    public void setInfoImg(int infoImg) {
        this.infoImg = infoImg;
    }
}
