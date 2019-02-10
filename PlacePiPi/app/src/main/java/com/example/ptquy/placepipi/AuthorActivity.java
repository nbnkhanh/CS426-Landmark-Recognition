package com.example.ptquy.placepipi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ramotion.circlemenu.CircleMenuView;

public class AuthorActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author);

		final CircleMenuView menu = findViewById(R.id.circle_menu);

		menu.setEventListener(new CircleMenuView.EventListener() {
			@Override
			public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
				Log.d("D", "onMenuOpenAnimationStart");
				menu.setClickable(false);
				menu.performClick();
			}

			@Override
			public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
				Log.d("D", "onMenuOpenAnimationEnd");

			}

			@Override
			public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
				Log.d("D", "onMenuCloseAnimationStart");
			}

			@Override
			public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
				Log.d("D", "onMenuCloseAnimationEnd");
			}

			@Override
			public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
				Log.d("D", "onButtonClickAnimationStart| index: " + index);
			}

			@Override
			public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
				Log.d("D", "onButtonClickAnimationEnd| index: " + index);
				Intent intent;
				if(index == 0){
					intent = new Intent(AuthorActivity.this, KhanhActivity.class);
				}
				else if(index == 1){
					intent = new Intent(AuthorActivity.this, QuyenActivity.class);
				}
				else {
					intent = new Intent(AuthorActivity.this, TuanActivity.class);
				}
				startActivity(intent);
			}

			@Override
			public boolean onButtonLongClick(@NonNull CircleMenuView view, int index) {
				Log.d("D", "onButtonLongClick| index: " + index);
				return true;
			}

			@Override
			public void onButtonLongClickAnimationStart(@NonNull CircleMenuView view, int index) {
				Log.d("D", "onButtonLongClickAnimationStart| index: " + index);
			}

			@Override
			public void onButtonLongClickAnimationEnd(@NonNull CircleMenuView view, int index) {
				Log.d("D", "onButtonLongClickAnimationEnd| index: " + index);
			}
		});
	}
}
