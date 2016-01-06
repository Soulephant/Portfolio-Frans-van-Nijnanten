package project.prototype.ipmedt4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

// TODO: Auto-generated Javadoc
/**
 * De settings class
 * In deze class staan de settings
 * 
 * @author Lars Noorlander
 */
public class Settings extends Activity
{	
  
	/** The terug. */
	Button terug;
	
	/** The button1. */
	Button button1;
	
	/** The button2. */
	Button button2;
	
	/** The optie1. */
	TextView optie1;
	
	/** The maps. */
	Button maps;

	//laden van XML
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//het koppelen van de xml aan java
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		optie1 = (TextView)findViewById(R.id.optie);
		
		addListenerOnButton();
		
	      LoadPreferences();

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
			openInfo(terug);
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

	/**
	 * Adds the listener on button.
	 */
	public void addListenerOnButton() {

		button1 = (Button) findViewById(R.id.button1);

		button1.setOnClickListener(new OnClickListener() {

			  @Override
			  public void onClick(View arg0) {
			   // TODO Auto-generated method stub
			   SavePreferences("MEM1", "Fiets");
			   LoadPreferences();
				
			}
		});

		button2 = (Button) findViewById(R.id.button2);

		button2.setOnClickListener(new OnClickListener() {

			  @Override
			  public void onClick(View arg0) {
			   // TODO Auto-generated method stub
			   SavePreferences("MEM1", "Auto");
			   LoadPreferences();

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
		
		maps = (Button) findViewById(R.id.maps);

		maps.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent myIntent = new Intent(view.getContext(), NotFound.class);
				startActivityForResult(myIntent, 0);
			}

		});
		
	}
	   
   	/**
   	 * Save preferences.
   	 *
   	 * @param key the key
   	 * @param value the value
   	 */
   	private void SavePreferences(String key, String value){
		    SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		    SharedPreferences.Editor editor = sharedPreferences.edit();
		    editor.putString(key, value);
		    editor.commit();
		   }
		  
		   /**
   		 * Load preferences.
   		 */
   		private void LoadPreferences(){
		    SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		    String strSavedMem1 = sharedPreferences.getString("MEM1", "");
		    optie1.setText(strSavedMem1);
		   }

}
