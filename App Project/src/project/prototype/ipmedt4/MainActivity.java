 package project.prototype.ipmedt4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.content.Intent;
import android.widget.Button;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

// TODO: Auto-generated Javadoc
/**
 * De Mainactivity.
 * Dit is de main opstart class
 * 
 * @author Lars Noorlander
 */
public class MainActivity extends Activity
{	
	
	/** The settings. */
	Button settings;
	
	/** The search. */
	Button search;
	
	/** The categorie1. */
	Button categorie1;
	
	/** The categorie2. */
	Button categorie2;
	
	/** The categorie3. */
	Button categorie3;
	
	/** The categorie4. */
	Button categorie4;
	//laadt de XML in
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//koppel de xml aan de java
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		addListenerOnButton();

	}

	//maak een menubalk
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//vul de menubalk in
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// verwijs de info knop door naar het info scherm.
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
	 * @param view the view
	 */
	private void openInfo(View view) {

		Intent myIntent = new Intent(view.getContext(), Info.class);
		startActivityForResult(myIntent, 0);
	}


	//maak een listener die buttons de mogelijkheid geeft om ingedrukt te worden
	/**
	 * Adds the listener on button.
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

		categorie1 = (Button) findViewById(R.id.categorie1);

		categorie1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), Categorie1.class);
				startActivityForResult(myIntent, 0);
			}
		});

		categorie2 = (Button) findViewById(R.id.categorie2);

		categorie2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), Categorie2.class);
				startActivityForResult(myIntent, 0);
			}
		});

		categorie3 = (Button) findViewById(R.id.categorie3);

		categorie3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), NotFound.class);
				startActivityForResult(myIntent, 0);
			}
		});

		categorie4 = (Button) findViewById(R.id.categorie4);

		categorie4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), NotFound.class);
				startActivityForResult(myIntent, 0);
			}
		});

	}
}
