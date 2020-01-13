package com.example.kent.hyperdeals.NavigationOptionsActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.PromoModel
import com.example.kent.hyperdeals.Model.userHistoryParce
import com.example.kent.hyperdeals.MyAdapters.HottestPromoAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_history.*
import org.jetbrains.anko.doAsync

class UserHistory : AppCompatActivity() {
    var database = FirebaseFirestore.getInstance()
    val TAG = "UserHistory"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_history)
        getUserHistories()


    }

    fun getUserHistories() {
        var userHistories = ArrayList<userHistoryParce>()
        doAsync {
            database.collection("UserHistory").document(LoginActivity.userUIDS).collection("Promo").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {
                        Log.e(TAG,"retrieving history")
                        var userHistoryParce = DocumentSnapshot.toObject(userHistoryParce::class.java)
                        userHistories.add(userHistoryParce)
                    }
                    getUserHistoriesObject(userHistories)
                }
                else{
                    Log.e(TAG, "retrieving fail")
                }

            }
        }
    }

    fun getUserHistoriesObject(userHistories: ArrayList<userHistoryParce>) {
        var promolist = ArrayList<PromoModel>()

        for (j in 0 until userHistories.size) {
            for (i in 0 until FragmentCategory.globalPromoList.size) {
            Log.e(TAG,"${userHistories[j].promoName} and ${FragmentCategory.globalPromoList[i].promoStore}" )
                if (userHistories[j].promoName.equals(FragmentCategory.globalPromoList[i].promoStore)) {
                    Log.e(TAG,"na true or wala")
                    promolist.add(FragmentCategory.globalPromoList[i])
                }

            }


        }


        Log.e(TAG," ${FragmentCategory.globalPromoList.size}  ${promolist.size} ngeek")
        var myAdapter = HottestPromoAdapter(this,promolist)
        RecyclerHistory.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        RecyclerHistory.adapter = myAdapter

    }
}
