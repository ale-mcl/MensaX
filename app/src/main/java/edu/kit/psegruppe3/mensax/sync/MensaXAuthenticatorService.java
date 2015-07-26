package edu.kit.psegruppe3.mensax.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The service which allows the sync adapter framework to access the authenticator.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class MensaXAuthenticatorService extends Service {

    /**
     * Instance field that stores the authenticator object
     */
    private MensaXAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new MensaXAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
