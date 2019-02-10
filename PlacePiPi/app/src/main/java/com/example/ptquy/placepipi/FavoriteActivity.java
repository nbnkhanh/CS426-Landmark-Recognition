package com.example.ptquy.placepipi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

	private RecyclerView recyclerView;
	private ArrayList<PlaceDataSQL> arrayList;
	private PlaceDataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		PlaceSQLite db = new PlaceSQLite(this, PlaceSQLite.DB_name, null, 1);
		arrayList = db.getAllFavPlaceData();

		recyclerView = (RecyclerView) findViewById(R.id.fav_list);
		recyclerView.setHasFixedSize(true);
		adapter = new PlaceDataAdapter(this, arrayList);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(adapter);
	}
}
