package com.example.kent.hyperdeals
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.example.kent.hyperdeals.FragmentsBusiness.Business_PromoProfile
import com.example.kent.hyperdeals.Model.PromoModel
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter

class NotificationReceiver: BroadcastReceiver() {
  var TAG = "NotificationReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        var myPromo =   intent.getParcelableExtra<PromoModel>("message")
        var myNotificationID = intent.getIntExtra("notificationID",0)
        var yawa = intent.getStringExtra("sure")
        var boolean = intent.getBooleanExtra("boolean",false)
        if(boolean)
            Log.e(TAG,"its true")
        Log.e(TAG, "NotificationReceiver $yawa  ${myPromo.promoID}  ${myPromo.promoname} $myNotificationID")
        PromoListAdapter.promoProfile = myPromo
        context.startActivity(Intent(context,Business_PromoProfile::class.java))
        
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(myNotificationID)



    }
}
class NotificationReceiverDismiss : BroadcastReceiver() {
    var TAG = "NotificationReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        var myPromo =   intent.getParcelableExtra<PromoModel>("message")
        var myNotificationID = intent.getIntExtra("notificationID",0)

        Log.e(TAG, "NotificationReceiver ${myPromo.promoID}  ${myPromo.promoname} $myNotificationID")
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(myNotificationID)




    }
}

class NotificationReceiverBusinessProfile : BroadcastReceiver() {
    var TAG = "NotificationReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        var myPromo =   intent.getParcelableExtra<PromoModel>("message")
        var myNotificationID = intent.getIntExtra("notificationID",0)
        var yawa = intent.getStringExtra("sure")
        var boolean = intent.getBooleanExtra("boolean",false)
        if(boolean)
            Log.e(TAG,"its true")
        Log.e(TAG, "NotificationReceiverBusinessProfile $yawa  ${myPromo.promoID}  ${myPromo.promoname} $myNotificationID")
        PromoListAdapter.promoProfile = myPromo
        context.startActivity(Intent(context,Business_PromoProfile::class.java))

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(myNotificationID)




    }
}

