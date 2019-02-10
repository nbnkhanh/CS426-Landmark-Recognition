package com.example.ptquy.placepipi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadImageActivity extends AppCompatActivity {

	private static int RESULT_LOAD_IMAGE = 1;
	private Bitmap mbm;
	private ByteArrayOutputStream mbos;
	private byte[] mbitmapdata;
	private String mimgdata;
	private Uri mselectedImage;
	private Handler mhandler;
	private ProgressDialog mprogressDialog;
	private String mip;
	private EditText mip_edit;
	private static final int ACTIVITY_START_CAMERA_APP = 0;
	private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 1;
	private ImageView mCaptureView;
	private String mImageFileLocation = "" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_image);

		mCaptureView = (ImageView) findViewById(R.id.LoadedImage);

		Intent iptest = getIntent();
		mip = iptest.getStringExtra("ip");
		Toast.makeText(UploadImageActivity.this, mip, Toast.LENGTH_SHORT).show();

		ImageButton buttonLoadImage = (ImageButton) findViewById(R.id.buttonLoadPicture);
		ImageButton buttonCaptureImg = (ImageButton) findViewById(R.id.btnCapture);

		LinearLayout layout_chooseImg = (LinearLayout) findViewById(R.id.layout_chooseImg);
		LinearLayout layout_captureImg = (LinearLayout) findViewById(R.id.layout_captureImg);

		mhandler = new Handler(getApplicationContext().getMainLooper());

		mprogressDialog = new ProgressDialog(UploadImageActivity.this);

		Button btn = (Button) findViewById(R.id.btntest);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UploadImageActivity.this, PlaceInfoActivity.class);
				intent.putExtra("placeID", "ChIJ5QCuTRkvdTERY_6WvdDFkEs");
				startActivity(intent);
			}
		});

		layout_chooseImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				/*Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);*/

				if(!mip.equals("")){
					Intent itent = new Intent(Intent.ACTION_GET_CONTENT);
					itent.setType("image/*");

					Intent pickitent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					pickitent.setType("image/*");

					Intent chooseitent = Intent.createChooser(itent, "Select image");
					chooseitent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickitent});

					startActivityForResult(chooseitent, RESULT_LOAD_IMAGE);
				}
				else{
					Toast.makeText(UploadImageActivity.this, "Remember to enter server ip", Toast.LENGTH_SHORT).show();
				}
			}
		});

		layout_captureImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePhoto(v);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri imguri = data.getData();
			mselectedImage = imguri;
			mCaptureView.setImageURI(imguri);
			try {
				mbm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mselectedImage);
			}catch(IOException ioe){

			}
			new UploadImageActivity.HandleImage().execute();
		}
		else if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK){
			rotateImage(setReducedImageSize());
			new UploadImageActivity.HandleImage().execute();
		}
	}

	public void takePhoto(View view){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			if (ContextCompat.checkSelfPermission(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				callCameraApp();
			} else {
				if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					Toast.makeText(this, "Exxternal storage permission required to save images", Toast.LENGTH_SHORT).show();
				}
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_RESULT);
			}
		} else {
			callCameraApp();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_EXTERNAL_STORAGE_RESULT){
			if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
				callCameraApp();
			} else {
				Toast.makeText(this, "External write permission has not been granted, camera cannot save images", Toast.LENGTH_SHORT).show();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void callCameraApp(){
		Intent callCameraApplicationIntent = new Intent();
		callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		File photoFile = null;
		try{
			photoFile = createImageFile();
		} catch (IOException e){
			e.printStackTrace();
		}
		String authorities = getApplicationContext().getPackageName() + ".fileprovider";
		Uri imageUri = FileProvider.getUriForFile(this, authorities, photoFile);
		callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
	}

	private File createImageFile() throws IOException{
		String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		String imageFileName = "IMAGE_" + timestamp + '_';
		File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File image = File.createTempFile(imageFileName, ".jpeg", storageDirectory);
		mImageFileLocation = image.getAbsolutePath();
		return image;
	}

	private Bitmap setReducedImageSize(){
		int targetImageViewWidth = mCaptureView.getWidth();
		int targetImageViewHeight = mCaptureView.getHeight();
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
		int cameraImageWidth = bmOptions.outWidth;
		int cameraImageHeight = bmOptions.outHeight;
		int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
	}

	private void rotateImage (Bitmap bitmap){
		ExifInterface exifInterface = null;
		try {
			exifInterface = new ExifInterface(mImageFileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
		Matrix matrix = new Matrix();
		switch (orientation){
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix.setRotate(90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.setRotate(180);
				break;
			default:
		}
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		mCaptureView.setImageBitmap(rotatedBitmap);
		mbm = rotatedBitmap;
	}

	public class HandleImage extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);

		}

		@Override
		protected String doInBackground(Void... uris) {

			/*String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			ImageView imageView = (ImageView) findViewById(R.id.LoadedImage);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			bm = BitmapFactory.decodeFile(picturePath);*/

			new Thread(new Runnable() {
				@Override
				public void run() {
					mhandler.post(new Runnable() {
						@Override
						public void run() {
							mprogressDialog.setMessage("Uploading, please wait...");
							mprogressDialog.show();
						}
					});
				}
			}).start();

			mbos = new ByteArrayOutputStream();
			mbm.compress(Bitmap.CompressFormat.JPEG, 100, mbos);
			mbitmapdata = mbos.toByteArray();
			mimgdata = Base64.encodeToString(mbitmapdata, Base64.DEFAULT);

			//sending image to server
			StringRequest request = new StringRequest(Request.Method.POST, "http://"+ mip +":8000/predict", new Response.Listener<String>(){
				@Override
				public void onResponse(String s) {
					mprogressDialog.dismiss();
					Toast.makeText(UploadImageActivity.this, "Uploaded successfully " + s, Toast.LENGTH_LONG).show();
					Intent intent = new Intent(UploadImageActivity.this, PlaceInfoActivity.class);
					intent.putExtra("placeID", s);
					startActivity(intent);
				}
			},new Response.ErrorListener(){
				@Override
				public void onErrorResponse(VolleyError volleyError) {
					mprogressDialog.dismiss();
					Toast.makeText(UploadImageActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
				}
			}) {
				//adding parameters to send
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> parameters = new HashMap<String, String>();
					parameters.put("img", mimgdata);
					return parameters;
				}
			};

			RequestQueue rQueue = Volley.newRequestQueue(UploadImageActivity.this);
			rQueue.add(request);
			Integer tmp = mimgdata.length();
			Log.d("quyen", String.valueOf(tmp));
			return mimgdata;
		}
	}
}
