package edu.kit.psegruppe3.mensax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Activity that shows how to contact the developer of this app.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class ContactActivity extends ActionBarActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);

        String contactUs = "This app has been developed by " +
                "The MensaX students Group at KIT.\n" +
                "All rights reserved.\n" +
                "Specific comments and general or private inquires can be send to:\n"+
                "mensaxapp@web.de";

        TextView txtContact = (TextView) findViewById(R.id.txtContact);
        txtContact.setText(contactUs);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        } else if (id == R.id.action_search) {
            onSearchRequested();
            return true;

        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_contact) {
            return true;

        }else if (id == R.id.action_liveCam) {
            Intent intent = new Intent(this, LiveCamsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
