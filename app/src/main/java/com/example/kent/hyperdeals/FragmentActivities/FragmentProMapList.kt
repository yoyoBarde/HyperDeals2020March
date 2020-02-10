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
    lateinit var myPromoList:ArrayList<PromoModel>

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
        swipe.setOnRefreshListener {


        }

        var filterAdapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.filter_recommendation,android.R.layout.simple_spinner_item)


        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filter_spinner.adapter = filterAdapter

        filter_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                if( filter_spinner.selectedItem.toString().equals("Recommended")){
                    Log.e(TAG,"Recommended")
                }


                else if( filter_spinner.selectedItem.toString().equals("Distance")){

                    Log.e(TAG,"Distance")



                }
                else if(filter_spinner.selectedItem.toString().equals("Preferred")){
                    Log.e(TAG,"Preferred")

                }


        }
            }

        for(i in 0 until 100000){
            if(FragmentCategory.globalPromoList.size>0 ){


                myPromoList=FragmentCategory.globalPromoList
                Loopthelist(myPromoList)
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


    }


    fun Loopthelist (myPromoList:ArrayList<PromoModel>) {

            for (i in 0 until myPromoList.size) {
                doAsync {
                    Log.e(TAG, "LOOOOOOOOOOOOOOOOOOOOOOOOOP $i")

                    Log.e(TAG, "${myPromoList[i].promoname} ${myPromoList[i].preferenceMatched} ${myPromoList[i].subcategories.size}  ${myPromoList[i].promoID}")

                    database.collection("PromoData").document("PromoViews").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {

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
                            Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user views -  ${myPromoList[i].userListView.size} ")


                        } else {
                            Log.e(TAG, "LooptheList task failed")
                        }


                    }

                    database.collection("PromoData").document("PromoLikes").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {

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
                            Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user likes -  ${myPromoList[i].userListLikes.size} ")

                        }


                    }

                    database.collection("PromoData").document("PromoPreferred").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {

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
                            Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user preferred -  ${myPromoList[i].userListPreferred.size} ")

                        }


                    }

                    database.collection("PromoData").document("PromoAvailed").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {

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
                            Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user availed -  ${myPromoList[i].userListAvailed.size} ")

                        }


                    }

                    database.collection("PromoData").document("PromoDismissed").collection("Promos").document(myPromoList[i].promoID).collection("Users").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {

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
                            Log.e(TAG, "LooptheListView task successful ${myPromoList[i].promoname} user dismissed -  ${myPromoList[i].userListDismissed.size} ")

                        }


                    }

                }

            }


        getUserSubcategoryActivities(myPromoList)


    }


    class ProgressAsyncTask :AsyncTask<Int,Int,Void> (){
        override fun doInBackground(vararg p0: Int?): Void {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: Void?) {

        }


    }
    fun getUserSubcategoryActivities(myPromoList:ArrayList<PromoModel>){

        Log.e(TAG,"getUserSubcategoryActivities Start")
        var userViewedSubcategoryParceList= ArrayList<UserSubcategoriesPreferencesParcelable>()
        var userLikedSubcategoryParceList= ArrayList<UserSubcategoriesPreferencesParcelable>()
        var userPreferredSubcategoryParceList= ArrayList<UserSubcategoriesPreferencesParcelable>()
        var userAvailedSubcategoryParceList= ArrayList<UserSubcategoriesPreferencesParcelable>()
        var userDismissedSubcategoryParceList= ArrayList<UserSubcategoriesPreferencesParcelable>()

    database.collection("UserViewedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            for (DocumentSnapshot in task.result) {
                var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                userViewedSubcategoryParceList.add(subcategoriesParce)
            }

            Log.e(TAG, "UserViewedPreferencesSubcategory ${userViewedSubcategoryParceList.size}")

        }
        database.collection("UserLikedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (DocumentSnapshot in task.result) {
                    var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                    userLikedSubcategoryParceList.add(subcategoriesParce)

                }
                Log.e(TAG, "UserLikedPreferencesSubcategory ${userLikedSubcategoryParceList.size}")

            }
            database.collection("UserPreferredPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {
                        var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                        userPreferredSubcategoryParceList.add(subcategoriesParce)

                    }
                    Log.e(TAG, "UserPreferreddPreferencesSubcategory ${userPreferredSubcategoryParceList.size}")

                }
                database.collection("UserAvailedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (DocumentSnapshot in task.result) {
                            var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                            userAvailedSubcategoryParceList.add(subcategoriesParce)

                        }
                        Log.e(TAG, "UserAvailedPreferencesSubcategory ${userAvailedSubcategoryParceList.size}")

                    }
                    database.collection("UserDismissedPreferences").document(LoginActivity.userUIDS).collection("Subcategories").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (DocumentSnapshot in task.result) {
                                var subcategoriesParce = DocumentSnapshot.toObject(UserSubcategoriesPreferencesParcelable::class.java)
                                userDismissedSubcategoryParceList.add(subcategoriesParce)

                            }
                            Log.e(TAG, "UserDismissedPreferencesSubcategory ${userDismissedSubcategoryParceList.size}")

                        }
                        for( i in 0 until myPromoList.size){
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
                                myPromoList[i].subcategory_preferencePoints = myPromoList[i].subcategory_preferenceCount * preferenceCriteria
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
    for(i in 0 until myPromoList.size){

        myPromoList[i].demography_viewPoints = (myPromoList[i].genderMatchViews + myPromoList[i].ageMatchViews + myPromoList[i].statusMatchViews) * viewCriteria
        myPromoList[i].demography_likesPoints =(myPromoList[i].genderMatchLikes + myPromoList[i].ageMatchLikes + myPromoList[i].statusMatchLikes) * likeCriteria
        myPromoList[i].demography_preferencePoints = (myPromoList[i].genderMatchPreferred + myPromoList[i].ageMatchPreferred + myPromoList[i].statusMatchPreferred) * preferenceCriteria
        myPromoList[i].demography_availedPoints =(myPromoList[i].genderMatchAvailed + myPromoList[i].ageMatchAvailed + myPromoList[i].statusMatchAvailed) * availedCriteria
        myPromoList[i].demography_dismissedPoints =(myPromoList[i].genderMatchDismissed + myPromoList[i].ageMatchDismissed + myPromoList[i].statusMatchDismissed) * dismissedCriteria

        myPromoList[i].demography_totalPoints =  myPromoList[i].demography_viewPoints + myPromoList[i].demography_likesPoints + myPromoList[i].demography_preferencePoints +
                myPromoList[i].demography_availedPoints +  myPromoList[i].demography_dismissedPoints




        Log.e(TAG,"  ${myPromoList[i].promoname}    viewCount   -   likeCount   -   preferenceCount -   availedCount    -   dismissedCount  -   ${myPromoList[i].promoID}\n")
        Log.e(TAG,"Demography Scoring")
        Log.e(TAG,"count - ${myPromoList[i].userListView.size}  ${myPromoList[i].userListLikes.size}  ${myPromoList[i].userListPreferred.size}  ${myPromoList[i].userListAvailed.size}  ${myPromoList[i].userListDismissed.size}")
        Log.e(TAG,"points - ${  myPromoList[i].demography_viewPoints}  ${myPromoList[i].demography_likesPoints}  ${myPromoList[i].demography_preferencePoints}  ${myPromoList[i].demography_availedPoints}  ${myPromoList[i].demography_dismissedPoints} - total ${myPromoList[i].demography_totalPoints }")

        Log.e(TAG,"\nSubcategory Scoring")
        Log.e(TAG,"count - ${myPromoList[i].subcategory_viewCount}  ${myPromoList[i].subcategory_likesCount}  ${myPromoList[i].subcategory_preferenceCount}  ${myPromoList[i].subcategory_availedCount}  ${myPromoList[i].subcategory_dismissedCount}")
        Log.e(TAG,"points - ${  myPromoList[i].subcategory_viewPoints}  ${myPromoList[i].subcategory_likesPoints}  ${myPromoList[i].subcategory_preferencePoints}  ${myPromoList[i].subcategory_availedPoints}  ${myPromoList[i].subcategory_dismissedPoints} - total ${myPromoList[i].subcategory_totalPoints }")

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
