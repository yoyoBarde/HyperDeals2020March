package com.example.kent.hyperdeals;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DismissService extends IntentService {
    public static final String ACTION1 = "ACTION1";
    public static final String ACTION2 = "ACTION2";
    public static final String TAG = "DismissService";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DismissService(String name) {
        super(name);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        if (ACTION1.equals(action)) {
            Log.e(TAG," ServiceDismiss ");


        } else if (ACTION2.equals(action)) {
            Log.e(TAG," ServiceDismiss2 ");

            // do some other stuff...
        } else {
            throw new IllegalArgumentException("Unsupported action: " + action);
        }
    }
}