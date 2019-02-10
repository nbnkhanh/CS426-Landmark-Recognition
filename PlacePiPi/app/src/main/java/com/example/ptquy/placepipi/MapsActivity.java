package com.example.ptquy.placepipi;

import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ramotion.circlemenu.CircleMenuView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


	private int x;
	private int y;
	private GoogleMap mMap;
	private CircleMenuView circleMenuView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps2);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					// remove the listener
					// ! before Jelly Bean:
					mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

					// Add a marker in Sydney and move the camera
					LatLng sydney = new LatLng(-34, 151);
					Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

					Projection projection = mMap.getProjection();

					LatLng markerLocation = marker.getPosition();

					Point screenPosition = projection.toScreenLocation(markerLocation);

					RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layoutForCirclemenu);
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50,50);
					params.leftMargin = (screenPosition.x - 25);
					params.topMargin = screenPosition.y - 25;

					circleMenuView = (CircleMenuView) findViewById(R.id.circle_menu);
					circleMenuView.setX(screenPosition.x - 25);
					circleMenuView.setY(screenPosition.y - 25);

					ImageView img = new ImageView(MapsActivity.this);
					img.setImageResource(R.drawable.caticon);
					relativeLayout.addView(img, params);
				}
			});
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x=(int)event.getX();
		int y=(int)event.getY();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
		}
		Toast.makeText(this,String.valueOf(x)+' '+String.valueOf(y),Toast.LENGTH_LONG).show();
		//circleMenuView = (CircleMenuView) findViewById(R.id.circle_menu);
		//circleMenuView.setX(x);
		//circleMenuView.setY(y);
		return false;
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
	}
}
