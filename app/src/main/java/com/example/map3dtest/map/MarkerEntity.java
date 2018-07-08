package com.example.map3dtest.map;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

/**
 * Created by 朱宏博 on 2018/7/4.
 */

public class MarkerEntity {
    public MarkerEntity(LatLng latLng, String title) {
        this.latLng = latLng;
        this.title = title;
    }

    private String title;
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

}
