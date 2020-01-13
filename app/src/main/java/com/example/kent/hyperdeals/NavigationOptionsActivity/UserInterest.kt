package com.example.kent.hyperdeals.NavigationOptionsActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.PromoModel
import com.example.kent.hyperdeals.Model.userLikeParce
import com.example.kent.hyperdeals.Model.userPromoLikedParce
import com.example.kent.hyperdeals.MyAdapters.HottestPromoAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_interest.*
import org.jetbrains.anko.doAsync

class UserInterest : AppCompatActivity() {
var database = FirebaseFirestore.getInstance()
    val TAG = "UserInterest"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_interest)
        getUserLikes()

    }

    fun getUserLikes(){
        var userLikes = ArrayList<String>()
        doAsync {
            database.collection("UserLikes").document(LoginActivity.userUIDS).collection("Promo").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {
                        var userLikedPromo = DocumentSnapshot.toObject(userPromoLikedParce::class.java)
                        Log.e(TAG, userLikedPromo.storeName)
                        userLikes.add(userLikedPromo.storeName)

                    }
                    getUserLikesObject(userLikes)

                }

            }
        }
    }

    fun getUserLikesObject (userLikes:ArrayList<String>) {
        var promolist = ArrayList<PromoModel>()


        for (j in 0 until userLikes.size) {
            for (i in 0 until FragmentCategory.globalPromoList.size) {

                if(userLikes[j].equals(FragmentCategory.globalPromoList[i].promoStore)){

                    promolist.add(FragmentCategory.globalPromoList[i])
                }

            }

        }

        var myAdapter = HottestPromoAdapter(this,promolist)
        RecyclerInterested.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        RecyclerInterested.adapter = myAdapter




    }
}
