package com.example.ptquy.placepipi;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewsFragment extends Fragment{
	private ArrayList<ReviewData> arrayList;
	private RecyclerView recyclerView;
	private ReviewAdapter adapter;
	private ReviewData reviewData;
	private TextView rating;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_place_reviews, container, false);
		final String sb = (String) getArguments().getString("placedata");
		rating = (TextView) v.findViewById(R.id.rating);
		reviewData = new ReviewData();
		arrayList = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(sb);
			JSONObject result = jsonObject.getJSONObject("result");
			if(result.has("rating")){
				rating.setText(result.getString("rating"));
			}
			if(result.has("reviews")) {
				JSONArray reviews = result.getJSONArray("reviews");
				for(int i = 0; i < reviews.length(); i++){
					JSONObject object = reviews.getJSONObject(i);
					String username = object.getString("author_name");
					String avt = object.getString("profile_photo_url");
					float rating = (float) object.getDouble("rating");
					long date = object.getLong("time");
					String review = object.getString("text");

					Date d = new Date(date*1000);
					SimpleDateFormat d2 = new SimpleDateFormat("dd/MM/yyyy");
					String date_review = d2.format(d);
					reviewData = new ReviewData(username, avt, review, date_review, rating);
					arrayList.add(reviewData);
					//adapter.notifyDataSetChanged();
				}
			}
		}catch(JSONException je){
			Log.d("ffff", "Dsahdjsah");
		}
		recyclerView = (RecyclerView) v.findViewById(R.id.review_list);
		adapter = new ReviewAdapter(arrayList, getContext());
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
		recyclerView.setLayoutManager(linearLayoutManager);

		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);
		recyclerView.setAdapter(adapter);
		return v;
	}
}
