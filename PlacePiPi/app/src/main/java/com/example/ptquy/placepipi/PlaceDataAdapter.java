package com.example.ptquy.placepipi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaceDataAdapter extends RecyclerView.Adapter<PlaceDataAdapter.ViewHolder>{

	private Context context;
	private ArrayList<PlaceDataSQL> arrayList;
	private int lastIndexToBeDeleted = -1;
	public PlaceDataAdapter(Context context, ArrayList<PlaceDataSQL> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.place_favorite_item, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		final PlaceDataSQL place = arrayList.get(position);
		holder.placeName.setText(place.getName());
		holder.placeAddress.setText(place.getAddress());
		holder.rating.setRating(place.getRating());

		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, final int position, boolean isLongClick) {
				if(isLongClick) {
					PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
					popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());
					popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							removeAt(position);
							return true;
						}
					});
					popupMenu.show();
				}
				else{
					String id = arrayList.get(position).getID();
					Intent intent = new Intent(view.getContext(), PlaceInfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("placeID", id);
					context.startActivity(intent);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return arrayList.size();
	}

	public void removeAt(int position){
		PlaceSQLite db = new PlaceSQLite(context, PlaceSQLite.DB_name, null, 1);
		db.removeFavPlace(arrayList.get(position).getID());
		arrayList.remove(position);
		notifyItemRemoved(lastIndexToBeDeleted);
		notifyItemRangeChanged(position, arrayList.size());
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

		private TextView placeName;
		private TextView placeAddress;
		private RatingBar rating;

		private ItemClickListener itemClickListener;

		public void setItemClickListener(ItemClickListener itemClickListener) {
			this.itemClickListener = itemClickListener;
		}

		public ViewHolder(View itemView) {
			super(itemView);
			placeName = (TextView) itemView.findViewById(R.id.name_fav);
			placeAddress = (TextView) itemView.findViewById(R.id.address_fav);
			rating = (RatingBar) itemView.findViewById(R.id.rating_fav);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View v) {
			itemClickListener.onClick(v, getAdapterPosition(), false);
		}

		@Override
		public boolean onLongClick(View v) {
			itemClickListener.onClick(v, getAdapterPosition(), true);
			return true;
		}
	}
}
