package com.example.covid19app;

//java class for Covid 19 data
public class Covid19Data {
    private int imageResource;
    private String cv19_desc_total, cv19_desc_daily, cv19_total_num, cv19_daily_num;

    public Covid19Data(int imageResource, String cv19_desc_total, String cv19_total_num, String cv19_desc_daily, String cv19_daily_num) {
        this.imageResource = imageResource;
        this.cv19_desc_total = cv19_desc_total;
        this.cv19_desc_daily = cv19_desc_daily;
        this.cv19_total_num = cv19_total_num;
        this.cv19_daily_num = cv19_daily_num;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getCv19_desc_total() {
        return cv19_desc_total;
    }

    public void setCv19_desc_total(String cv19_desc_total) {
        this.cv19_desc_total = cv19_desc_total;
    }

    public String getCv19_desc_daily() {
        return cv19_desc_daily;
    }

    public void setCv19_desc_daily(String cv19_desc_daily) {
        this.cv19_desc_daily = cv19_desc_daily;
    }

    public String getCv19_total_num() {
        return cv19_total_num;
    }

    public void setCv19_total_num(String cv19_total_num) {
        this.cv19_total_num = cv19_total_num;
    }

    public String getCv19_daily_num() {
        return cv19_daily_num;
    }

    public void setCv19_daily_num(String cv19_daily_num) {
        this.cv19_daily_num = cv19_daily_num;
    }
}
