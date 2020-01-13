package com.example.kent.hyperdeals

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class NotificationReceiver1 : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("NotificationReceiver", "gagoKaba")
    }
}
