package com.example.map3dtest.map;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

/**
 * Created by 朱宏博 on 2018/7/7.
 */

public class MarkerManager {
    public MarkerManager(LatLng latLng, String title, Marker marker) {
        this.latLng = latLng;
        this.title = title;
        this.marker = marker;
    }

    private Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
