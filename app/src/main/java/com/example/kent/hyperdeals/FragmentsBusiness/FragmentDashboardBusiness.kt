package com.example.kent.hyperdeals.FragmentsBusiness

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_data_control.*
import kotlinx.android.synthetic.main.fragment_fragment_dashboard_business.*
import kotlinx.android.synthetic.main.fragmentpromaplist.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class FragmentDashboardBusiness : Fragment() {
    val TAG = "fragmentBusiness"
    var database = FirebaseFirestore.getInstance()
    var userViews = ArrayList<UserModelParce>()
    var userLikes = ArrayList<UserModelParce>()
    var userNotified = ArrayList <UserModelParce>()
    var userDismissed = ArrayList <UserModelParce>()
    var executed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_dashboard_business, container, false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     businessman_demography_age.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            filterDemography()


            }
        }

        businessman_demography_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            filterDemography()

            }
        }

        businessman_demography_status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        filterDemography()

            }
        }



        var ageAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.promo_demography_age,android.R.layout.simple_spinner_item)
        var genderAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.promo_demography_gender,android.R.layout.simple_spinner_item)
        var statusAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.promo_demography_status,android.R.layout.simple_spinner_item)

        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        businessman_demography_age.adapter = ageAdapter
        businessman_demography_gender.adapter = genderAdapter
        businessman_demography_status.adapter = statusAdapter

        var mypromo = PromoListAdapter.promoProfile
        getDemography(mypromo)


    }
    fun getDemography (mypromo:PromoModel){

Log.e(TAG,"Retrieve ${mypromo.promoname}    ${mypromo.promoID}")
doAsync {
    database.collection("PromoData").document("PromoViews").collection("Promos").document(mypromo.promoID).collection("Users").get().addOnCompleteListener { task ->
        if (task.isSuccessful) {

            for (DocumentSnapshot in task.result) {
                var UserView = DocumentSnapshot.toObject(UserModelParce::class.java)

                userViews.add(UserView)
            }
            Log.e(TAG,"${userViews.size} user view retrieve")

        } else {
            Log.e(TAG, "wala ma add")
        }

        database.collection("PromoData").document("PromoLikes").collection("Promos").document(mypromo.promoID).collection("Users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                for (DocumentSnapshot in task.result) {
                    var UserLikes = DocumentSnapshot.toObject(UserModelParce::class.java)

                    userLikes.add(UserLikes)

                }
                Log.e(TAG,"${userLikes.size} user like retrieve")

            } else
                Log.e(TAG, "wala ma add")

            database.collection("PromoData").document("PromoAvailed").collection("Promos").document(mypromo.promoID).collection("Users").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    for (DocumentSnapshot in task.result) {
                        var UserAvailed = DocumentSnapshot.toObject(UserModelParce::class.java)

                        userNotified.add(UserAvailed)

                    }
                    Log.e(TAG,"${userNotified.size} user notified retrieve")

                    //     Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user availed -  ${myPromoList[i].userListAvailed.size} ")

                } else
                    Log.e(TAG, "wala ma add")

                database.collection("PromoData").document("PromoDismissed").collection("Promos").document(mypromo.promoID).collection("Users").get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        for (DocumentSnapshot in task.result) {
                            var PromoDismissed = DocumentSnapshot.toObject(UserModelParce::class.java)

                            userDismissed.add(PromoDismissed)

                        }
                        Log.e(TAG,"${userDismissed.size} user dismissed retrieve")
                        //    Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user dismissed -  ${myPromoList[i].userListDismissed.size} ")
                    } else
                        Log.e(TAG, "wala ma add")

                    executed = true
                    Log.e(TAG,"userView - ${userViews.size}  userLikes - ${userLikes.size} userNotified - ${userNotified.size} - userDismissed - ${userDismissed.size}")
                    uiThread {
                        filterDemography()
                    }
                }


            }


        }


    }

}



        }

    fun filterDemography(){
        var myuserViews = ArrayList<UserModelParce>()
        var myuserLikes = ArrayList<UserModelParce>()
        var myuserNotified = ArrayList <UserModelParce>()
        var myuserDismissed = ArrayList <UserModelParce>()

        var myuserViews1 = ArrayList<UserModelParce>()
        var myuserLikes1= ArrayList<UserModelParce>()
        var myuserNotified1= ArrayList <UserModelParce>()
        var myuserDismissed1 = ArrayList <UserModelParce>()

        var myuserViews2 = ArrayList<UserModelParce>()
        var myuserLikes2= ArrayList<UserModelParce>()
        var myuserNotified2 = ArrayList <UserModelParce>()
        var myuserDismissed2 = ArrayList <UserModelParce>()

                if( businessman_demography_age.selectedItem.toString().equals("All")){
            for(i in 0 until userViews.size){

                myuserViews.add(userViews[i])
            }
            for(i in 0 until userLikes.size){

                myuserLikes.add(userLikes[i])
            }
            for(i in 0 until userNotified.size){

                myuserNotified.add(userNotified[i])
            }
            for(i in 0 until userDismissed.size){

                myuserDismissed.add(userDismissed[i])
            }

        }
        else if( businessman_demography_age.selectedItem.toString().equals("Adult")){
            for(i in 0 until userViews.size){

                if(userViews[i].Age.equals("Adult")) {
                    myuserViews.add(userViews[i])
                }
            }
            for(i in 0 until userLikes.size){

                if(userLikes[i].Age.equals("Adult")) {
                    myuserLikes.add(userLikes[i])
                }
            }
            for(i in 0 until userNotified.size){

                if(userNotified[i].Age.equals("Adult")){
                    myuserNotified.add(userNotified[i])
                }
            }
            for(i in 0 until userDismissed.size){

                if(userDismissed[i].Age.equals("Adult")) {
                    myuserDismissed.add(userDismissed[i])
                }
            }


        }
        else if( businessman_demography_age.selectedItem.toString().equals("Teenager")){
            for(i in 0 until userViews.size){

                if(userViews[i].Age.equals("Teenager")) {
                    myuserViews.add(userViews[i])
                }
            }
            for(i in 0 until userLikes.size){

                if(userLikes[i].Age.equals("Teenager")) {
                    myuserLikes.add(userLikes[i])
                }
            }
            for(i in 0 until userNotified.size){

                if(userNotified[i].Age.equals("Teenager")){
                    myuserNotified.add(userNotified[i])
                }
            }
            for(i in 0 until userDismissed.size){

                if(userDismissed[i].Age.equals("Teenager")) {
                    myuserDismissed.add(userDismissed[i])
                }
            }

        }
        else if( businessman_demography_age.selectedItem.toString().equals("Kids")){
            for(i in 0 until userViews.size){

                if(userViews[i].Age.equals("Kids")) {
                    myuserViews.add(userViews[i])
                }
            }
            for(i in 0 until userLikes.size){

                if(userLikes[i].Age.equals("Kids")) {
                    myuserLikes.add(userLikes[i])
                }
            }
            for(i in 0 until userNotified.size){

                if(userNotified[i].Age.equals("Kids")){
                    myuserNotified.add(userNotified[i])
                }
            }
            for(i in 0 until userDismissed.size){

                if(userDismissed[i].Age.equals("Kids")) {
                    myuserDismissed.add(userDismissed[i])
                }
            }

        }


        if( businessman_demography_gender.selectedItem.toString().equals("Male/Female")){
            for(i in 0 until myuserViews.size){

                myuserViews1.add(myuserViews[i])


            }
            for(i in 0 until myuserLikes.size){

                myuserLikes1.add(myuserLikes[i])


            }
            for(i in 0 until myuserNotified.size){

                myuserNotified1.add(myuserNotified[i])


            }
            for(i in 0 until myuserDismissed.size){

                myuserDismissed1.add(myuserDismissed[i])


            }


        }
        else if( businessman_demography_gender.selectedItem.toString().equals("Male")) {
            for (i in 0 until myuserViews.size) {

                if (myuserViews[i].Gender.equals("Male")) {
                    myuserViews1.add(myuserViews[i])
                }
            }
            for (i in 0 until myuserLikes.size) {

                if (myuserLikes[i].Gender.equals("Male")) {
                    myuserLikes1.add(myuserLikes[i])
                }
            }
            for (i in 0 until myuserNotified.size) {

                if (myuserNotified[i].Gender.equals("Male")) {
                    myuserNotified1.add(myuserNotified[i])
                }
            }
            for (i in 0 until myuserDismissed.size) {

                if (myuserDismissed[i].Gender.equals("Male")) {
                    myuserDismissed1.add(myuserDismissed[i])
                }

            }
        }
        else if( businessman_demography_gender.selectedItem.toString().equals("Female")) {
            for (i in 0 until myuserViews.size) {

                if (myuserViews[i].Gender.equals("Female")) {
                    myuserViews1.add(myuserViews[i])
                }
            }
            for (i in 0 until myuserLikes.size) {

                if (myuserLikes[i].Gender.equals("Female")) {
                    myuserLikes1.add(myuserLikes[i])
                }
            }
            for (i in 0 until myuserNotified.size) {

                if (myuserNotified[i].Gender.equals("Female")) {
                    myuserNotified1.add(myuserNotified[i])
                }
            }
            for (i in 0 until myuserDismissed.size) {

                if (myuserDismissed[i].Gender.equals("Female")) {
                    myuserDismissed1.add(myuserDismissed[i])
                }

            }
        }


        if( businessman_demography_status.selectedItem.toString().equals("Single/Married")){

            for(i in 0 until myuserViews1.size){

                myuserViews2.add(myuserViews1[i])


            }

            for(i in 0 until myuserLikes1.size){

                myuserLikes2.add(myuserLikes1[i])


            }
            for(i in 0 until myuserNotified1.size){

                myuserNotified2.add(myuserNotified1[i])


            }
            for(i in 0 until myuserDismissed1.size){

                myuserDismissed2.add(myuserDismissed1[i])


            }
        }
        else if( businessman_demography_status.selectedItem.toString().equals("Single")){

            for(i in 0 until myuserViews1.size){

                if(myuserViews1[i].Status.equals("Single")) {
                    myuserViews2.add(myuserViews1[i])
                }
            }
            for(i in 0 until myuserLikes1.size){

                if(myuserLikes1[i].Status.equals("Single")) {
                    myuserLikes2.add(myuserLikes1[i])
                }
            }
            for(i in 0 until myuserNotified1.size){

                if(myuserNotified1[i].Status.equals("Single")) {
                    myuserNotified2.add(myuserNotified1[i])
                }
            }
            for(i in 0 until myuserDismissed1.size){

                if(myuserDismissed1[i].Status.equals("Single")) {
                    myuserDismissed2.add(myuserDismissed1[i])
                }
            }

        }
        else if( businessman_demography_status.selectedItem.toString().equals("Married")){

            for(i in 0 until myuserViews1.size){

                if(myuserViews1[i].Status.equals("Married")) {
                    myuserViews2.add(myuserViews1[i])
                }
            }
            for(i in 0 until myuserLikes1.size){

                if(myuserLikes1[i].Status.equals("Married")) {
                    myuserLikes2.add(myuserLikes1[i])
                }
            }
            for(i in 0 until myuserNotified1.size){

                if(myuserNotified1[i].Status.equals("Married")) {
                    myuserNotified2.add(myuserNotified1[i])
                }
            }
            for(i in 0 until myuserDismissed1.size){

                if(myuserDismissed1[i].Status.equals("Married")) {
                    myuserDismissed2.add(myuserDismissed1[i])
                }
            }
        }



        businessman_views_count.text = myuserViews2.size.toString()
        businessan_likes_count.text  = myuserLikes2.size.toString()
        businessan_notified_count.text = myuserNotified2.size.toString()
        businessan_dismissed_count.text = myuserDismissed2.size.toString()



    }
    }

