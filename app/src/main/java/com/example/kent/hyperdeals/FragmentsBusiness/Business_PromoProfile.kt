package com.example.kent.hyperdeals.FragmentsBusiness

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
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
        userViewedCategory(PromoListAdapter.promoProfile)
        userViewedSubcategory(PromoListAdapter.promoProfile)


        val viewPager   = findViewById<ViewPager>(R.id.viewPagerBusinessman)
        val tabLayout   = findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.setupWithViewPager(viewPager)
        val adapter = com.example.kent.hyperdeals.MyAdapters.PagerAdapter(supportFragmentManager)
        adapter.addFragments(FragmentPrompDetailsBusiness(),"Details")
        adapter.addFragments(FragmentPromoSaleBusiness(),"Items")

        if(MainActivity.userLog){

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
        Log.e(TAG,"userViewedSubcategory")

        for (i in 0 until myPromo.subcategories.size) {
            doAsync {
                var prevCount = 0

                database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).get().addOnCompleteListener {   task ->

                if (task.isSuccessful) {

                    val document = task.result
                    if(document.exists()) {

                        var mySubcategoryPref = document.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                        prevCount = mySubcategoryPref.viewCount + 1
                        Log.e(TAG, "Cached document data: ${document?.data}")

                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], prevCount)
                        database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG, "UserViewedPreferences Success")
                        }
                    }
                    else{
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i],1)
                        database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG,"UserViewedPreferences Success")
                        }
                    }

                } else {
                    Log.e(TAG, "Cached get failed: ", task.exception)
                    var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i],1)
                    database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                        Log.e(TAG,"UserViewedPreferences Success")
                    }
                }

            }


        }
        }

    }
    fun userViewedCategory(myPromo:PromoModel) {


        Log.e(TAG,"userViewedSubcategory")

        myPromo.promoCategories.toSet()
        for (i in 0 until myPromo.categories.size) {
            doAsync {
                var prevCount = 0

                database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).get().addOnCompleteListener {   task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if(document.exists()) {
                            var myCategoryPref = document.toObject(UserCategoriesPreferencesParcelabl::class.java)
                            prevCount = myCategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserCategoriesPreferences(myPromo.categories[i], prevCount)
                            database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserViewedPreferences Success")
                            }
                        }
                        else{
                            Log.e(TAG, "Initializing Document ", task.exception)
                            var mySubcategoryPreference = UserCategoriesPreferences(myPromo.categories[i],1)
                            database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).set(mySubcategoryPreference).addOnCompleteListener {
                                Log.e(TAG,"UserViewedPreferences Success")
                            }
                        }
                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserCategoriesPreferences(myPromo.categories[i],1)
                        database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).set(mySubcategoryPreference).addOnCompleteListener {
                            Log.e(TAG,"UserViewedPreferences Success")
                        }
                    }

                }


            }
        }

    }
    fun userLikedSubcategory(myPromo:PromoModel) {
        Log.e(TAG,"userViewedSubcategory")

        for (i in 0 until myPromo.subcategories.size) {
            doAsync {
                var prevCount = 0

                database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).get().addOnCompleteListener {   task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if(document.exists()) {

                            var mySubcategoryPref = document.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            prevCount = mySubcategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i], prevCount)
                            database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserLikedPreferences Success")
                            }
                        }
                        else{
                            Log.e(TAG, "Cached get failed: ", task.exception)
                            var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i],1)
                            database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG,"UserLikedPreferences Success")
                            }
                        }

                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserSubcategoriesPreferences(myPromo.subcategories[i],1)
                        database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").document(myPromo.subcategories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                            Log.e(TAG,"UserLikedPreferences Success")
                        }
                    }

                }


            }
        }

    }
    fun userLikedCategory(myPromo:PromoModel){


        Log.e(TAG,"userViewedSubcategory")

        myPromo.promoCategories.toSet()
        for (i in 0 until myPromo.categories.size) {
            doAsync {
                var prevCount = 0

                database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).get().addOnCompleteListener {   task ->

                    if (task.isSuccessful) {

                        val document = task.result
                        if(document.exists()) {
                            var myCategoryPref = document.toObject(UserCategoriesPreferencesParcelabl::class.java)
                            prevCount = myCategoryPref.viewCount + 1
                            Log.e(TAG, "Cached document data: ${document?.data}")

                            var mySubcategoryPreference = UserCategoriesPreferences(myPromo.categories[i], prevCount)
                            database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).set(mySubcategoryPreference).addOnCompleteListener {


                                Log.e(TAG, "UserLikedPreferences Success")
                            }
                        }
                        else{
                            Log.e(TAG, "Initializing Document ", task.exception)
                            var mySubcategoryPreference = UserCategoriesPreferences(myPromo.categories[i],1)
                            database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).set(mySubcategoryPreference).addOnCompleteListener {
                                Log.e(TAG,"UserLikedPreferences Success")
                            }
                        }
                    } else {
                        Log.e(TAG, "Cached get failed: ", task.exception)
                        var mySubcategoryPreference = UserCategoriesPreferences(myPromo.categories[i],1)
                        database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Categories").document(myPromo.categories[i]).set(mySubcategoryPreference).addOnCompleteListener {
                            Log.e(TAG,"UserLikedPreferences Success")
                        }
                    }

                }


            }
        }

    }



}
