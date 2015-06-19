package edu.kit.psegruppe3.mensax.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by ekremsenturk on 19.06.15.
 */
public class MensaXAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private MensaXAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new MensaXAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
