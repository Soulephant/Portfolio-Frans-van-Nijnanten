package project.prototype.ipmedt4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.content.Intent;
import android.widget.Button;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * De Class voor categorie 2
 * 
 * @author Lars Noorlander
 */
public class Categorie2 extends Activity
{	
	Button settings;
	Button search;
	Button terug;
	
	/** The item1. */
	Button item1;
	Button item2;
	Button item3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//koppel de xml aan de java
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorie2);

		addListenerOnButton();
		//laad het listview object
	}

	//maak een menubalk
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//vul de menubalk in
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// verwijs de info knop door naar het info scherm.
		switch (item.getItemId()) {
		case R.id.info:
			openContact(settings);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * Opent contact pagina.
	 * 
	 * @param view
	 */
	private void openContact(View view) {

		Intent myIntent = new Intent(view.getContext(), Info.class);
		startActivityForResult(myIntent, 0);
	}

	/**
	 * Maakt een listener die buttons de mogelijkheid geeft om ingedrukt te worden.
	 * Zo kan in deze layout nog op Settings en Search worden gedrukt.
	 */
	public void addListenerOnButton() {


		item1 = (Button) findViewById(R.id.item1);
		item1.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), NotFound.class);
				startActivityForResult(myIntent, 0);
			}
		});

		item2 = (Button) findViewById(R.id.item2);
		item2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), NotFound.class);
				startActivityForResult(myIntent, 0);
			}
		});

		item3 = (Button) findViewById(R.id.item3);
		item3.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(), NotFound.class);
				startActivityForResult(myIntent, 0);
			}
		});

		settings = (Button) findViewById(R.id.settings);
		settings.setOnClickListener(new OnClickListener() {
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
		terug.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
