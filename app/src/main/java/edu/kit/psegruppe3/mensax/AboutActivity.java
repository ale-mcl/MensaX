package edu.kit.psegruppe3.mensax;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

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
            return true;

        } else if (id == R.id.action_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);

        }else if (id == R.id.action_liveCam) {
            Intent intent = new Intent(this, LiveCamsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
