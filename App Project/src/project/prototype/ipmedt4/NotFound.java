package project.prototype.ipmedt4;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

// TODO: Auto-generated Javadoc
/**
 * Not Found
 * Dit is de class als een scherm niet gevonden word
 * 
 * @author Lars Noorlander & Tim van Dam
 */
public class NotFound extends Activity
{	
	
	/** The terug. */
	Button terug;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notfound);

		addListenerOnButton();
	}

	/**
	 * Adds the listener on button.
	 */
	public void addListenerOnButton() {

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
