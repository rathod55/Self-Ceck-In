package com.abhirathore.checked_in;

public class SQLDataModel {
    private String id,hotel_n,cust_name,date_v,time_v,days_v,price_v,status_v;

    public SQLDataModel(String id, String hotel_n, String cust_name, String date_v, String time_v, String days_v, String price_v, String status_v) {
        this.id = id;
        this.hotel_n = hotel_n;
        this.cust_name = cust_name;
        this.date_v = date_v;
        this.time_v = time_v;
        this.days_v = days_v;
        this.price_v = price_v;
        this.status_v = status_v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHotel_n() {
        return hotel_n;
    }

    public void setHotel_n(String hotel_n) {
        this.hotel_n = hotel_n;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getDate_v() {
        return date_v;
    }

    public void setDate_v(String date_v) {
        this.date_v = date_v;
    }

    public String getTime_v() {
        return time_v;
    }

    public void setTime_v(String time_v) {
        this.time_v = time_v;
    }

    public String getDays_v() {
        return days_v;
    }

    public void setDays_v(String days_v) {
        this.days_v = days_v;
    }

    public String getPrice_v() {
        return price_v;
    }

    public void setPrice_v(String price_v) {
        this.price_v = price_v;
    }

    public String getStatus_v() {
        return status_v;
    }

    public void setStatus_v(String status_v) {
        this.status_v = status_v;
    }
}
