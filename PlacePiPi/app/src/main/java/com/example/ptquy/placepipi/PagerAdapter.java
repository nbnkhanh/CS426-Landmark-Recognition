package com.example.ptquy.placepipi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter{

	private String placedata;
	private ArrayList<String> arrayListFavID;
	public PagerAdapter(FragmentManager fm, String placedata, ArrayList<String> arrayListFavID) {
		super(fm);
		this.placedata = placedata;
		this.arrayListFavID = arrayListFavID;
		Log.d("kkkk", placedata);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment frag = null;
		Bundle bundle = new Bundle();
		bundle.putCharSequence("placedata", placedata);

		switch (position){
			case 0:
				frag = new PlaceInfoFragment();
				bundle.putSerializable("fav_id", arrayListFavID);
				frag.setArguments(bundle);
				break;
			case 1:
				frag = new PlaceImagesFragment();
				frag.setArguments(bundle);
				break;
			case 2:
				frag = new ReviewsFragment();
				frag.setArguments(bundle);
				break;
		}
		return frag;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		String title = "";
		switch (position){
			case 0:
				title = "Overview";
				break;
			case 1:
				title = "Images";
				break;
			case 2:
				title = "Reviews";
				break;
		}
		return title;
	}
}
