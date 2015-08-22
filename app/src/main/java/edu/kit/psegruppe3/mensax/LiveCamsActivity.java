package edu.kit.psegruppe3.mensax;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Activity that shows the Live Cams of the Mensa.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class LiveCamsActivity extends ActionBarActivity {

    private final String CAMSURL = "http://www.studentenwerk-karlsruhe.de/de/essen/livecams/popup/?page=1";
    public static Activity activity;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livecam);

        activity = this;
        final WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.setVisibility(View.INVISIBLE);
                AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                adb.setCancelable(false);
                adb.setTitle(R.string.error_dialog_title);
                adb.setMessage(R.string.error_dialog_message_livecams);
                adb.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });
                adb.show();
            }
        });
        webView.loadUrl(CAMSURL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
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
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);

        }else if (id == R.id.action_liveCam) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
