package project.prototype.ipmedt4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * De Class AsyncTaskPull.
 *
 * @author Jim Schoorl, Frans van Nijnanten
 */
public class AsyncTaskPull extends Activity {

	ArrayList<JSONObject> arrayValue = new ArrayList<JSONObject>();
	TextView txt;
	TextView txt2;
	TextView txt_percentage;
	String[] attID = null;
	String[] attDesc = null;
	String[] attNaam = null;
	Button settings;
	Button search;
	Button terug;
	int asyncItemNummer;
	ItemHandle nummerHandlerAsync = new ItemHandle();



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		//koppel de xml aan de java
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_async_task);

		addListenerOnButton();
		txt = (TextView) findViewById(R.id.textView1);
		txt2 = (TextView) findViewById(R.id.textView2);  
		search = (Button) findViewById(R.id.search);
		settings = (Button) findViewById(R.id.settings);

		this.asyncItemNummer = nummerHandlerAsync.getDBitem();
		new ShowDialogAsyncTask().execute();



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		//Verwijst de info knop door naar het info scherm.
		switch (item.getItemId()) {
		case R.id.info:
			openInfo(search);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Open info.
	 *
	 * @param View view
	 */
	private void openInfo(View view) {

		Intent myIntent = new Intent(view.getContext(), Info.class);
		startActivityForResult(myIntent, 0);
	}

	/**
	 * De Class ShowDialogAsyncTask.
	 */
	private class ShowDialogAsyncTask extends AsyncTask<Void, Void, String>
	{


		@Override
		protected void onPreExecute() 
		{
			//Update de UI zodra de task is uitgevoerd.
			super.onPreExecute();

			// Set txt naar Connecting en roept de volgende task toe.
			Toast.makeText(AsyncTaskPull.this,
					"Invoke onPreExecute()", Toast.LENGTH_SHORT).show();

			txt.setText("Connecting...");
		}

		@Override
		protected String doInBackground(Void... params) {

			InputStream is = null;

			String result = "";

			// In test.php goes De SQL query.
			String URL = "http://timvandam.nl/App/connect.php";

			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(URL);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();


			}catch(Exception e){
				Log.e("log_tag", "Error in http connection "+e.toString());
			}

			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				is.close();
				result=sb.toString();

			}

			catch(Exception e)
			{
				Log.e("log_tag", "Error converting result "+e.toString());
			}

			String returnString = null;

			try{
				JSONArray jArray = new JSONArray(result);

				attID = new String[jArray.length()];
				attNaam = new String[jArray.length()];
				attDesc = new String[jArray.length()];

				//For loop loopt alle kollommen af en zet ze in een array.
				for(int i=0;i<jArray.length();i++)
				{
					//Hier wordt de JSon data in een array gezet. Deze worden later geladen als tekst.
					attID[i] = jArray.getJSONObject(i).getString("Attraction_ID");
					attNaam[i] = jArray.getJSONObject(i).getString("Attraction_name_Dutch");
					attDesc[i] = jArray.getJSONObject(i).getString("Attraction_description_Dutch");

				}
			}

			catch(JSONException e)
			{
				Log.e("log_tag", "Error parsing data "+e.toString());
			}

			return returnString; 




		}  

		/**
		 * De nummer van de item/knop uit Categorie wordt uit ItemHandle.java gehaald 
		 * om geset te worden volgens wat gedefinieerd is via de PHP in de array.
		 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Toast.makeText(AsyncTaskPull.this,"Invoke onPostExecute()", Toast.LENGTH_SHORT).show();

			txt.setText(attNaam[nummerHandlerAsync.getDBitem()]);
			txt2.setText(attDesc[nummerHandlerAsync.getDBitem()]);

			LinearLayout plaatje=(LinearLayout)findViewById(R.id.plaatje);

			switch(nummerHandlerAsync.getDBitem()) {
			case 0:
				plaatje.setBackgroundResource(R.drawable.cat1item4);
				break;
			case 1:
				plaatje.setBackgroundResource(R.drawable.cat1item5);
				break;
			case 2:
				plaatje.setBackgroundResource(R.drawable.cat1item1);
				break;
			case 3:
				plaatje.setBackgroundResource(R.drawable.cat1item3);
				break;
			case 4:
				plaatje.setBackgroundResource(R.drawable.cat1item2);
				break;
			case 5:
				plaatje.setBackgroundResource(R.drawable.cat1item6);
				break;
			}

		}
	}

	/**
	 * Maakt een listener die buttons de mogelijkheid geeft om ingedrukt te worden.
	 * Zo kan in deze layout nog op Settings en Search worden gedrukt.
	 */
	public void addListenerOnButton() {

		//koppel de button code aan een widget en voeg een onclicklistener toe
		settings = (Button) findViewById(R.id.settings);

		settings.setOnClickListener(new OnClickListener() {
			//koppel de button aan een nieuw xml scherm wat opent
			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), Settings.class);
				startActivityForResult(myIntent, 0);
			}

		});

		search = (Button) findViewById(R.id.search);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), Search.class);
				startActivityForResult(myIntent, 0);
			}
		});
		terug = (Button) findViewById(R.id.terug);

		terug.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}

		});


	}

}

