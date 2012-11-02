package com.glue.client.android.location;

import android.os.Bundle;
import android.view.Menu;

import com.glue.client.android.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class LocationPickerMapActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker_map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_location_picker_map, menu);
        return true;
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
