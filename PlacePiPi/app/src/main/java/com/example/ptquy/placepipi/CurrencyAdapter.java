package com.example.ptquy.placepipi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder>{

	private ArrayList<CurrencyData> arrayList;
	private Context context;
	private int lastSelectedPosition = -1;

	public CurrencyAdapter(ArrayList<CurrencyData> arrayList, Context context) {
		this.arrayList = arrayList;
		this.context = context;
	}

	public int getLastSelectedPosition(){
		return this.lastSelectedPosition;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.currency_item, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
		holder.flagIMG.setImageResource(arrayList.get(position).getImgID());
		holder.btnChoose.setText(arrayList.get(position).getName());
		holder.btnChoose.setChecked(position == lastSelectedPosition);
	}

	@Override
	public int getItemCount() {
		return arrayList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

		private RadioButton btnChoose;
		private ImageView flagIMG;
		private ItemClickListener itemClickListener;

		public void setItemClickListener(ItemClickListener itemClickListener) {
			this.itemClickListener = itemClickListener;
		}

		public ViewHolder(View itemView) {
			super(itemView);
			btnChoose = (RadioButton) itemView.findViewById(R.id.btn_choose);
			flagIMG = (ImageView) itemView.findViewById(R.id.img_flag);
			btnChoose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					lastSelectedPosition = getAdapterPosition();
					notifyDataSetChanged();
				}
			});
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
