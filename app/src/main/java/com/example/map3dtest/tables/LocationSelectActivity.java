package com.example.map3dtest.tables;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;


import com.example.nbapp.R;
import com.example.map3dtest.Utils.Position2ID;
import com.example.map3dtest.transformdata.TransformData;

import chihane.jdaddressselector.AddressSelector;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

public class LocationSelectActivity extends AppCompatActivity implements OnAddressSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.project_select_framelayout);

        AddressSelector selector = new AddressSelector(this);
        selector.setOnAddressSelectedListener(this);
//        selector.setAddressProvider(new TestAddressProvider());

        assert frameLayout != null;
        frameLayout.addView(selector.getView());

    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String s =
                (province == null ? "" : province.name);
        /*      String s =
                (province == null ? "" : province.name) +
                        (city == null ? "" : "\n" + city.name) +
                        (county == null ? "" : "\n" + county.name) +
                        (street == null ? "" : "\n" + street.name);*/

        //T.showShort(this, s);
        Intent intent = new Intent(LocationSelectActivity.this, ProjectSheetActivity.class);
        Bundle bundle = new Bundle();
        TransformData transTrd = new TransformData("", "", Position2ID.province2ID(s));
        bundle.putParcelable("positionSec", transTrd);
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
