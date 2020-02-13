package com.example.kent.hyperdeals.Admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.kent.hyperdeals.FragmentActivities.DialogFragmentAddCategoryUser
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.*

import com.example.kent.hyperdeals.R
import com.example.kent.hyperdeals.R.layout.fragment_data_control
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_data_control.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class DataControl : Fragment(),myInterfaceSelectPromo {
    companion object{
        lateinit var selectedPromoo:PromoModel
        lateinit var globalTvPromoname:TextView
        lateinit var globalTvPromostore:TextView
        lateinit var globalTvSubcategories:TextView
        lateinit var globalIvPromopic:ImageView
        lateinit var globalDialog:AlertDialog


    }

    override fun selectedPromo(selectedPromo: PromoModel) {

        Log.e(TAG,selectedPromo.promoname +"selectedPromo")
        setSelectedPromo(selectedPromo)
    }


    var database = FirebaseFirestore.getInstance()

    var btnViewClick = false
    var btnLikeClick = false
    var btnPreferredClick = false
    var btnAvailedClick = false
    var btnDismissClick = false
        val TAG = "fragmentBusiness"
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(fragment_data_control, container, false)
        }


        @SuppressLint("ResourceAsColor")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        setViews()

        }

    fun setSelectedPromo(promo:PromoModel){
        Picasso.get()
                .load(promo.promoImageLink)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(globalIvPromopic)
        globalTvPromoname.setText(promo.promoname)
        globalTvPromostore.setText(promo.promoStore)
        globalTvSubcategories.setText(promo.subcategories.size.toString())
        globalDialog.dismiss()

    }
    fun setViews(){

        globalIvPromopic = activity!!.findViewById<ImageView>(R.id.ivPromoPic)
        globalTvPromoname = activity!!.findViewById<TextView>(R.id.tvPromoName)
        globalTvPromostore = activity!!.findViewById<TextView>(R.id.tvPromoStore)
        globalTvSubcategories = activity!!.findViewById<TextView>(R.id.tvSubcategories)

        var promoList = getPromos()

        var ageAdapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.demography_age,android.R.layout.simple_spinner_item)
        var genderAdapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.demography_gender,android.R.layout.simple_spinner_item)
        var statusAdapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(activity!!,R.array.demography_status,android.R.layout.simple_spinner_item)

        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        demography_age.adapter = ageAdapter
        demography_gender.adapter = genderAdapter
        demography_status.adapter = statusAdapter

        btnView.setOnClickListener {

            if (btnViewClick) {
                btnViewClick = false
                btnView.setTextColor(Color.parseColor("#000000"))
                btnView.setBackgroundResource(R.drawable.button_curve)
            } else {
                btnViewClick = true
                btnView.setTextColor(Color.parseColor("#FFFFFF"))
                btnView.setBackgroundResource(R.drawable.gradientbluefinal)
            }
        }
        btnLike.setOnClickListener {
            if(btnLikeClick){
                btnLikeClick= false
                btnLike.setTextColor(Color.parseColor("#000000"))
                btnLike.setBackgroundResource(R.drawable.button_curve)
            }
            else {
                btnLikeClick = true
                btnLike.setTextColor(Color.parseColor("#FFFFFF"))
                btnLike.setBackgroundResource(R.drawable.gradientbluefinal)
            }


        }
        btnAvailed.setOnClickListener {
            if(btnAvailedClick){
                btnAvailedClick= false
                btnAvailed.setTextColor(Color.parseColor("#000000"))
                btnAvailed.setBackgroundResource(R.drawable.button_curve)
            }
            else {
                btnAvailedClick = true
                btnAvailed.setTextColor(Color.parseColor("#FFFFFF"))
                btnAvailed.setBackgroundResource(R.drawable.gradientbluefinal)
            }

        }
        btnPreferred.setOnClickListener {
            if(btnPreferredClick){
                btnPreferredClick= false
                btnPreferred.setTextColor(Color.parseColor("#000000"))
                btnPreferred.setBackgroundResource(R.drawable.button_curve)
            }
            else {
                btnPreferredClick = true
                btnPreferred.setTextColor(Color.parseColor("#FFFFFF"))
                btnPreferred.setBackgroundResource(R.drawable.gradientbluefinal)
            }

        }
        btnDismiss.setOnClickListener {
            if(btnDismissClick){
                btnDismissClick= false
                btnDismiss.setTextColor(Color.parseColor("#000000"))
                btnDismiss.setBackgroundResource(R.drawable.button_curve)
            }
            else {
                btnDismissClick = true
                btnDismiss.setTextColor(Color.parseColor("#FFFFFF"))
                btnDismiss.setBackgroundResource(R.drawable.gradientbluefinal)
            }

        }



        btnSelectPromo.setOnClickListener {
showDialog(1,promoList)

        }



        btnExecute.setOnClickListener {
          var   volume = etNumber.text.toString().toInt()
            for(i in 0 until volume) {

                val random = Random().nextInt(9999 - 1000 + 1) + 1000
                var age = demography_age.selectedItem
                var gender = demography_gender.selectedItem
                var status = demography_status.selectedItem
                var fName = "John"
                var lName = "Doe $random"

                var myUser = UserModel(fName, lName, "dummyData@gmail.com", "******", age.toString(), gender.toString(), status.toString())
                pushDummyData(myUser)

            }
        }
    }


    fun pushDummyData(myUser:UserModel){
        doAsync {

            if (btnViewClick) {



                database.collection("PromoData").document("PromoViews").collection("Promos").document(selectedPromoo.promoID).collection("Users").document().set(myUser).addOnSuccessListener {
                    Log.e(TAG, "Store is fucking satored")

                }

            }
            if (btnLikeClick) {

                database.collection("PromoData").document("PromoLikes").collection("Promos").document(selectedPromoo.promoID).collection("Users").document().set(myUser).addOnSuccessListener {
                    Log.e(TAG, "Store is fucking satored")

                }


            }
            if (btnPreferredClick) {

                database.collection("PromoData").document("PromoPreferred").collection("Promos").document(selectedPromoo.promoID).collection("Users").document().set(myUser).addOnSuccessListener {
                    Log.e(TAG, "Store is fucking satored")

                }


            }
            if (btnAvailedClick) {

                database.collection("PromoData").document("PromoAvailed").collection("Promos").document(selectedPromoo.promoID).collection("Users").document().set(myUser).addOnSuccessListener {
                    Log.e(TAG, "Store is fucking satored")

                }


            }
            if (btnDismissClick) {

                database.collection("PromoData").document("PromoDismissed").collection("Promos").document(selectedPromoo.promoID).collection("Users").document().set(myUser).addOnSuccessListener {
                    Log.e(TAG, "Store is fucking satored")

                }


            }


            uiThread { Toast.makeText(activity!!,"Dummy Data pushed",Toast.LENGTH_LONG).show() }
        }

    }
    fun showDialog(position:Int,promoList:ArrayList<PromoModel>) {



        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_fragment_select_promo, null)

        dialogBuilder.setCancelable(false)

        dialogBuilder.setView(dialogView)




        val searchPromo = dialogView.findViewById(R.id.et_searchPromo) as EditText
        val recyclerView = dialogView.findViewById(R.id.recyclerPromoSearch) as RecyclerView

        var myAdapter = SelectPromoAdapter(activity!!,promoList)
        recyclerView.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
        recyclerView.adapter = myAdapter


        val b = dialogBuilder.create()

        globalDialog = b
        b.show()
        b.setCancelable(true)






        }
    fun getPromos():ArrayList<PromoModel>{

        var promolist = ArrayList<PromoModel>()
        Log.e(TAG,"getting Promos")
        doAsync {
            database.collection("PromoDetails").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {

                        var upload = DocumentSnapshot.toObject(PromoModel::class.java)
                        var geoPoint = DocumentSnapshot.getGeoPoint("promoGeo")

                        upload.promoID = DocumentSnapshot.getId()
                        upload.promoCategories = DocumentSnapshot.getId()
                        upload.promoLocation = LatLng(geoPoint.latitude, geoPoint.longitude)
                        upload.startDateCalendar.set(upload.startDateYear, upload.startDateMonth - 1, upload.startDateDay)
                        upload.endDateCalendar.set(upload.endDateYear, upload.endDateMonth - 1, upload.endDateDay)

                        database.collection("PromoCategories").document(upload.promoCategories).collection("Subcategories").get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (DocumentSnapshot in task.result) {
                                    var subcategory = DocumentSnapshot.toObject(promoSubcategoryParce::class.java)
                                    upload.subcategories.add(subcategory.SubcategoryName)
                                    Log.e(TAG, "${upload.promoStore} - ${subcategory.SubcategoryName}")

                                }
                                Log.e(TAG, "promosubCategory size ${upload.promoStore} ${upload.promoname}- ${upload.subcategories.size} index equals ${promolist.size}")

                            }

                        }


                        database.collection("PromoDemography").document(upload.promoID).collection("AgeTarget").document("AgeTarget").get().addOnSuccessListener { document ->
                            if (document != null  && document.exists()) {
                                Log.e(TAG, "DocumentSnapshot data: ${document.data}")
                                var myAgeTarget = document.toObject(AgeTargetParcelable::class.java)
                                Log.e(TAG,myAgeTarget.toString())

                            } else {
                                Log.e(TAG, "No such document")
                            }
                        }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "get failed with ", exception)
                                }
                        database.collection("PromoDemography").document(upload.promoID).collection("GenderTarget").document("GenderTarget").get().addOnSuccessListener { document ->
                            if (document != null && document.exists()) {

                                Log.e(TAG, "DocumentSnapshot data: ${document.data}")
                                var myGenderTarget = document.toObject(GenderTargetParcelable::class.java)
                                Log.e(TAG,myGenderTarget.toString())


                            } else {
                                Log.e(TAG, "No such document")
                            }
                        }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "get failed with ", exception)
                                }
                        database.collection("PromoDemography").document(upload.promoID).collection("StatusTarget").document("StatusTarget").get().addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                Log.e(TAG, "DocumentSnapshot data: ${document.data}")
                                var myStatusTarget = document.toObject(StatusTargetParcelable::class.java)
                                Log.e(TAG,myStatusTarget.toString())

                            } else {
                                Log.e(TAG, "No such document")
                            }
                        }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "get failed with ", exception)
                                }

                        if(upload.approved) {
                            promolist.add(upload)
                        }


                    }
                    try {

                    }
                    catch (e: Exception){

                    }


                } else
                    toast("error")
            }






        }

        return promolist
    }


    }







