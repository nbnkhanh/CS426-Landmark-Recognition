package com.example.ptquy.placepipi;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ConvertCurrency extends AppCompatActivity {

	private int choice=0;
	private double rate = 1;
	private ArrayList<CurrencyData> arrayList;
	private CurrencyAdapter adapter;
	private CurrencyAdapter adapter2;
	private String srcName;
	private String desName;
	private double value;
	private EditText editText;
	TextView resultText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_convert_currency);

		editText = (EditText)findViewById(R.id.inputAmount);
		resultText=(TextView)findViewById(R.id.result);
		arrayList = new ArrayList<>();
		arrayList.add(new CurrencyData("USD", R.drawable.usd3));
		arrayList.add(new CurrencyData("EUR", R.drawable.eur));
		arrayList.add(new CurrencyData("SGD", R.drawable.sgd));
		arrayList.add(new CurrencyData("JPY", R.drawable.jpy));
		arrayList.add(new CurrencyData("CNY", R.drawable.cny));
		arrayList.add(new CurrencyData("AUD", R.drawable.aud));
		arrayList.add(new CurrencyData("VND", R.drawable.vnd));
		arrayList.add(new CurrencyData("GBP", R.drawable.gbp));
		arrayList.add(new CurrencyData("CAD", R.drawable.cad));
		arrayList.add(new CurrencyData("MYR", R.drawable.myr));
		arrayList.add(new CurrencyData("KRW", R.drawable.krw));

		createView(R.id.currency_list);
		createView(R.id.currency_list_2);
	}

	public void convertClick(View view) {

		int src = adapter.getLastSelectedPosition();
		int des = adapter2.getLastSelectedPosition();
		//Toast.makeText(this, String.valueOf(src) + " " + String.valueOf(des) + " " + String.valueOf(rate), Toast.LENGTH_SHORT).show();
		if(src == -1 || des == -1){
			Snackbar.make(view, "Remember to choose your type of currency", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
		}
		else {
			srcName = arrayList.get(src).getName();
			desName = arrayList.get(des).getName();
			//Toast.makeText(this, srcName + " " + desName, Toast.LENGTH_SHORT).show();
			String link = "http://free.currencyconverterapi.com/api/v3/convert?q=" + srcName + "_" + desName + "&compact=ultra";
			if (!editText.getText().toString().trim().equals("")) {
				new MyAsyncTask().execute(link);

			} else {
				Snackbar.make(view, "Remember to input your data", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		}
	}

	public class MyAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... strings) {
			//1USD to VND
			String url_select = strings[0];
			HttpURLConnection connection = null;
			BufferedReader reader = null;

			try {
				URL url = new URL(url_select);
				connection=(HttpURLConnection)url.openConnection();
				connection.connect();

				InputStream stream = connection.getInputStream();
				reader = new BufferedReader(new InputStreamReader(stream));
				StringBuffer buffer = new StringBuffer();
				String line="";
				while((line=reader.readLine())!=null){
					buffer.append(line+"\n");
				}
				return buffer.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (s==null) {
				Toast.makeText(ConvertCurrency.this, "Error occurred", Toast.LENGTH_SHORT).show();
			}
			else{
				try {
					JSONObject jsonObject = new JSONObject(s);
					String parse=jsonObject.getString(srcName + "_" + desName);
					rate=Double.parseDouble(parse);
					Toast.makeText(ConvertCurrency.this, srcName + " " + desName + " " + String.valueOf(rate), Toast.LENGTH_SHORT).show();
					value = Double.parseDouble(editText.getText().toString());
					value *= rate;
					resultText.setText(String.valueOf(value));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void createView(int recyclerView_id){
		RecyclerView recyclerView = (RecyclerView) findViewById(recyclerView_id);
		recyclerView.setHasFixedSize(true);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		recyclerView.setLayoutManager(linearLayoutManager);

		if(recyclerView_id == R.id.currency_list) {
			adapter = new CurrencyAdapter(arrayList, getApplicationContext());
			recyclerView.setAdapter(adapter);
		}
		else {
			adapter2 = new CurrencyAdapter(arrayList, getApplicationContext());
			recyclerView.setAdapter(adapter2);
		}
	}
}
