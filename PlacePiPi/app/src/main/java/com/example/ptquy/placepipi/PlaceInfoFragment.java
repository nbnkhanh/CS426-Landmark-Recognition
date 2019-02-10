package com.example.ptquy.placepipi;

import android.Manifest;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.provider.Telephony;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceInfoFragment extends Fragment{
	private TextView mphone;
	private TextView mweek_time_txt;
	private TextView mAddress;
	private TextView mopenHours;
	private ImageView mphoneicon;
	private ImageButton mbtnLove;
	private PlaceData mplace;
	private TextView mname;
	private com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton;
	private SubActionButton.Builder mitemBuilder;
	private SubActionButton button;
	private SubActionButton button1;
	private SubActionButton button2;
	private SubActionButton button3;
	private FloatingActionMenu actionMenu;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_place_info, container, false);
		final String sb = (String) getArguments().getString("placedata");
		final ArrayList<String> arr_fav_id = (ArrayList<String>) getArguments().getSerializable("fav_id");
		try {
			String address = "None";
			String phone = "None";
			String open_now = "No working time";
			float rating = 0;
			ArrayList<String> week_day_time = null;
			ArrayList<String> type = null;
			String website = "None";

			JSONObject jsonObject = new JSONObject(sb);

			JSONObject result = jsonObject.getJSONObject("result");
			String placeID = result.getString("place_id");
			String name = result.getString("name");

			if (result.has("formatted_address")) {
				address = result.getString("formatted_address");
			}
			if (result.has("formatted_phone_number")) {
				phone = result.getString("formatted_phone_number");
			}
			if (result.has("opening_hours")) {
				JSONObject opening_hours = result.getJSONObject("opening_hours");
				if (opening_hours.has("open_now")) {
					Boolean tmp_hour = opening_hours.getBoolean("open_now");
					if (tmp_hour) {
						open_now = "open now";
					} else
						open_now = "closed";
				}
				if (opening_hours.has("weekday_text")) {
					week_day_time = new ArrayList<>();
					JSONArray weekDay = opening_hours.getJSONArray("weekday_text");
					for (int i = 0; i < weekDay.length(); i++) {
						week_day_time.add(String.valueOf(weekDay.get(i)));
					}
				}
			}
			if (result.has("rating")) {
				rating = (float) result.getDouble("rating");
			}
			if (result.has("types")) {
				type = new ArrayList<>();
				JSONArray types = result.getJSONArray("types");
				for (int i = 0; i < types.length(); i++) {
					type.add(String.valueOf(types.get(i)));
				}
			}
			if (result.has("website")) {
				website = result.getString("website");
			}
			mplace = new PlaceData(placeID, name, rating, address, phone, website, open_now, week_day_time, type);
		} catch (JSONException je) {
			Log.d("ssss", "null goyy");
		}
		mname = (TextView) v.findViewById(R.id.place_name);
		mphone = (TextView) v.findViewById(R.id.Phone);
		mopenHours = (TextView) v.findViewById(R.id.openhours);
		mphoneicon = (ImageView) v.findViewById(R.id.phoneicon);
		mAddress = (TextView) v.findViewById(R.id.address);
		mweek_time_txt = (TextView) v.findViewById(R.id.week_time);
		mbtnLove = (ImageButton) v.findViewById(R.id.btnLove);

		if(mplace != null){
			mname.setText(mplace.getName());
			mAddress.setText(mplace.getAddress());
			mphone.setText(mplace.getPhone());
			ArrayList<String> weektime = mplace.getWeek_time();
			if(weektime != null){
				StringBuilder s = new StringBuilder();
				s.append("\n");
				for(int i = 0; i < weektime.size(); i++){
					s.append(weektime.get(i));
					s.append("\n");
				}
				mweek_time_txt.setText(s.toString());
			}
			else{
				mweek_time_txt.setText("None");
			}
			if(mplace.getIsOpen().equals("open now")) {
				mopenHours.setTextColor(Color.GREEN);
			}
			else
				mopenHours.setTextColor(Color.RED);
			mopenHours.setText(mplace.getIsOpen());
			LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.type);
			ArrayList<String> type = mplace.getType();
			if(type != null){
				for(int i = 0; i < type.size(); i++){
					TextView txt_type = new TextView(getContext());
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.setMargins(5,0,5,0);
					txt_type.setLayoutParams(layoutParams);
					txt_type.setTextSize(10);
					txt_type.setPadding(5,0,5,0);
					txt_type.setText(type.get(i));
					txt_type.setTextColor(getResources().getColor(R.color.white));
					txt_type.setBackgroundResource(R.drawable.custom_border_layout);
					linearLayout.addView(txt_type);
				}
			}

			// set if user clicked button favorite
			if(arr_fav_id.contains(mplace.getPlaceId())){
				mbtnLove.setBackgroundResource(R.drawable.heart);
			}

			mbtnLove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!arr_fav_id.contains(mplace.getPlaceId())){
						mbtnLove.setBackgroundResource(R.drawable.heart);
						PlaceSQLite db = new PlaceSQLite(getContext(), PlaceSQLite.DB_name, null, 1);
						db.insertPlaceData(mplace.getPlaceId(), mplace.getName(), mplace.getRating(), mplace.getAddress());
						Snackbar.make(v, "Added to favorite list", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			});

			// floating circle menu
			ImageView icon = new ImageView(getContext()); // Create an icon
			icon.setImageResource(R.drawable.earthicon);

			actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(getActivity()).setContentView(icon).build();
			mitemBuilder = new SubActionButton.Builder(getActivity());
			// repeat many times:
			ImageView itemIcon = new ImageView(getContext());
			itemIcon.setImageResource(R.drawable.webicon);
			button = mitemBuilder.setContentView(itemIcon).build();

			ImageView itemIcon1 = new ImageView(getContext());
			itemIcon1.setImageResource(R.drawable.phoneicon);
			button1 = mitemBuilder.setContentView(itemIcon1).build();

			ImageView itemIcon2 = new ImageView(getContext());
			itemIcon2.setImageResource(R.drawable.smsicon);
			button2 = mitemBuilder.setContentView(itemIcon2).build();


			actionMenu = new FloatingActionMenu.Builder(getActivity())
					.addSubActionView(button)
					.addSubActionView(button1)
					.addSubActionView(button2)
					.attachTo(actionButton)
					.build();

			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = mplace.getWebsiteURL();
					Uri weburl = Uri.parse(url);
					if(!url.equals("None")){
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, weburl);
						PackageManager packageManager = getActivity().getPackageManager();
						List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
						boolean isIntentSafe = activities.size() > 0;
						if (isIntentSafe) {
							startActivity(mapIntent);
						}
					}
					else{
						Snackbar.make(v, "Not available for this location", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			});

			button1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!mplace.getPhone().equals("None")) {
						Uri number = Uri.parse("tel:" + mplace.getPhone());

						Intent callIntent = new Intent(Intent.ACTION_CALL, number);
						if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
								!= PackageManager.PERMISSION_GRANTED) {
							return;
						}
						startActivity(callIntent);
					}
					else{
						Snackbar.make(v, "Not available for this location", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			});
			button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!mplace.getPhone().equals("None")){
						Intent smsIntent = new Intent(Intent.ACTION_VIEW);
						smsIntent.setType("vnd.android-dir/mms-sms");
						smsIntent.putExtra("sms_body", "Hi");
						smsIntent.putExtra("address", mplace.getPhone());
						startActivity(smsIntent);
					}
					else{
						Snackbar.make(v, "Not available for this location", Snackbar.LENGTH_LONG)
								.setAction("Action", null).show();
					}
				}
			});
		}
		return v;
	}
}
