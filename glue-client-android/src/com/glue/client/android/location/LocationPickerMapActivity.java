package com.glue.client.android.location;

import android.os.Bundle;
import android.view.Menu;

import com.glue.client.android.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class LocationPickerMapActivity extends MapActivity {

	private static final int ZOOM_LEVEL = 17;
	private MyLocationOverlay myLocation = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_picker_map);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		myLocation = new MyLocationOverlay(getApplicationContext(), mapView);
		mapView.getOverlays().add(myLocation);
		myLocation.enableMyLocation();

		final MapController mc = mapView.getController();
		myLocation.runOnFirstFix(new Runnable() {
			public void run() {
				mc.animateTo(myLocation.getMyLocation());
				mc.setZoom(ZOOM_LEVEL);
			}
		});

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
