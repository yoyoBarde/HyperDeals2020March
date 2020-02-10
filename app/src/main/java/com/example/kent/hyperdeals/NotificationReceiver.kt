package com.example.kent.hyperdeals
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.FragmentsBusiness.Business_PromoProfile
import com.example.kent.hyperdeals.Model.PromoModel
import com.example.kent.hyperdeals.Model.UserSubcategoriesPreferences
import com.example.kent.hyperdeals.Model.UserSubcategoriesPreferencesParcelable
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync

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
    val database = FirebaseFirestore.getInstance()

    override fun onReceive(context: Context, intent: Intent) {
        var myPromo =   intent.getParcelableExtra<PromoModel>("message")
        var myNotificationID = intent.getIntExtra("notificationID",0)

        Log.e(TAG, "NotificationReceiver ${myPromo.promoID}  ${myPromo.promoname} $myNotificationID")
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(myNotificationID)
        userDismissedSubcategory(myPromo)



    }
    fun userDismissedSubcategory(myPromo:PromoModel) {
        Log.e(TAG, "userViewedSubcategory")
        database.collection("PromoData").document("PromoDismissed").collection("Promos").document(myPromo.promoID).collection("Users").document(FragmentCategory.globalUserDemography.Email).set(FragmentCategory.globalUserDemography).addOnSuccessListener {
            Log.e(TAG, "Store is fucking satored")

        }
        for (i in 0 until myPromo.subcategories.size) {
            doAsync {
                var prevCountDismissed = 0
                database.collection("UserDismissedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).get().addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if (document.exists()) {

                            var mySubcategoryPref = document.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            prevCountDismissed = mySubcategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], prevCountDismissed)
                            database.collection("UserDismissedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserDismissedPreferences Success")
                            }
                        } else {
                            Log.e(TAG, "Cached get failed: ", task.exception)
                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                            database.collection("UserDismissedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserDismissedPreferences Success")
                            }
                        }

                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                        database.collection("UserDismissedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG, "UserDismissedPreferences Success")
                        }


                    }

                }
            }
        }
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


