package com.example.ptquy.placepipi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceSQLite extends SQLiteOpenHelper{

	public static String DB_name = "place_fav_db";
	private static String table_name = "Places";
	private static String PlaceID = "PlaceID";
	private static String Name = "Name";
	private static String Rating = "Rating";
	private static String Address = "Address";

	private static String CREATE_TABLE = "CREATE TABLE " + table_name + "("
			                                             + PlaceID + " VARCHAR(50) NOT NULL PRIMARY KEY,"
			                                             + Name + " NVARCHAR(500) NOT NULL,"
			                                             + Rating + " FLOAT,"
														 + Address + " NVARCHAR(500));";


	public PlaceSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + table_name);
		onCreate(db);
	}

	public void insertPlaceData(String placeId, String name, float rating, String address){
		SQLiteDatabase database = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(PlaceID, placeId);
		contentValues.put(Name, name);
		contentValues.put(Rating, rating);
		contentValues.put(Address, address);
		long result = database.insert(table_name, null, contentValues);
	}

	public ArrayList<PlaceDataSQL> getAllFavPlaceData(){
		ArrayList<PlaceDataSQL> arrayList;
		PlaceDataSQL place;
		Cursor cursor = this.GetData("SELECT * FROM " + table_name);
		if(cursor != null){
			cursor.moveToFirst();
			arrayList = new ArrayList<>();
			while(!cursor.isAfterLast()){
				String id = cursor.getString(cursor.getColumnIndex(PlaceID));
				String name = cursor.getString(cursor.getColumnIndex(Name));
				float rating = cursor.getFloat(cursor.getColumnIndex(Rating));
				String address = cursor.getString(cursor.getColumnIndex(Address));
				place = new PlaceDataSQL(id, name, address, rating);
				arrayList.add(place);
				cursor.moveToNext();
			}
			return arrayList;
		}
		else
			return null;
	}

	public ArrayList<String> getAllFavPlaceID(){
		ArrayList<String> arrayList;
		Cursor cursor = this.GetData("SELECT " + PlaceID + " FROM " + table_name);
		if(cursor != null){
			arrayList = new ArrayList<>();
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				String id = cursor.getString(cursor.getColumnIndex(PlaceID));
				arrayList.add(id);
				cursor.moveToNext();
			}
			return arrayList;
		}
		return null;
	}

	public void removeFavPlace(String id){
		this.QueryData("DELETE FROM " + table_name + " WHERE " + PlaceID + " = " + "'" + id + "'" + ";");
	}

	public void QueryData(String sql){
		SQLiteDatabase database = getWritableDatabase();
		database.execSQL(sql);
	}

	public Cursor GetData(String sql){
		SQLiteDatabase database = getReadableDatabase();
		return database.rawQuery(sql, null);
	}
}
