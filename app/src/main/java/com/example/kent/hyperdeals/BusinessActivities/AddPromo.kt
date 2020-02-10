package com.example.kent.hyperdeals.BusinessActivities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.example.kent.hyperdeals.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.zaddpromobusinessman.*
import android.webkit.MimeTypeMap
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.*
import org.jetbrains.anko.toast
import java.util.*
import android.util.Log
import android.view.View
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.GeoPoint
import android.text.format.DateFormat;
import android.widget.*
import com.example.kent.hyperdeals.FragmentActivities.DialogFragmentAddCategoryBusiness
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.LoginActivityBusinessman
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.CategoryAdapterBusiness
import com.example.kent.hyperdeals.MyAdapters.SelectedSubcategoryAdapterBusiness
import com.google.android.gms.internal.zzahn
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.locationManager
import java.lang.Exception
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AddPromo : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener, myInterfaces {

    var globalPromoStore = " "
    companion object {
        lateinit var recyclerViewSub:RecyclerView   
        var globalCategorylist=ArrayList<CategoryParse>()
        lateinit var globalCustomSubcategory:ArrayList<String>
        lateinit var globalCustomCategory:ArrayList<String>
        var categoryLista = ArrayList<String>()

    }
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    lateinit var myStoreCategories:ArrayList<String>
    var storeList = ArrayList<StoreModel>()
    lateinit var subcategoryList:ArrayList<String>
    var globalSubcategoryStringList = ArrayList<String>()
    val database = FirebaseFirestore.getInstance()
    private var mImageLink : UploadTask.TaskSnapshot?=null
    var ref=FirebaseDatabase.getInstance().getReference("Geofences")
    var geoFire: GeoFire=GeoFire(ref)
    var  myGeolocation:GeoLocation= GeoLocation(2.2,2.2)
    val PICK_IMAGE_REQUEST = 11
    var imageUri: Uri?=null
    val place:PlaceSelectionListener?=null
    val TAG = "AddPromo"
    var myDate:Date?=null
    var year:Int? =null
    var month:Int? =null
    var day:Int? = null
    var hour:Int? = null
    var mystartDate:TextView?=null
    var myendDate:TextView?=null
    var selectedEndorStart =0

    var startDateCalendar=Calendar.getInstance()
    var endDateCalendar=Calendar.getInstance()
    lateinit var myAdapter: CategoryAdapterBusiness

    var categoryList=ArrayList<CategoryParse>()

    var startDateYear:Int?=null
    var startDateMonth:Int?=null
    var startDateDay:Int?=null
    var endDateYear:Int?=null
    var endDateMonth:Int?=null
    var endDateDay:Int?=null
    var startTimeHour:Int?=null
    var startTimeMinute:Int?=null
    var endTimeHour:Int?=null
    var endTimeMinute:Int?=null

    lateinit var newFragment:DialogFragmentAddCategoryBusiness

    private var mStorage: FirebaseStorage?=null
    private var mStorageReference : StorageReference?=null
    private var mFirebaseFirestore = FirebaseFirestore.getInstance()
    var initialSqm = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zaddpromobusinessman)
        getStores2()

        et_sqm.setText("${initialSqm}")
         startTimeHour=8
         startTimeMinute=0
         endTimeHour=17
        endTimeMinute=0
        startTime.text = "${startTimeHour}:${startTimeMinute} am"
        endTime.text = "5:00 pm"
        var myCalendar = Calendar.getInstance()
        var myStartDate = myCalendar.time
        var  dateFormat = SimpleDateFormat("MM/dd/yyyy")
        startDate.text = dateFormat.format(myStartDate)
        startDateYear = myCalendar.get(Calendar.YEAR)
         startDateMonth = myCalendar.get(Calendar.MONTH)
         startDateDay = myCalendar.get(Calendar.DAY_OF_MONTH)
        Log.e(TAG,"Initial start date ${startDateYear},${startDateMonth}${startDateYear}")






        newFragment = DialogFragmentAddCategoryBusiness().newInstance()
        getCategories()
        recyclerViewSub = findViewById<RecyclerView>(R.id.Recycler_selectedSubcategory)
        var  selectedSubAdapter = SelectedSubcategoryAdapterBusiness(this, arrayListOf(""))
        var myStagger = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        recyclerViewSub.layoutManager = myStagger
        recyclerViewSub.adapter = selectedSubAdapter



        imageView7.setOnClickListener { finish()
                categoryLista = ArrayList<String>()

                globalCustomSubcategory = ArrayList<String>()
            CategoryAdapterBusiness.globalCategoryList =  ArrayList<CategoryParse>()
        }
        btnGetLocation.setOnClickListener { getLocation()
        }
        addPromoImage.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"),  PICK_IMAGE_REQUEST)
        }

        addPromoPublish.setOnClickListener{

            uploadFile()


        }

        btn_add_categoru.setOnClickListener {
            showDialog()

        }



        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {


                val getlat= place.latLng.latitude.toString()
                val getlong = place.latLng.longitude.toString()

                val getPlaceName = place.name.toString()

                addPromoLocation.setText(getPlaceName)

                addPromoPlace.setText(getlat + "," + getlong)
                myGeolocation = GeoLocation(place.latLng.latitude,place.latLng.longitude)
            }

            override fun onError(status: Status) {
                toast("Error")
            }
        })



        startDate.setOnClickListener {
            selectedEndorStart = 1
            var myCalendar = Calendar.getInstance()
            year = myCalendar.get(Calendar.YEAR)
            month = myCalendar.get(Calendar.MONTH)
            day = myCalendar.get(Calendar.DAY_OF_MONTH)
            myDate = myCalendar.time



            var myTimePicker = DatePickerDialog(this,this, year!!,month!!,day!!)
            myTimePicker.show()


        }

        endDate.setOnClickListener {
            selectedEndorStart = 2
            var myCalendar = Calendar.getInstance()
            year = myCalendar.get(Calendar.YEAR)
            month = myCalendar.get(Calendar.MONTH)
            day = myCalendar.get(Calendar.DAY_OF_MONTH)
            myDate = myCalendar.time
            myCalendar.time

            var myTimePicker = DatePickerDialog(this,this, year!!,month!!,day!!)
            myTimePicker.show()


        }


        startTime.setOnClickListener {

            selectedEndorStart = 3
            var myTimepicker =  TimePickerDialog(this,this,8,0,DateFormat.is24HourFormat(this))
            myTimepicker.show()
        }
        endTime.setOnClickListener {

            selectedEndorStart = 4
            var myTimepicker =  TimePickerDialog(this,this,17,0,DateFormat.is24HourFormat(this))
            myTimepicker.show()
        }

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Log.e(TAG,"$p1/$p2/$p3")

        var myCalendar  = Calendar.getInstance()
        myCalendar.set(p1,p2,p3)
        var  dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val myDate:Date = myCalendar.time



        if(selectedEndorStart==1){
            startDateYear = p1
            startDateMonth = p2+1
            startDateDay = p3

            startDateCalendar!!.set(p1,p2,p3  )
            Log.e(TAG,"${startDateCalendar.time}")
            var currentCalendar = Calendar.getInstance()
            if(currentCalendar!!.timeInMillis>startDateCalendar!!.timeInMillis){
             //   Toast.makeText(this,"Start Date should be further than today",Toast.LENGTH_LONG).show()
                startDate.text = dateFormat.format(myDate)

            }
            else
                startDate.text = dateFormat.format(myDate)

        }
        else if (selectedEndorStart == 2){
            endDateYear = p1
            endDateMonth = p2+1
            endDateDay = p3

            endDateCalendar!!.set(p1,p2,p3)

            if(startDateCalendar!!.timeInMillis>endDateCalendar!!.timeInMillis){
                Toast.makeText(this,"End Date should be further than Start Date",Toast.LENGTH_LONG).show()
            }
            else {
                endDate.text = dateFormat.format(myDate)
            }
        }

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        var amorpm = "am"
        var subHour =p1.toString()
        var subMinute = p2.toString()
        if(selectedEndorStart==3){
            startTimeHour = p1
            startTimeMinute = p2
            if(p1==0)
                p1 -1
            if(p1>=12) {
                amorpm = "pm"
                p1 - 12

            }
            Log.e(TAG,"length ${p1.toString().length}")
            if(p2.toString().length==1)
             subMinute ="0${p2.toString()}"

            if(p1>12)
                subHour =(p1-12).toString()
            startTime.text = "$subHour:$subMinute $amorpm"

        }
        else if (selectedEndorStart==4)
        {

            if(p1>=12) {
                amorpm = "pm"
                p1 - 12

            }
            endTimeHour = p1
            endTimeMinute = p2
            if(p2.toString().length==1)
             subMinute ="0${p2}"
            if(p1>12)

             subHour =(p1-12).toString()
            endTime.text = "$subHour:$subMinute $amorpm"
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataa: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataa)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && dataa != null){
            imageUri = dataa.data

            Picasso.get().load(imageUri).into(addPromoImage)


        }
    }



    private fun uploadFile() {

        mStorage = FirebaseStorage.getInstance()
        mStorageReference = mStorage!!.reference

        if (imageUri!=null){
            addPromoPublish.text = " "
            addPromoPublish.isEnabled = false
            val ref = mStorageReference!!.child("images/" + UUID.randomUUID().toString())
            ref.putFile(imageUri!!)
                    .addOnSuccessListener {


                        addPromoProgressBar.visibility = View.VISIBLE

                        val image = it.downloadUrl.toString()

                        addPromoImageLink.setText(image)

                        toast("Image Uploaded Successfully")

                        var SangitString = "sangit"
                        var proceed=true
                        if(startTime.text.toString().matches("0 0 : 0 0".toRegex())){
                            SangitString = "Set start time"
                             proceed=false

                        }
                        if(endTime.text.toString().matches("0 0 : 0 0".toRegex())){
                            SangitString = "Set end time"
                            proceed=false

                        }
                        var finalSqm = et_sqm.text
                        if(initialSqm>finalSqm.toString().toInt()){
                            SangitString = "Area must atleast be 30 sqm"
                            proceed=false


                        }
                        if(startDate.text.toString().matches("M-DD-YYYY".toRegex())){
                            SangitString = "Set start date"
                            proceed=false

                        }
                        if(endDate.text.toString().matches("M-DD-YYYY".toRegex())){
                            SangitString = "Set end date"
                            proceed=false

                        }
                        if(proceed) {

                            storeDatatoFirestore()
                            finish()
                            addPromoProgressBar.visibility = View.VISIBLE
                        }
                        else{
                            toast(SangitString)
                        }
                    }
                    .addOnFailureListener{
                        addPromoPublish.isEnabled = true
                        addPromoPublish.text =  "Publish Promo"
                        toast("Uploading Failed")
                    }

        }




    }
    private fun addGeofence(key:String,location:GeoLocation){





        geoFire.setLocation(key,location, GeoFire.CompletionListener { key, error ->

            Log.d("HyperDeals",key)


        })


    }
    override fun saveCategoriesBusiness(myCategoryList: ArrayList<CategoryParse>) {
        globalCategorylist=ArrayList<CategoryParse>()
        categoryLista = ArrayList<String>()
            Log.e(TAG, "saveCategoriesBusiness")
         subcategoryList = ArrayList<String>()
            for (i in 0 until myCategoryList.size) {
                Log.e(TAG, "loop1")

                for (j in 0 until myCategoryList[i].Subcategories.size) {
                    Log.e(TAG, "loop2")
                    if (myCategoryList[i].Subcategories[j].Selected) {
                        subcategoryList.add(myCategoryList[i].Subcategories[j].SubcategoryName)
                        categoryLista.add(myCategoryList[i].categoryName)
                        Log.e(TAG,"category lista added ${myCategoryList[i].categoryName}")
                        Log.e(TAG,"${myCategoryList[i].Subcategories[j].SubcategoryName} and ${myCategoryList[i].categoryName} ")


                    }
                }


            }
        Log.e(TAG,"categoryLista ${categoryLista.size} size")
            globalSubcategoryStringList = subcategoryList
            for (k in 0 until subcategoryList.size) {

                Log.e(TAG, "selected - " + subcategoryList[k])
            }
setAdapter(subcategoryList)


    }

    private fun storeDatatoFirestore(){
        addPromoProgressBar.visibility = View.VISIBLE
        Log.d("HyperDeals",subsubTag.text.toString())
        val pStore = et_storeName.text.toString()
        val pContact = addPromoContact.text.toString()
        val pName = addPromoName.text.toString()
        val pDescription = addPromoDescription.text.toString()
        val pLatLng = addPromoPlace.text.toString()
        val pPromoPlace = addPromoLocation.text.toString()
        val pPromoImageLink = addPromoImageLink.text.toString()



Log.e(TAG,"$pName $pStore $pContact")
        val pEntity = PromoModelBusinessman("asdasd", pStore, pContact, pDescription, pPromoPlace, pName, pLatLng, pPromoImageLink,
                GeoPoint(myGeolocation.latitude, myGeolocation.longitude), subsubTag.text.toString(), 0, 0, 0, 0
                , startDateYear!!
                , startDateMonth!!
                , startDateDay!!
                , endDateYear!!
                , endDateMonth!!
                , endDateDay!!
                , startTimeHour!!
                , startTimeMinute!!
                , endTimeHour!!
                , endTimeMinute!!

        )
        pEntity.approved = false
        pEntity.posterBy = LoginActivityBusinessman.userBusinessManUsername
        pEntity.categoryLista = categoryLista
        pEntity.areaSqm = et_sqm.text.toString().toDouble()
        var randomUIID = "${pStore}-"+UUID.randomUUID().toString()
        pushtoDataBsae(randomUIID)


        var myDemoTarget= DemoTarget(randomUIID,AgeTarget(businessmancheckboxyoung.isChecked,businessmancheckboxteenager.isChecked,
                businessmancheckboxadult.isChecked), GenderTarget(businessmancheckboxmale.isChecked,businessmancheckboxfemale.isChecked),
                StatusTarget(businessmancheckboxsingle.isChecked, businessmancheckboxinarelationship.isChecked))

        Log.e("leroygwapo",myDemoTarget.toString())

        mFirebaseFirestore.collection("PromoDemography").document(randomUIID).collection("AgeTarget").document("AgeTarget").set(myDemoTarget.myAgeTarget)
        mFirebaseFirestore.collection("PromoDemography").document(randomUIID).collection("GenderTarget").document("GenderTarget").set(myDemoTarget.myGenderTarger)
        mFirebaseFirestore.collection("PromoDemography").document(randomUIID).collection("StatusTarget").document("StatusTarget").set(myDemoTarget.myStatusTarget)

        mFirebaseFirestore.collection("PendingPromoDetails").document(randomUIID).set(pEntity)
        toast("Success")
        addPromoProgressBar.visibility = View.INVISIBLE
        Log.d("HyperDeals",myGeolocation.latitude.toString()+myGeolocation.longitude.toString())


    }




    fun getCategories() {
        doAsync {

            database.collection("Categories").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {
                        var uploaded = DocumentSnapshot.toObject(CategoryParse::class.java)
                        Log.e(TAG, DocumentSnapshot.getId() + " => " + DocumentSnapshot.getData())
                        database.collection("Categories").document(DocumentSnapshot.id).collection("Subcategories").get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (DocumentSnapshot in task.result) {
                                    var upload = DocumentSnapshot.toObject(SubcategoryParse::class.java)
                                    Log.e(TAG, " Subcategory - " + upload.toString())
                                    uploaded.Subcategories.add(upload)


                                }


                            } else
                                toast("error")
                        }

                        categoryList.add(uploaded)
                        globalCategorylist.add(uploaded)

                    }

