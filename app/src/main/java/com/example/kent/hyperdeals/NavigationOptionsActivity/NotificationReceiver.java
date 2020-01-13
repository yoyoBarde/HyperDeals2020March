package com.example.kent.hyperdeals.NavigationOptionsActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    String TAG = "Receiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase("INTERESTED")) {
            Log.e(TAG,"INTERESTED");
//            Toast.makeText(context, "Booking your ride", Toast.LENGTH_SHORT).show();


        } else if (intent.getAction().equalsIgnoreCase("DISMISS")) {
//
            Log.e(TAG,"DISMISS");

//            NotificationManager notificationManager =
//                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancel(11111);

        }



    }
}
