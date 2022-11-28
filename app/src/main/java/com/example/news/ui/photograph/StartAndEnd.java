package com.example.news.ui.photograph;

import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.NaviPoi;

import java.io.Serializable;

public class StartAndEnd implements Serializable {


    private String startname;
    private double startlat;
    private double startlon;
    private String endname;
    private double endlat;
    private double endlon;
    private String endPoiID;
    public StartAndEnd(String startname, double startlat, double startlon, String endname,double endlat,double endlon,String endPoiID){
        this.startname = startname;
        this.startlat = startlat;
        this.startlon = startlon;
        this.endname = endname;
        this.endlat=endlat;
        this.endlon=endlon;
        this.endPoiID=endPoiID;
    }
    public String getStartName() {
        return startname;
    }
    public double getStartLat() {
        return startlat;
    }
    public double getStartLon() {
        return startlon;
    }
    public String getEndName() {
        return endname;
    }
    public double getEndLat() {
        return endlat;
    }
    public double getEndLon() {
        return endlon;
    }
    public String getEndPoiID(){
        return endPoiID;
    }
}
