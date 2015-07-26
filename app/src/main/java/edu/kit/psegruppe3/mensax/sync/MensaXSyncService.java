package edu.kit.psegruppe3.mensax.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * The MensaSyncService class provides a service to run the syncadapter tasks
 * in the background.
 *
 * @author MensaX-group
 * @version 1.0
 */
public class MensaXSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static MensaXSyncAdapter sMensaXSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sMensaXSyncAdapter == null) {
                sMensaXSyncAdapter = new MensaXSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sMensaXSyncAdapter.getSyncAdapterBinder();
    }
}
