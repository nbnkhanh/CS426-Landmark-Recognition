package com.example.ptquy.placepipi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.ramotion.circlemenu.CircleMenuView;
import com.zhy.view.CircleMenuLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

	CircleMenuLayout mCircleMenuLayout;
	private String[] mItemTexts = new String[] { "Start","Change IP","Currency","OCR", "Favorite", "About"};

	private int[] mItemImgs = new int[] { R.drawable.projecthaha,
			R.drawable.settingshaha, R.drawable.currency, R.drawable.ocr, R.drawable.favorite, R.drawable.mobileapp
	};
	private AlertDialog.Builder alert;
	private AlertDialog alertDialog;
	private StringBuilder IP_value = new StringBuilder("");
	private ShareDialog shareDialog;
	EditText ip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//this.deleteDatabase(PlaceSQLite.DB_name);

		shareDialog = new ShareDialog(this);
		alert = new AlertDialog.Builder(this);
		alertConfigure();

		mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);


		mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener()
		{

			@Override
			public void itemClick(View view, int pos)
			{
				Toast.makeText(MainActivity.this, mItemTexts[pos],
						Toast.LENGTH_SHORT).show();
				if(pos == 0){
					Intent intent = new Intent(MainActivity.this, UploadImageActivity.class);
					if(IP_value.toString().equals("")){
						Toast.makeText(MainActivity.this, "Remember to choose ID", Toast.LENGTH_SHORT).show();
					}
					else {
						intent.putExtra("ip", IP_value.toString());
						startActivity(intent);
					}
				}
				else if (pos==1){
					alertDialog.show();
				}
				else if (pos==2){
					Intent intent = new Intent(MainActivity.this,ConvertCurrency.class);
					MainActivity.this.startActivity(intent);
				}
				else if(pos == 3){
					Intent intent = new Intent(MainActivity.this,OCRActivity.class);
					MainActivity.this.startActivity(intent);
				}
				else if(pos == 4){
					Intent intent = new Intent(MainActivity.this,FavoriteActivity.class);
					MainActivity.this.startActivity(intent);
				}
				else{
					Intent intent = new Intent(MainActivity.this,AuthorActivity.class);
					MainActivity.this.startActivity(intent);
				}
			}

			@Override
			public void itemCenterClick(View view)
			{
				ShareLinkContent linkContent=new ShareLinkContent.Builder().
						setContentUrl(Uri.parse("https://placepipi.com")).setShareHashtag(new ShareHashtag.Builder().setHashtag("#PlacePipi").build()).
						setQuote("Cung Place pipipi, nao cung detect tect tect").build();
				if (ShareDialog.canShow(ShareLinkContent.class)) {
					shareDialog.show(linkContent);
				}
				else{
					Toast.makeText(MainActivity.this,"Share Error",Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private void alertConfigure(){
		final EditText edittext = new EditText(this);
		alert.setMessage("Enter your server IP:");
		alert.setTitle("Change IP");

		alert.setView(edittext);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//What ever you want to do with the value
				IP_value.append(edittext.getText().toString());
			}
		});

		alertDialog = alert.create();
	}
}
