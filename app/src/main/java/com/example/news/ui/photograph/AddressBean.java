package com.example.news.ui.photograph;

public class AddressBean {
    private double longitude;//经度
    private double latitude;//纬度
    private String title;//信息标题
    private String text;//信息内容
    private String PoiID;
    public AddressBean(double lon, double lat, String title, String text,String PoiID){
        this.longitude = lon;
        this.latitude = lat;
        this.title = title;
        this.text = text;
        this.PoiID=PoiID;
    }
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public String getTitle() {
        return title;
    }
    public String getText(){
        return text;
    }
    public String getPoiID(){
        return PoiID;
    }
}
