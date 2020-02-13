package com.example.kent.hyperdeals.FragmentActivities


import android.content.Context
import android.os.AsyncTask
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter

import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragmentpromaplist.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

/*

onViewCreated() - >
Loopthelist(myPromoList) - >
getUserSubcategoryActivities(myPromoList) - >




 */
class FragmentProMapList: Fragment() {

val TAG = "FragmentProMapList"
    var executed = false
    var database = FirebaseFirestore.getInstance()
    lateinit var myPromoLister:ArrayList<PromoModel>

    var viewCriteria = 1.2
    var likeCriteria = 1.4
    var preferenceCriteria = 1.5
    var availedCriteria = 1.6
    var dismissedCriteria = -1.3

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragmentpromaplist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        var filterAdapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.filter_recommendation,android.R.layout.simple_spinner_item)


        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filter_spinner.adapter = filterAdapter

        filter_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                if( filter_spinner.selectedItem.toString().equals("Recommended")){

                    Log.e(TAG,"Recommended")
                    if(executed)
                    sortFinalRecommendedList(FragmentCategory.globalPromoList,0)
                }


                else if( filter_spinner.selectedItem.toString().equals("Distance")){

                    Log.e(TAG,"Distance")
                    sortFinalRecommendedListDistance(FragmentCategory.globalPromoList,1)



                }
                else if(filter_spinner.selectedItem.toString().equals("Preferred")){
                    Log.e(TAG,"Preferred")
                    sortFinalRecommendedListPreferred(FragmentCategory.globalPromoList,2)


                }


        }
            }
        swipe.setOnRefreshListener {
            Loopthelist(FragmentCategory.globalPromoList)
            filter_spinner.setSelection(0)
        }
        for(i in 0 until 100000){
            if(FragmentCategory.globalPromoList.size>0 ){


                Loopthelist(FragmentCategory.globalPromoList)
                if(executed){
                    Log.e(TAG,"Save Instance State")
                }
                else {
                    Log.e(TAG,"delivered ${FragmentCategory.promoDistanceSorted.size} ${FragmentCategory.promoMatchedSorted.size} ")
                }
                break
            }


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

            database.collection("PromoData").document("PromoViews").delete().addOnSuccessListener {
                Log.e(TAG,"PromoViews Deleted")
            }
            database.collection("PromoData").document("PromoLikes").delete().addOnSuccessListener {   Log.e(TAG,"PromoLikes Deleted") }
            database.collection("PromoData").document("PromoPreferred").delete().addOnSuccessListener { Log.e(TAG,"PromoPreferred Delete") }
            database.collection("PromoData").document("PromoAvailed").delete().addOnSuccessListener {Log.e(TAG,"PromoAvailed Deleted")  }
            database.collection("PromoData").document("PromoDismissed").delete().addOnSuccessListener {Log.e(TAG,"PromoDismissed Deleted")  }




            database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).delete().addOnSuccessListener { Log.e(TAG," Userview deleted") }
            database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).delete().addOnSuccessListener { Log.e(TAG," userlike deleted") }
            database.collection("UserPreferredPreferences").document(LoginActivity.userUIDS).delete().addOnSuccessListener { Log.e(TAG," userPref deleted") }
            database.collection("UserAvailedPreferences").document(LoginActivity.userUIDS).delete().addOnSuccessListener { Log.e(TAG," userAvailedDeleted") }
            database.collection("UserDismissedPreferencesSubcategory").document(LoginActivity.userUIDS).delete().addOnSuccessListener { Log.e(TAG," userDismissed Deleted") }
        }


    }


    fun Loopthelist (myPromoList:ArrayList<PromoModel>) {

            for (i in 0 until myPromoList.size) {
                doAsync {


                   // Log.e(TAG, "${myPromoList[i].promoname} ${myPromoList[i].preferenceMatched} ${myPromoList[i].subcategories.size}  ${myPromoList[i].promoID}")

                    database.collection("PromoData").document("PromoViews").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            myPromoList[i].userListView = ArrayList<UserModelParce>()
                            myPromoList[i].genderMatchViews = 0
                            myPromoList[i].ageMatchViews = 0
                            myPromoList[i].statusMatchViews = 0
                            for (DocumentSnapshot in task.result) {
                                var UserView = DocumentSnapshot.toObject(UserModelParce::class.java)



                                if (UserView.Gender.matches(FragmentCategory.globalUserDemography.Gender.toRegex()))
                                    myPromoList[i].genderMatchViews = myPromoList[i].genderMatchViews + 1
                                if (UserView.Age.matches(FragmentCategory.globalUserDemography.Age.toRegex()))
                                    myPromoList[i].ageMatchViews = myPromoList[i].ageMatchViews + 1
                                if (UserView.Status.matches(FragmentCategory.globalUserDemography.Status.toRegex()))
                                    myPromoList[i].statusMatchViews = myPromoList[i].statusMatchViews + 1




                                myPromoList[i].userListView.add(UserView)

                            }
                         //   Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user views -  ${myPromoList[i].userListView.size} ")


                        } else {
                            Log.e(TAG, "LooptheList task failed")
                        }


                    }

                    database.collection("PromoData").document("PromoLikes").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            myPromoList[i].userListLikes = ArrayList<UserModelParce>()

                            myPromoList[i].genderMatchLikes = 0
                            myPromoList[i].ageMatchLikes = 0
                            myPromoList[i].statusMatchLikes = 0

                            for (DocumentSnapshot in task.result) {
                                var UserLikes = DocumentSnapshot.toObject(UserModelParce::class.java)

                                if (UserLikes.Gender.matches(FragmentCategory.globalUserDemography.Gender.toRegex()))
                                    myPromoList[i].genderMatchLikes = myPromoList[i].genderMatchLikes + 1
                                if (UserLikes.Age.matches(FragmentCategory.globalUserDemography.Age.toRegex()))
                                    myPromoList[i].ageMatchLikes = myPromoList[i].ageMatchLikes + 1
                                if (UserLikes.Status.matches(FragmentCategory.globalUserDemography.Status.toRegex()))
                                    myPromoList[i].statusMatchLikes = myPromoList[i].statusMatchLikes + 1


                                myPromoList[i].userListLikes.add(UserLikes)

                            }
                           // Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user likes -  ${myPromoList[i].userListLikes.size} ")

                        }


                    }

                    database.collection("PromoData").document("PromoPreferred").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            myPromoList[i].userListPreferred = ArrayList<UserModelParce>()

                            myPromoList[i].genderMatchPreferred = 0
                            myPromoList[i].ageMatchPreferred = 0
                            myPromoList[i].statusMatchPreferred = 0

                            for (DocumentSnapshot in task.result) {

                                var UserPreferred = DocumentSnapshot.toObject(UserModelParce::class.java)


                                if (UserPreferred.Gender.matches(FragmentCategory.globalUserDemography.Gender.toRegex()))
                                    myPromoList[i].genderMatchPreferred = myPromoList[i].genderMatchPreferred + 1
                                if (UserPreferred.Age.matches(FragmentCategory.globalUserDemography.Age.toRegex()))
                                    myPromoList[i].ageMatchPreferred = myPromoList[i].ageMatchPreferred + 1
                                if (UserPreferred.Status.matches(FragmentCategory.globalUserDemography.Status.toRegex()))
                                    myPromoList[i].statusMatchPreferred = myPromoList[i].statusMatchPreferred + 1




                                myPromoList[i].userListPreferred.add(UserPreferred)

                            }
                          //  Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user preferred -  ${myPromoList[i].userListPreferred.size} ")

                        }


                    }

                    database.collection("PromoData").document("PromoAvailed").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            myPromoList[i].userListAvailed = ArrayList<UserModelParce>()
                            myPromoList[i].genderMatchAvailed = 0
                            myPromoList[i].ageMatchAvailed = 0
                            myPromoList[i].statusMatchAvailed = 0
                            for (DocumentSnapshot in task.result) {
                                var UserAvailed = DocumentSnapshot.toObject(UserModelParce::class.java)

                                if (UserAvailed.Gender.matches(FragmentCategory.globalUserDemography.Gender.toRegex()))
                                    myPromoList[i].genderMatchAvailed = myPromoList[i].genderMatchAvailed + 1
                                if (UserAvailed.Age.matches(FragmentCategory.globalUserDemography.Age.toRegex()))
                                    myPromoList[i].ageMatchAvailed = myPromoList[i].ageMatchAvailed + 1
                                if (UserAvailed.Status.matches(FragmentCategory.globalUserDemography.Status.toRegex()))
                                    myPromoList[i].statusMatchAvailed = myPromoList[i].statusMatchAvailed + 1

                                myPromoList[i].userListAvailed.add(UserAvailed)

                            }
                       //     Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user availed -  ${myPromoList[i].userListAvailed.size} ")

                        }


                    }

                    database.collection("PromoData").document("PromoDismissed").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            myPromoList[i].userListDismissed = ArrayList<UserModelParce>()
                            myPromoList[i].genderMatchDismissed = 0
                            myPromoList[i].ageMatchDismissed = 0
                            myPromoList[i].statusMatchDismissed = 0
                            for (DocumentSnapshot in task.result) {
                                var PromoDismissed = DocumentSnapshot.toObject(UserModelParce::class.java)

                                if (PromoDismissed.Gender.matches(FragmentCategory.globalUserDemography.Gender.toRegex()))
                                    myPromoList[i].genderMatchDismissed = myPromoList[i].genderMatchDismissed + 1
                                if (PromoDismissed.Age.matches(FragmentCategory.globalUserDemography.Age.toRegex()))
                                    myPromoList[i].ageMatchDismissed = myPromoList[i].ageMatchDismissed + 1
                                if (PromoDismissed.Status.matches(FragmentCategory.globalUserDemography.Status.toRegex()))
                                    myPromoList[i].statusMatchDismissed = myPromoList[i].statusMatchDismissed + 1


                                myPromoList[i].userListDismissed.add(PromoDismissed)

                            }
                        //    Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user dismissed -  ${myPromoList[i].userListDismissed.size} ")

                        }


                    }

                }

            }


        getUserSubcategoryActivities(myPromoList)


    }


    fun getUserSubcategoryActivities(myPromoList:ArrayList<PromoModel>){

        Log.e(TAG,"getUserSubcategoryActivities Start")
       lateinit var userViewedSubcategoryParceList:ArrayList<UserSubcategoriesPreferencesParcelable>
       lateinit var userLikedSubcategoryParceList: ArrayList<UserSubcategoriesPreferencesParcelable>
       lateinit var userPreferredSubcategoryParceList: ArrayList<UserSubcategoriesPreferencesParcelable>
       lateinit var userAvailedSubcategoryParceList: ArrayList<UserSubcategoriesPreferencesParcelable>
       lateinit var userDismissedSubcategoryParceList:ArrayList<UserSubcategoriesPreferencesParcelable>

    database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
        userViewedSubcategoryParceList = ArrayList<UserSubcategoriesPreferencesParcelable>()
        if (task.isSuccessful) {
            for (DocumentSnapshot in task.result) {
                var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                userViewedSubcategoryParceList.add(subcategoriesParce)
            }

            Log.e(TAG, "UserViewedPreferencesSubcategory ${userViewedSubcategoryParceList.size}")

        }
        database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
            userLikedSubcategoryParceList = ArrayList<UserSubcategoriesPreferencesParcelable>()

            if (task.isSuccessful) {
                for (DocumentSnapshot in task.result) {
                    var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                    userLikedSubcategoryParceList.add(subcategoriesParce)

                }
                Log.e(TAG, "UserLikedPreferencesSubcategory ${userLikedSubcategoryParceList.size}")

            }
            database.collection("UserPreferredPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
                userPreferredSubcategoryParceList = ArrayList<UserSubcategoriesPreferencesParcelable>()

                if (task.isSuccessful) {

                    for (DocumentSnapshot in task.result) {

                        var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                        userPreferredSubcategoryParceList.add(subcategoriesParce)

                    }
                    Log.e(TAG, "UserPreferreddPreferencesSubcategory ${userPreferredSubcategoryParceList.size}")

                }
                database.collection("UserAvailedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
                    userAvailedSubcategoryParceList = ArrayList<UserSubcategoriesPreferencesParcelable>()


                    if (task.isSuccessful) {

                        for (DocumentSnapshot in task.result) {
                            var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            userAvailedSubcategoryParceList.add(subcategoriesParce)

                        }
                        Log.e(TAG, "UserAvailedPreferencesSubcategory ${userAvailedSubcategoryParceList.size}")

                    }
                    database.collection("UserDismissedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
                        userDismissedSubcategoryParceList = ArrayList<UserSubcategoriesPreferencesParcelable>()

                        if (task.isSuccessful) {
                            for (DocumentSnapshot in task.result) {
                                var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                                userDismissedSubcategoryParceList.add(subcategoriesParce)

                            }
                            Log.e(TAG, "UserDismissedPreferencesSubcategory ${userDismissedSubcategoryParceList.size}")

                        }
                        for( i in 0 until myPromoList.size){
                            myPromoList[i].subcategory_viewCount=0

                            for(j in 0 until myPromoList[i].subcategories.size) {


                                for(k in 0 until userViewedSubcategoryParceList.size){

                                    if(myPromoList[i].subcategories[j].equals(userViewedSubcategoryParceList[k].subCategoryName)){
                                        myPromoList[i].subcategory_viewCount= myPromoList[i].subcategory_viewCount + userViewedSubcategoryParceList[k].viewCount
                                    }
                                }
                                for(k in 0 until userLikedSubcategoryParceList.size){

                                    if(myPromoList[i].subcategories[j].equals(userLikedSubcategoryParceList[k].subCategoryName)){
                                        myPromoList[i].subcategory_likesCount= myPromoList[i].subcategory_likesCount + userLikedSubcategoryParceList[k].viewCount
                                    }
                                }
                                for(k in 0 until userPreferredSubcategoryParceList.size){

                                    if(myPromoList[i].subcategories[j].equals(userPreferredSubcategoryParceList[k].subCategoryName)){
                                        myPromoList[i].subcategory_preferenceCount= myPromoList[i].subcategory_preferenceCount + userPreferredSubcategoryParceList[k].viewCount
                                    }
                                }
                                for(k in 0 until userAvailedSubcategoryParceList.size){

                                    if(myPromoList[i].subcategories[j].equals(userAvailedSubcategoryParceList[k].subCategoryName)){
                                        myPromoList[i].subcategory_availedCount= myPromoList[i].subcategory_availedCount + userAvailedSubcategoryParceList[k].viewCount
                                    }
                                }
                                for(k in 0 until userDismissedSubcategoryParceList.size){

                                    if(myPromoList[i].subcategories[j].equals(userDismissedSubcategoryParceList[k].subCategoryName)){
                                        myPromoList[i].subcategory_dismissedCount= myPromoList[i].subcategory_dismissedCount + userDismissedSubcategoryParceList[k].viewCount
                                    }
                                }


                                myPromoList[i].subcategory_viewPoints = myPromoList[i].subcategory_viewCount * viewCriteria
                                myPromoList[i].subcategory_likesPoints = myPromoList[i].subcategory_likesCount * likeCriteria
                                myPromoList[i].subcategory_preferencePoints = myPromoList[i].preferenceMatched * preferenceCriteria
                                myPromoList[i].subcategory_availedPoints = myPromoList[i].subcategory_availedCount * availedCriteria
                                myPromoList[i].subcategory_dismissedPoints = myPromoList[i].subcategory_dismissedCount * dismissedCriteria

                                myPromoList[i].subcategory_totalPoints = myPromoList[i].subcategory_viewPoints + myPromoList[i].subcategory_likesPoints +  myPromoList[i].subcategory_preferencePoints + myPromoList[i].subcategory_availedPoints + myPromoList[i].subcategory_dismissedPoints


                            }


                        }
                        setScorePromoDemographies(myPromoList)
                        Log.e(TAG,"do Next Step Here line 334")


                    }

                }

            }

        }




        }








        Log.e(TAG,"getUserSubcategoryActivities End")




    }


    fun setScorePromoDemographies(myPromoList:ArrayList<PromoModel>){
        Log.e(TAG," promo name -  viewcountps - likecountpts - preferredcountpts - availedcountpts - dismissedcountpts - totalpts")
    for(i in 0 until myPromoList.size){
//
//        myPromoList[i].demography_viewPoints = myPromoList[i].userListView.size * viewCriteria
//        myPromoList[i].demography_likesPoints =myPromoList[i].userListLikes.size * likeCriteria
//        myPromoList[i].demography_preferencePoints = myPromoList[i].userListPreferred.size * preferenceCriteria
//        myPromoList[i].demography_availedPoints = myPromoList[i].userListAvailed.size * availedCriteria
//        myPromoList[i].demography_dismissedPoints = myPromoList[i].userListDismissed.size * dismissedCriteria

        myPromoList[i].demography_viewPoints = (myPromoList[i].genderMatchViews + myPromoList[i].ageMatchViews + myPromoList[i].statusMatchViews) * viewCriteria
        myPromoList[i].demography_likesPoints =(myPromoList[i].genderMatchLikes + myPromoList[i].ageMatchLikes + myPromoList[i].statusMatchLikes) * likeCriteria
        myPromoList[i].demography_preferencePoints = (myPromoList[i].genderMatchPreferred + myPromoList[i].ageMatchPreferred + myPromoList[i].statusMatchPreferred) * preferenceCriteria
        myPromoList[i].demography_availedPoints =(myPromoList[i].genderMatchAvailed + myPromoList[i].ageMatchAvailed + myPromoList[i].statusMatchAvailed) * availedCriteria
        myPromoList[i].demography_dismissedPoints =(myPromoList[i].genderMatchDismissed + myPromoList[i].ageMatchDismissed + myPromoList[i].statusMatchDismissed) * dismissedCriteria
        myPromoList[i].demography_totalPoints =  myPromoList[i].demography_viewPoints + myPromoList[i].demography_likesPoints + myPromoList[i].demography_preferencePoints +
                myPromoList[i].demography_availedPoints +  myPromoList[i].demography_dismissedPoints

        myPromoList[i].totalPoints = myPromoList[i].demography_totalPoints + myPromoList[i].subcategory_totalPoints
//        Log.e(TAG,"Demography Scoring")
//        Log.e(TAG,"  ${myPromoList[i].promoname}  " +
//                "${myPromoList[i].userListView.size} / ${myPromoList[i].demography_viewPoints} ~~~ " +
//                "${myPromoList[i].userListLikes.size} / ${myPromoList[i].demography_likesPoints} ~~~ "+
//                "${myPromoList[i].userListPreferred.size}  ${myPromoList[i].demography_preferencePoints} ~~~ "+
//                "${myPromoList[i].userListAvailed.size} / ${myPromoList[i].demography_availedPoints} ~~~ "+
//                "${myPromoList[i].userListDismissed.size} / ${myPromoList[i].demography_dismissedPoints} ~~~ " +
//                "${myPromoList[i].demography_totalPoints}~~~" +
//                "ID - ${myPromoList[i].promoID}")
//
//        Log.e(TAG,"Subcategory Scoring")
//        Log.e(TAG,"  ${myPromoList[i].promoname}  " +
//                "${myPromoList[i].subcategory_viewCount} / ${myPromoList[i].subcategory_viewPoints} ~~~  " +
//                "${myPromoList[i].subcategory_likesCount} / ${myPromoList[i].subcategory_likesPoints} ~~~ "+
//                "${myPromoList[i].subcategory_preferenceCount} / ${myPromoList[i].subcategory_preferencePoints} ~~~  "+
//                "${myPromoList[i].subcategory_availedCount} / ${myPromoList[i].subcategory_availedPoints} ~~~ "+
//                "${myPromoList[i].subcategory_dismissedCount} / ${myPromoList[i].subcategory_dismissedPoints} ~~~ " +
//                "${myPromoList[i].subcategory_totalPoints}~~~" +
//                "ID  ${myPromoList[i].promoID}")

        Log.e(TAG," user view and likes ${myPromoList[i].userListView.size} ${myPromoList[i].userListLikes.size}")
        Log.e(TAG,"${myPromoList[i].genderMatchViews} ${myPromoList[i].ageMatchViews} ${myPromoList[i].statusMatchViews}")



    }
        sortFinalRecommendedList(myPromoList,0)

    }


    fun sortFinalRecommendedList(myPromoList:ArrayList<PromoModel>,filter:Int){
        swipe.isRefreshing = false

        myPromoLister = myPromoList
        executed = true

        var finalsortedList = myPromoList.sortedWith(compareByDescending {it.totalPoints})


        if(filter==0){
            finalsortedList  =  myPromoList.sortedWith(compareByDescending {it.totalPoints})

        }
        else if(filter==1){
            finalsortedList  =  myPromoList.sortedWith(compareByDescending {it.distance})


        }
        else if(filter==2){
            finalsortedList  =  myPromoList.sortedWith(compareByDescending {it.preferenceMatched})


        }
        var sortedRecycler2List = ArrayList<PromoModel>()
        var sortedRecycler3List = ArrayList<PromoModel>()

            Log.e(TAG,"     Promo    ViewPts    LikePts     PreferencePts       AvailedPts      Subcategoriespts      Demographypts      Total")

        for(i in 0 until finalsortedList.size){


            var a = finalsortedList[i].promoname
            var b = finalsortedList[i].subcategory_viewPoints
            var c = finalsortedList[i].subcategory_likesPoints
            var d = finalsortedList[i].subcategory_preferencePoints
            var e = finalsortedList[i].subcategory_availedPoints
            var f = finalsortedList[i].subcategory_totalPoints
            var g = finalsortedList[i].demography_totalPoints
            var h = finalsortedList[i].totalPoints
            Log.e(TAG," Subcategoriespts - $f  Demographypts - $g    Total - $h   promo - $a         ViewPts - $b    LikePts - $c     PreferencePts - $d       AvailedPts - $e     ")



            if(i==0){

                Picasso.get()
                        .load(finalsortedList[i].promoImageLink)
                        .placeholder(R.drawable.hyperdealslogofinal)
                        .into(PromoImage)

                PromoStore.text = finalsortedList[i].promoStore
                PromoTitle.text = finalsortedList[i].promoname
                this.PromoDescription.text = finalsortedList[i].promodescription
                PromoPlace.text = finalsortedList[i].promoPlace
                PromoConctact.text = finalsortedList[i].promoContactNumber

            }
            else{
                if(i<3) {
                    sortedRecycler2List.add(finalsortedList[i])

                }
                else{
                    if(i<20){
                        sortedRecycler3List.add(finalsortedList[i])
                    }


                }


            }

        }

        setAdapter2(sortedRecycler2List)
        setAdapter3(sortedRecycler3List)

    }
    fun sortFinalRecommendedListDistance(myPromoList:ArrayList<PromoModel>,filter:Int){
        swipe.isRefreshing = false

        myPromoLister = myPromoList
        executed = true


           var finalsortedList  =  myPromoList.sortedWith(compareBy {it.distance})


        var sortedRecycler2List = ArrayList<PromoModel>()
        var sortedRecycler3List = ArrayList<PromoModel>()

        Log.e(TAG," Sorted by Distance " )
        for(i in 0 until finalsortedList.size){


            var a = finalsortedList[i].promoname
            var b = finalsortedList[i].subcategory_viewPoints
            var c = finalsortedList[i].subcategory_likesPoints
            var d = finalsortedList[i].subcategory_preferencePoints
            var e = finalsortedList[i].subcategory_availedPoints
            var f = finalsortedList[i].subcategory_totalPoints
            var g = finalsortedList[i].demography_totalPoints
            var h = finalsortedList[i].totalPoints
            Log.e(TAG," ${myPromoList[i].promoname}   distance --- ${myPromoList[i].distance} ")



            if(i==0){

                Picasso.get()
                        .load(finalsortedList[i].promoImageLink)
                        .placeholder(R.drawable.hyperdealslogofinal)
                        .into(PromoImage)

                PromoStore.text = finalsortedList[i].promoStore
                PromoTitle.text = finalsortedList[i].promoname
                this.PromoDescription.text = finalsortedList[i].promodescription
                PromoPlace.text = finalsortedList[i].promoPlace
                PromoConctact.text = finalsortedList[i].promoContactNumber

            }
            else{
                if(i<3) {
                    sortedRecycler2List.add(finalsortedList[i])

                }
                else{
                    if(i<20){
                        sortedRecycler3List.add(finalsortedList[i])
                    }


                }


            }

        }

        setAdapter2(sortedRecycler2List)
        setAdapter3(sortedRecycler3List)

    }
    fun sortFinalRecommendedListPreferred(myPromoList:ArrayList<PromoModel>,filter:Int){
        swipe.isRefreshing = false

        myPromoLister = myPromoList
        executed = true


        var finalsortedList  =  myPromoList.sortedWith(compareByDescending {it.preferenceMatched})


        var sortedRecycler2List = ArrayList<PromoModel>()
        var sortedRecycler3List = ArrayList<PromoModel>()

        Log.e(TAG," Sorted by Preference " )
        for(i in 0 until finalsortedList.size){


            var a = finalsortedList[i].promoname
            var b = finalsortedList[i].subcategory_viewPoints
            var c = finalsortedList[i].subcategory_likesPoints
            var d = finalsortedList[i].subcategory_preferencePoints
            var e = finalsortedList[i].subcategory_availedPoints
            var f = finalsortedList[i].subcategory_totalPoints
            var g = finalsortedList[i].demography_totalPoints
            var h = finalsortedList[i].totalPoints
            Log.e(TAG," ${myPromoList[i].promoname}   Preference matched --- ${myPromoList[i].distance} ")



            if(i==0){

                Picasso.get()
                        .load(finalsortedList[i].promoImageLink)
                        .placeholder(R.drawable.hyperdealslogofinal)
                        .into(PromoImage)

                PromoStore.text = finalsortedList[i].promoStore
                PromoTitle.text = finalsortedList[i].promoname
                this.PromoDescription.text = finalsortedList[i].promodescription
                PromoPlace.text = finalsortedList[i].promoPlace
                PromoConctact.text = finalsortedList[i].promoContactNumber

            }
            else{
                if(i<3) {
                    sortedRecycler2List.add(finalsortedList[i])

                }
                else{
                    if(i<20){
                        sortedRecycler3List.add(finalsortedList[i])
                    }


                }


            }

        }

        setAdapter2(sortedRecycler2List)
        setAdapter3(sortedRecycler3List)

    }



    fun setAdapter3(promoList:ArrayList<PromoModel>){
        var mSelected: SparseBooleanArray = SparseBooleanArray()

        recyclerView3.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
        recyclerView3.adapter =  PromoListAdapter(activity!!,mSelected, promoList)
        recyclerView3.itemAnimator = DefaultItemAnimator()


    }
    fun setAdapter2(promoList:ArrayList<PromoModel>){
        var mSelected: SparseBooleanArray = SparseBooleanArray()
        recyclerView2.layoutManager = GridLayoutManager(activity!!,2)
        recyclerView2.adapter =  PromoListAdapter(activity!!,mSelected, promoList)
        recyclerView2.itemAnimator = DefaultItemAnimator()


    }

    fun setToZero(){



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
