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
 * De Class voor categorie 1
 * 
 * @author Lars Noorlander
 */
public class Categorie1 extends Activity
{	
	
	/** The settings. */
	Button settings;
	
	/** The search. */
	Button search;
	
	/** The terug. */
	Button terug;
	
	/** The item1. */
	Button item1;
	
	/** The item2. */
	Button item2;
	
	/** The item3. */
	Button item3;
	
	/** The item4. */
	Button item4;
	
	/** The item5. */
	Button item5;
	
	/** The item6. */
	Button item6;
	
	/** The item categorie. */
	int itemCategorie = 0;
	
	/** The nummer handler categorie. */
	ItemHandle nummerHandlerCategorie = new ItemHandle();
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//koppel de xml aan de java
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorie1);

		addListenerOnButton();
		//laad het listview object
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
			openContact(settings);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * Opent contact pagina.
	 *
	 * @param view the view
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
				nummerHandlerCategorie.setDBitem(0);
				Intent myIntent = new Intent(view.getContext(), AsyncTaskPull.class);
				startActivityForResult(myIntent, 0);
			}
		});

		item2 = (Button) findViewById(R.id.item2);
		item2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				nummerHandlerCategorie.setDBitem(1);
				Intent myIntent = new Intent(view.getContext(), AsyncTaskPull.class);
				startActivityForResult(myIntent, 0);
			}
			
		});

		item3 = (Button) findViewById(R.id.item3);
		item3.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view)
			{
				nummerHandlerCategorie.setDBitem(2);
				Intent myIntent = new Intent(view.getContext(), AsyncTaskPull.class);
				startActivityForResult(myIntent, 0);
			}
		});

		item4 = (Button) findViewById(R.id.item4);
		item4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) 
			{
				nummerHandlerCategorie.setDBitem(3);
				Intent myIntent = new Intent(view.getContext(), AsyncTaskPull.class);
				startActivityForResult(myIntent, 0);
			}
		});

		item5 = (Button) findViewById(R.id.item5);
		item5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) 
			{
				nummerHandlerCategorie.setDBitem(4);
				Intent myIntent = new Intent(view.getContext(), AsyncTaskPull.class);
				startActivityForResult(myIntent, 0);
			}
		});

		item6 = (Button) findViewById(R.id.item6);
		item6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) 
			{
				nummerHandlerCategorie.setDBitem(5);
				Intent myIntent = new Intent(view.getContext(), AsyncTaskPull.class);
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
	
	/**
	 * Sets itemCategorie.
	 *
	 * @param num the new nummer
	 */
	public void setNummer(int num)
	{
		itemCategorie = num;
	}
	
	/**
	 * Gets itemCategorie.
	 * 
	 * @return itemCategorie terug
	 */
	public int getNummerTerug()
	{
		return itemCategorie;
	}
}
