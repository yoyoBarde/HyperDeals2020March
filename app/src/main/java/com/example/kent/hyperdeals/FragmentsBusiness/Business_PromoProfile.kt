package com.example.kent.hyperdeals.FragmentsBusiness

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.kent.hyperdeals.Admin.DataControl
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.MainActivity
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_business__promo_profile.*
import org.jetbrains.anko.doAsync
import java.lang.Exception

class Business_PromoProfile : AppCompatActivity() {
val TAG = "Business_PromoProfile"
    var database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e(TAG,"BUsinessPromasdomoadsmodsad")

        setContentView(R.layout.activity_business__promo_profile)
        try {
            var myNotificationID = intent.getIntExtra("notificationID",0)

            Log.e(TAG,"$myNotificationID")
            Log.e(TAG,intent.getStringExtra("promoID"))
            var myPromo =   intent.getParcelableExtra<PromoModel>("message")
            var myPromoID = intent.dataString
            Log.e(TAG,"${myPromoID}")
            var atay = intent.getParcelableExtra<PromoModel>("object")
            Log.e(TAG, "get intent ${atay.promoStore}")

            PromoListAdapter.promoProfile = atay
        }
        catch (e:Exception){
            Log.e(TAG,e.toString())
            print(e)
        }

        buttonBack.setOnClickListener { finish()}
        Picasso.get()
                .load(PromoListAdapter.promoProfile.promoImageLink)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(iv_promo_image)


        val viewPager   = findViewById<ViewPager>(R.id.viewPagerBusinessman)
        val tabLayout   = findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.setupWithViewPager(viewPager)
        val adapter = com.example.kent.hyperdeals.MyAdapters.PagerAdapter(supportFragmentManager)
        adapter.addFragments(FragmentPrompDetailsBusiness(),"Details")
        adapter.addFragments(FragmentPromoSaleBusiness(),"Items")

        if(MainActivity.userLog){
            userViewedSubcategory(PromoListAdapter.promoProfile)

            adapter.addFragments(FragmentRelatedPromos(),"Similar Promos")

        }
        else{
            adapter.addFragments(FragmentDashboardBusiness(),"Dashboard")

        }
        viewPager.adapter = adapter

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.e(TAG," yawaaaaa")
        Log.e(TAG,"resultCode $resultCode "+data!!.getStringExtra("key"))

        super.onActivityResult(requestCode, resultCode, data)


    }



    fun userViewedSubcategory(myPromo:PromoModel) {
        Log.e(TAG, "userViewedSubcategory")

            database.collection("PromoData").document("PromoViews").collection("Promos").document(myPromo.promoID).collection("Users").document().set(FragmentCategory.globalUserDemography).addOnSuccessListener {
                Log.e(TAG, "Store is fucking satored")

            }


        for (i in 0 until myPromo.subcategories.size) {
            doAsync {
                var prevCount = 0

                database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).get().addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if (document.exists()) {

                            var mySubcategoryPref = document.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            prevCount = mySubcategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], prevCount)
                            database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserViewedPreferences Success")
                            }
                        } else {
                            Log.e(TAG, "Cached get failed: ", task.exception)
                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                            database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserViewedPreferences Success")
                            }
                        }

                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                        database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG, "UserViewedPreferences Success")
                        }
                    }

                }

            }
        }
    }
    fun userLikedSubcategory(myPromo:PromoModel) {
        Log.e(TAG, "userViewedSubcategory")
        database.collection("PromoData").document("PromoLikes").collection("Promos").document(myPromo.promoID).collection("Users").document().set(FragmentCategory.globalUserDemography).addOnSuccessListener {
            Log.e(TAG, "Store is fucking satored")

        }
        for (i in 0 until myPromo.subcategories.size) {
            doAsync {

                var prevCountLiked = 0

                database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).get().addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if (document.exists()) {

                            var mySubcategoryPref = document.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            prevCountLiked = mySubcategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], prevCountLiked)
                            database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserLikedPreferences Success")
                            }
                        } else {
                            Log.e(TAG, "Cached get failed: ", task.exception)
                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                            database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserLikedPreferences Success")
                            }
                        }

                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                        database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG, "UserLikedPreferences Success")
                        }
                    }

                }




            }
        }
    }
    fun userAvailedSubcategory(myPromo:PromoModel) {
        Log.e(TAG, "userViewedSubcategory")
        database.collection("PromoData").document("PromoAvailed").collection("Promos").document(myPromo.promoID).collection("Users").document().set(FragmentCategory.globalUserDemography).addOnSuccessListener {
            Log.e(TAG, "Store is fucking satored")

        }
        for (i in 0 until myPromo.subcategories.size) {
            doAsync {

                var prevCountAvailed = 0
                database.collection("UserAvailedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).get().addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if (document.exists()) {

                            var mySubcategoryPref = document.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            prevCountAvailed = mySubcategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], prevCountAvailed)
                            database.collection("UserAvailedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserAvailedPreferences Success")
                            }
                        } else {
                            Log.e(TAG, "Cached get failed: ", task.exception)
                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                            database.collection("UserAvailedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserAvailedPreferences Success")
                            }
                        }

                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                        database.collection("UserAvailedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG, "UserAvailedPreferences Success")
                        }
                    }

                }

            }
        }
    }
    fun userPreferredSubcategory(myPromo:PromoModel) {
        database.collection("PromoData").document("PromoPreferred").collection("Promos").document(myPromo.promoID).collection("Users").document().set(FragmentCategory.globalUserDemography).addOnSuccessListener {
            Log.e(TAG, "Store is fucking satored")

        }
        Log.e(TAG, "userViewedSubcategory")

        for (i in 0 until myPromo.subcategories.size) {
            doAsync {


                var prevCountPreferred = 0

                database.collection("UserPreferredPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).get().addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if (document.exists()) {

                            var mySubcategoryPref = document.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            prevCountPreferred = mySubcategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], prevCountPreferred)
                            database.collection("UserPreferredPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserPreferredPreferences Success")
                            }
                        } else {
                            Log.e(TAG, "Cached get failed: ", task.exception)
                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                            database.collection("UserPreferredPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserPreferredPreferences Success")
                            }
                        }

                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], 1)
                        database.collection("UserPreferredPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG, "UserPreferredPreferences Success")
                        }
                    }

                }


            }
        }
    }
    fun userDismissedSubcategory(myPromo:PromoModel) {
        Log.e(TAG, "userViewedSubcategory")
        database.collection("PromoData").document("PromoDismissed").collection("Promos").document(myPromo.promoID).collection("Users").document().set(FragmentCategory.globalUserDemography).addOnSuccessListener {
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
