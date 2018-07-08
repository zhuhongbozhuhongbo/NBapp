package com.example.map3dtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.map3dtest.adapter.InfoWinAdapter;
import com.example.nbapp.R;

public class MainActivity extends AppCompatActivity{
    private MapView mapView = null;
    private AMap aMap;
    private MarkerOptions markerOption;
    private Marker marker;
    private InfoWinAdapter adapter;
    private LatLng latlng = new LatLng(30.666482, 104.036407);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView)findViewById(R.id.mapfirst);
        mapView.onCreate(savedInstanceState);

        init();

    }

    private void init(){
        if(aMap == null){
            aMap = mapView.getMap();
            //addMarkersToMap();
        }

        adapter = new InfoWinAdapter();
        aMap.setInfoWindowAdapter(adapter);
        addMarkerToMap(latlng ,"太湖0");
    }

    private void addMarkerToMap(LatLng latLng, String title) {
        Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_normal))
        );

       marker.showInfoWindow();
    }

    private void addMarkersToMap(){
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latlng)
                .title("标题")
                .snippet("详细信息")
                .draggable(true);
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
