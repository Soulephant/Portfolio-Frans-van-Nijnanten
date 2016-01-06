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
 * De Search.
 * Dit is het zoekscherm
 * 
 * @author Lars Noorlander
 */
public class Search extends Activity
{	
	
	/** The terug. */
	Button terug;
	
	/** The search. */
	Button search;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//het koppelen van de xml aan java
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		addListenerOnButton();

	}

	//maak een menubalk
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//vul de menubalk
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
			openinfo(terug);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * Openinfo.
	 *
	 * @param view the view
	 */
	private void openinfo(View view) {

		Intent myIntent = new Intent(view.getContext(), Info.class);
		startActivityForResult(myIntent, 0);
	}
	
	/**
	 * Adds the listener on button.
	 */
	public void addListenerOnButton() {
		
		search = (Button) findViewById(R.id.search);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), NotFound.class);
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
