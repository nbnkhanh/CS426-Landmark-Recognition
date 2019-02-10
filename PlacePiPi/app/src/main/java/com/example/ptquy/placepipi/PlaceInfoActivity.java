package com.example.ptquy.placepipi;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlaceInfoActivity extends AppCompatActivity implements OnMapReadyCallback{

	TabLayout tabLayout;
	ViewPager pager;
	PlaceData place;
	StringBuilder StringJSON = new StringBuilder();
	private String API_KEY = "AIzaSyB4cSAEQi0uINGwRHokQj7qJhQyG0dE0-g";
	private String PlaceDetailURL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
	private CollapsingToolbarLayout collapsingToolbarLayout;
	GoogleMap mMap;
	GeoDataClient mGeoDataClient;
	String placeID;
	ArrayList<String> arrayListFavID;
	private ImageButton shareFB;
	private ShareDialog shareDialog;
	private CallbackManager callbackManager;
	private StringBuilder website = new StringBuilder();
	private StringBuilder name = new StringBuilder();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_info);
		collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		collapsingToolbarLayout.setTitle(" ");
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle("ffsf");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		PlaceInfoActivity.this.getSupportActionBar().show();

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		mGeoDataClient = Places.getGeoDataClient(this, null);

		shareFB = (ImageButton) findViewById(R.id.share_fb_button);
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);

		PlaceSQLite db = new PlaceSQLite(this, PlaceSQLite.DB_name, null, 1);
		arrayListFavID = db.getAllFavPlaceID();
		getDataFromAPI();

	}

	public void createViewInfo() {
		pager = (ViewPager) findViewById(R.id.view_pager_place_info);
		tabLayout = (TabLayout) findViewById(R.id.tab_layout_place_info);
		FragmentManager manager = getSupportFragmentManager();
		PagerAdapter adapter = new PagerAdapter(manager, StringJSON.toString(), arrayListFavID);
		pager.setAdapter(adapter);
		tabLayout.setupWithViewPager(pager);
		pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.setTabsFromPagerAdapter(adapter);
	}

	public void getDataFromAPI()
	{
		Intent intent = getIntent();
		String placeid = intent.getStringExtra("placeID");
		placeID = new String(placeid);
		Log.d("url", PlaceDetailURL + placeid + "&language=en" + "&key=" + API_KEY);
		new ParseJSON().execute(PlaceDetailURL + placeid + "&language=en" + "&key=" + API_KEY);
		Log.d("TAA", StringJSON.toString());

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		moveCameraToThisLocation();
	}

	public void shareFBButtonClicked(View view) {
		if(website.toString().equals("None")){
			Snackbar.make(view, "The link is not available", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
		}
		else{
			ShareLinkContent linkContent=new ShareLinkContent.Builder().
					setContentUrl(Uri.parse(website.toString())).setShareHashtag(new ShareHashtag.Builder()
					.setHashtag("#" + name.toString().trim()).build())
					.build();
			if (ShareDialog.canShow(ShareLinkContent.class)) {
				shareDialog.show(linkContent);
			}
			else{
				Snackbar.make(view, "Share error", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		}
	}

	public class ParseJSON extends AsyncTask<String , Void , StringBuilder> {

		@Override
		protected StringBuilder doInBackground(String... params) {

			String link = params[0];

			try {
				Log.e("tryyyy", "rrrrrrrrrrrrr");
				URL url = new URL(link);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				BufferedReader br = new BufferedReader(inputStreamReader);
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					line = br.readLine();
				}

				JSONObject jsonObject = new JSONObject(sb.toString());

				JSONObject result = jsonObject.getJSONObject("result");

				if (result.has("website")) {
					website.append(result.getString("website"));
				}
				else{
					website.append("None");
				}
				name.append(result.getString("name"));
				Log.d("jda", sb.toString());
				return sb;
			} catch (MalformedURLException me) {
				Log.d("123", "Malformed");
			} catch (IOException ioe) {
				Log.d("456", "IOE");
			}  catch (Exception e) {
				Log.d("789", "igisdo");
			}
			return null;
		}

		@Override
		protected void onPostExecute(StringBuilder s) {
			super.onPostExecute(s);
			StringJSON.append(s);
			createViewInfo();
		}
	}

	private void moveCameraToThisLocation() {
		mGeoDataClient.getPlaceById(placeID).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
			@Override
			public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
				if (task.isSuccessful()) {
					PlaceBufferResponse places = task.getResult();
					Place myPlace = places.get(0);
					Log.i("place_error", "Place found: " + myPlace.getName());
					LatLng location = myPlace.getLatLng();

					mMap.addMarker(new MarkerOptions().title(myPlace.getAddress().toString())
							.position(location));
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
					places.release();
				} else {
					Log.e("place_error", "Place not found.");
				}
			}
		});
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