//
//                    for(i in 0 until  categoryList.size){
//                        doAsync {
//                            for (j in 0 until categoryList[i].Subcategories.size){
//                                doAsync {
//                                    categoryList[i].Subcategories[j].Selected = true
//                                }
//                            }
//
//
//
//                        }
//
//
//                    }




                } else
                    toast("error")
            }


        }

    }
    fun setAdapter(myList:ArrayList<String>){
        var  selectedSubAdapter = SelectedSubcategoryAdapterBusiness(this,subcategoryList)

Log.e("Suway2","${subcategoryList.size} ${subcategoryList.toString()}")

        globalCustomSubcategory = subcategoryList


        var myStagger = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        recyclerViewSub.layoutManager = myStagger
            recyclerViewSub.adapter = selectedSubAdapter
    }

    fun pushtoDataBsae(randomUIID:String){

        Log.e("Suway2", globalCustomSubcategory.size.toString() + globalCustomSubcategory.toString())

        var myArrayStringSub = globalCustomSubcategory
        var myArrayStringSub2 = categoryLista
        globalCustomSubcategory= ArrayList()
        categoryLista = ArrayList()
        CategoryAdapterBusiness.globalCategoryList =  ArrayList<CategoryParse>()

        Log.e(TAG,"time for push ${myArrayStringSub.size}")
        for(k in 0 until myArrayStringSub.size){
            doAsync {
                val Subcategory = HashMap<String, Any>()
                Subcategory["SubcategoryName"] = myArrayStringSub[k]

                mFirebaseFirestore.collection("PromoCategories").document(randomUIID).collection("Subcategories")
                        .document().set(Subcategory)
                        .addOnSuccessListener { Log.e(TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.e(TAG, "Error  writing document", e) }
            }

        }
        Log.e(TAG,"categoryLista size ${categoryLista.size}" )
        for (w in 0 until categoryLista.size){
            doAsync {


                val Category = HashMap<String, Any>()
                Category["CategoryName"] = myArrayStringSub2[w]

                mFirebaseFirestore.collection("PromoCategories").document(randomUIID).collection("Categories")
                        .document().set(Category)
                        .addOnSuccessListener { Log.e(TAG, "DocumentSnapshot successfully written Categories!") }
                        .addOnFailureListener { e -> Log.e(TAG, "Error  writing document", e) }
            }
            }
    }
    private fun showDialog() {
        AddStore.Store = false
        var myDialog = DialogFragmentAddCategoryBusiness()
        myDialog.show(fragmentManager,"myCustomDialog")
    }

    fun setSpinner(stringlist:ArrayList<String>){
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, stringlist)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = aa


        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
             //   globalPromoStore = LoginActivityBusinessman.globalUserBusinessman.stores[0]

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
         //       globalPromoStore = LoginActivityBusinessman.globalUserBusinessman.stores[position]
//                Log.e(TAG," selected ${ LoginActivityBusinessman.globalUserBusinessman.stores[position]}")
                for(i in 0 until storeList.size){
                    if(storeList[i].storeName==stringlist[position]){
                        changeFields(storeList[i])

                    }
                    else {

                    }

                }
            }

        }
    }
    fun noneFields(){
        addPromoPublish.visibility = View.GONE
    }
    fun changeFields(myStore:StoreModel){
        myGeolocation =GeoLocation( myStore.storeLatLng.latitude,myStore.storeLatLng.longitude)
        addPromoPublish.visibility = View.VISIBLE

        et_storeName.setText(myStore.storeName)
        addPromoLocation.setText(myStore.storeAddress)
        addPromoContact.setText(myStore.storeContact)

        myStoreCategories = ArrayList<String>()
        myStoreCategories = myStore.storeCategories
        globalCustomSubcategory = myStore.storeCategories

        var  selectedSubAdapter = SelectedSubcategoryAdapterBusiness(this,myStore.storeCategories)
        var myStagger = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        recyclerViewSub.layoutManager = myStagger
        recyclerViewSub.adapter = selectedSubAdapter

    }

    fun getStores2() {
        var spinerList = arrayListOf<String>("None")
        doAsync {
            database.collection("UserBusinessman").document(LoginActivity.userUIDS).collection("Stores").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {


                        val storeName = DocumentSnapshot.data.get("storeName").toString()
                        val storeDescription = DocumentSnapshot.data.get("storeDescription").toString()
                        val storeAddress = DocumentSnapshot.data.get("storeAddress").toString()
                        val storeBy  = DocumentSnapshot.data.get("storeBy").toString()
                        val storeContact= DocumentSnapshot.data.get("storeContact").toString()
                        val storeLink = DocumentSnapshot.data.get("storeLink").toString()
                        val storeOpenTime  = DocumentSnapshot.data.get("storeOpenTime").toString()
                        val storeCloseTime = DocumentSnapshot.data.get("storeCloseTime").toString()
                        val storeCategories = DocumentSnapshot.data.get("storeCategories") as ArrayList<String>
                        var storeLatLng = DocumentSnapshot.data.get("storeLatLng") as GeoPoint
                        val storeImage  = DocumentSnapshot.data.get("storeImage").toString()

                        val myStore  = StoreModel(storeImage,storeName,storeContact,storeDescription
                                ,storeLink, storeLatLng ,storeAddress, storeCategories,storeOpenTime,storeCloseTime,storeBy)
                        storeList.add(myStore)
                        spinerList.add(storeName)
                        Log.e(TAG,myStore.toString())
//                        Log.e(TAG,myStore.toString())
                    }
                    setSpinner(spinerList)


                }
            }

        }
    }
    fun getLocation() {
        var callTimes = false
        Log.e(TAG,"getLocation")
        locationManager = this!!.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

                Log.e(TAG,"Location change ${      location.latitude }  +  ${location.longitude}")
            if(!callTimes){

                val string = "${location.latitude},${location.longitude}"
                addPromoLocation.setText(string)
                myGeolocation = GeoLocation(location.latitude,location.longitude)

                addPromoLocation.setText(getAdress(location.latitude,location.longitude))
                callTimes= true

            }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onProviderDisabled(provider: String) {

            }
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(
                            this!!.applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this!!.applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

            // locationManager.!!requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f,);

        } else {
            if (ActivityCompat.checkSelfPermission(
                            this!!.applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this!!.applicationContext, Manifest.permission
                            .ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                return
            } else {
                //       locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
                // locationManager!!.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
                try {
                    locationManager!!.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f, locationListener)
                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

                }
                catch (e: Exception){
                    Log.e(TAG,"LOL")
                }
                Log.e(TAG, "this part")
            }

        }
    }
    fun getAdress(latitude: Double, longitude: Double):String {

        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        val city = addresses[0].locality
        val state = addresses[0].adminArea
        val country = addresses[0].countryName
        val postalCode = addresses[0].postalCode
        val knownName = addresses[0].featureName
        val subLocality = addresses[0].subLocality
        val premises = addresses[0].premises
        val a = addresses[0].subAdminArea





        Log.e(TAG,"$city \n  $state\n  $country\n  $postalCode\n  $knownName\n  $subLocality $premises $a")
        Log.e(TAG,address)
        return address
    }

}


