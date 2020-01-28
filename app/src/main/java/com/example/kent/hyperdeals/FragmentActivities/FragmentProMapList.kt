package com.example.kent.hyperdeals.FragmentActivities


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter

import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmentpromaplist.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

class FragmentProMapList: Fragment() {

val TAG = "FragmentProMapList"
    var executed = false
    var database = FirebaseFirestore.getInstance()
    lateinit var myPromoList:ArrayList<PromoModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragmentpromaplist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe.setOnRefreshListener {


        }
        tvClearPreference.setOnClickListener {

            database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).delete().addOnSuccessListener {
            Log.e(TAG,"Success deleted cached for ${LoginActivity.userUIDS}")

                   toast("Success deleting cached")



            }
                    .addOnFailureListener {

                        Log.e(TAG,"Fail deleted cached")
                        toast("Fail deleting cached")

                    }

        }


        for(i in 0 until 100000){
            Log.e(TAG,"yawa pisti")
            if(FragmentCategory.globalPromoList.size>0 ){


                myPromoList=FragmentCategory.globalPromoList
                Loopthelist(myPromoList)
                 Log.e(TAG,"yawa")
                if(executed){
                    Log.e(TAG,"Save Instance State")
                }
                else {
                    executed = true
                    Log.e(TAG,"delivered ${FragmentCategory.promoDistanceSorted.size} ${FragmentCategory.promoMatchedSorted.size} ")
                }
                break
            }


        }





    }


    fun Loopthelist (myPromoList:ArrayList<PromoModel>){
        for(i in 0 until myPromoList.size){

            Log.e(TAG,"${myPromoList[i].promoname} ${myPromoList[i].preferenceMatched} ")


        }


    }




    fun getUserViewedPreference(){
        var userViewedSubcategoryParceList= ArrayList<UserSubcategoriesPreferencesParcelable>()
        doAsync {

            database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {
                        var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                        userViewedSubcategoryParceList.add(subcategoriesParce)

                    }
                    Log.e(TAG,"UserViewedPreferencesSubcategory ${userViewedSubcategoryParceList.size}")

                }

            }


        }
}




    override fun onPause() {
        Log.e(TAG,"onPause")
        super.onPause()
    }

    override fun onDestroy() {
        Log.e(TAG,"onDestroy")

        super.onDestroy()
    }

    override fun onAttach(context: Context?) {
        Log.e(TAG,"onAttach")

        super.onAttach(context)

    }

    override fun onDetach() {
        Log.e(TAG,"onDetach")

        super.onDetach()
    }

    override fun onStop() {
        Log.e(TAG,"onStop")

        super.onStop()
    }

    override fun onResume() {
        Log.e(TAG,"onResume")

        super.onResume()
    }


    }
