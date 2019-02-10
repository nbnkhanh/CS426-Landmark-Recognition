package com.example.ptquy.placepipi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OCRActivity extends AppCompatActivity {

	private TessBaseAPI m_tess;

	private static final int ACTIVITY_START_CAMERA_APP = 0;
	private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 1;
	private ImageView mCaptureView;
	private String mImageFileLocation = "" ;
	private Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocr);

		mCaptureView = (ImageView) findViewById(R.id.img_input);
		//mCaptureView.setImageResource(R.drawable.ddddd);
		prepareLanguageDir();
		m_tess = new TessBaseAPI();
		m_tess.init(String.valueOf(getFilesDir()), "vie");

		Button btn = (Button) findViewById(R.id.btn_rec);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				takePhoto(v);
			}
		});
	}

	private void copyFile(){
		AssetManager assMng = getAssets();
		try {
			InputStream is = assMng.open("tessdata/vie.traineddata");
			OutputStream os = new FileOutputStream(getFilesDir()+"/tessdata/vie.traineddata");
			byte[] buffer = new byte[1024];
			int read;
			while((read=is.read(buffer))!=-1){
				os.write(buffer,0,read);
			}
			is.close();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void prepareLanguageDir(){

		File dir = new File(getFilesDir() + "/tessdata");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File trainedData = new File(getFilesDir()+"/tessdata/vie.traineddata");
		if (!trainedData.exists()){
			copyFile();
		}
	}

	private static String urlEscape(String toEscape){
		//if null, keep null (no gain or loss of safety)
		if (toEscape==null)
			return null;

		StringBuilder sb=new StringBuilder();
		for (char character: toEscape.toCharArray())//for every character in the string
			switch (character){//if the character needs to be escaped, add its escaped value to the StringBuilder
				case '!': sb.append("%21"); continue;
				case '#': sb.append("%23"); continue;
				case '$': sb.append("%24"); continue;
				case '&': sb.append("%26"); continue;
				case '\'': sb.append("%27"); continue;
				case '(': sb.append("%28"); continue;
				case ')': sb.append("%29"); continue;
				case '*': sb.append("%2A"); continue;
				case '+': sb.append("%2B"); continue;
				case ',': sb.append("%2C"); continue;
				case '/': sb.append("%2F"); continue;
				case ':': sb.append("%3A"); continue;
				case ';': sb.append("%3B"); continue;
				case '=': sb.append("%3D"); continue;
				case '?': sb.append("%3F"); continue;
				case '@': sb.append("%40"); continue;
				case '[': sb.append("%5B"); continue;
				case ']': sb.append("%5D"); continue;
				case ' ': sb.append("%20"); continue;
				case '"': sb.append("%22"); continue;
				case '%': sb.append("%25"); continue;
				case '-': sb.append("%2D"); continue;
				case '.': sb.append("%2E"); continue;
				case '<': sb.append("%3C"); continue;
				case '>': sb.append("%3E"); continue;
				case '\\': sb.append("%5C"); continue;
				case '^': sb.append("%5E"); continue;
				case '_': sb.append("%5F"); continue;
				case '`': sb.append("%60"); continue;
				case '{': sb.append("%7B"); continue;
				case '|': sb.append("%7C"); continue;
				case '}': sb.append("%7D"); continue;
				case '~': sb.append("%7E"); continue;
				case '\n': sb.append("%0A"); continue;
				default: sb.append(character);//if it does not need to be escaped, add the character itself to the StringBuilder
			}
		return sb.toString();//build the string, and return
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK){
			rotateImage(setReducedImageSize());
		}
	}

	private File createImageFile() throws IOException{
		String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		String imageFileName = "IMAGE_" + timestamp + '_';
		File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, ".jpeg", storageDirectory);
		mImageFileLocation = image.getAbsolutePath();
		Log.d("-->file: ", mImageFileLocation);
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
		//mCaptureView.setImageBitmap(rotatedBitmap);
		Picasso.get().load(new File(mImageFileLocation)).into(mCaptureView);
		m_tess.setImage(rotatedBitmap);
		String result = m_tess.getUTF8Text().toLowerCase();
		TextView resultView = (TextView) findViewById(R.id.txt_result);
		resultView.setText(result);

		//mo web google trans
		Intent webIntent = new Intent(Intent.ACTION_VIEW);
		webIntent.setData(Uri.parse("https://translate.google.com/#vi/en/" + urlEscape(result).trim()));
		startActivity(webIntent);
		deleteImage();
	}

	public void deleteImage() {
		File fdelete = new File(mImageFileLocation);
		if (fdelete.exists()) {
			if (fdelete.delete()) {
				Log.e("-->", "file Deleted :" + mImageFileLocation);
				callBroadCast();
			} else {
				Log.e("-->", "file not Deleted :" + mImageFileLocation);
			}
		}
		else
			Log.e("-->", "file not exists " + mImageFileLocation);
	}

	public void callBroadCast() {
		if (Build.VERSION.SDK_INT >= 14) {
			Log.e("-->", " >= 14");
			MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
				/*
				 *   (non-Javadoc)
				 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
				 */
				public void onScanCompleted(String path, Uri uri) {
					Log.e("ExternalStorage", "Scanned " + path + ":");
					Log.e("ExternalStorage", "-> uri=" + uri);
				}
			});
		} else {
			Log.e("-->", " < 14");
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		}
	}
}
