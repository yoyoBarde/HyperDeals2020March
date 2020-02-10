package com.example.kent.hyperdeals.FragmentActivities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RemoteViews
import com.example.kent.hyperdeals.*
import com.example.kent.hyperdeals.FragmentsBusiness.Business_PromoProfile
import com.example.kent.hyperdeals.Home.HomeAdapter
import com.example.kent.hyperdeals.Home.PreferedPromoAdapter
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.CategoryHomeAdapter
import com.example.kent.hyperdeals.MyAdapters.HottestPromoAdapter
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.NavigationOptionsActivity.UserHistory
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.internal.zzahn.runOnUiThread
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.dialogbox.view.*
import kotlinx.android.synthetic.main.fragmentcategory.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentCategory: Fragment() {
    companion object {
        lateinit var userLatLng: LatLng
        lateinit var globalPromoList:ArrayList<PromoModel>
         var globalUserPreferredTime:userPreferredTimeParce?=null
         var categoryListAll = ArrayList<CategoryParse>()
         var promolist = ArrayList<PromoModel>()
        var promoDistanceSorted= ArrayList<PromoModel>()
       var promoMatchedSorted= ArrayList<PromoModel>()

        lateinit var categoryHome:CategoryParse
        var run = 0
        var globalPreferePromoList  = ArrayList<PromoModel>()

        lateinit var globalUserDemographyParce:UserModelParce
        lateinit var globalUserDemography:UserModel

    }
    var callTimes = false
    var database = FirebaseFirestore.getInstance()
    var locationCount = 0
    var notifIDCounter = 102

    private var mAdapter : HomeAdapter? = null
    private var mSelected: SparseBooleanArray = SparseBooleanArray()
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var userCategories = ArrayList<CategoryParse>()

     var userModel = ArrayList<UserModelParcelable>()

    lateinit var  geoFire:GeoFire
    lateinit var  ref:DatabaseReference
    lateinit var globalBitmap:Bitmap
    var currentDate = Calendar.getInstance()
    var minimize_pref = true
    var minimize_hottes = true
    var minimize_nearb = true

    var TAG = "FragmentCategory"
    var notificationList = arrayListOf<String>("Firstnull")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragmentcategory, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        database.firestoreSettings = settings
        ref = FirebaseDatabase.getInstance().getReference("Geofences")
        geoFire = GeoFire(ref)
        val layoutManager = LinearLayoutManager(context)
        my_recycler_view111.layoutManager = layoutManager
        my_recycler_view111.itemAnimator = DefaultItemAnimator()
        swipe.setOnRefreshListener {
            promoDistanceSorted = ArrayList<PromoModel>()
            promoMatchedSorted = ArrayList<PromoModel>()
            callTimes = false
            swipe.isRefreshing = true
            promolist = ArrayList<PromoModel>()
            globalPromoList = ArrayList<PromoModel>()
            Log.e(TAG, "promolist size ${promolist.size}")
            getUserPreferredTime()


        }
        minimize_hottest.setOnClickListener {
            if (minimize_hottes) {
                minimize_hottest.setImageResource(R.mipmap.ic_arrow_blue_right)
                minimize_hottes = false
                reclerView_hottest_promo.visibility = View.GONE
            } else {
                minimize_hottes = true
                reclerView_hottest_promo.visibility = View.VISIBLE
                Log.e(TAG, "show")
                minimize_hottest.setImageResource(R.mipmap.ic_arrow_blue_down)


            }

        }
        minimize_nearby.setOnClickListener {

            if (minimize_nearb) {
                minimize_nearby.setImageResource(R.mipmap.ic_arrow_blue_right)
                minimize_nearb = false
                my_recycler_view111.visibility = View.GONE

            } else {
                minimize_nearb = true
                my_recycler_view111.visibility = View.VISIBLE
                minimize_nearby.setImageResource(R.mipmap.ic_arrow_blue_down)

                Log.e(TAG, "show")
            }

        }
        minimize_preferred.setOnClickListener {

            if (minimize_pref) {
                minimize_pref = false
                recyclerview_prefered_promo.visibility = View.GONE
                minimize_preferred.setImageResource(R.mipmap.ic_arrow_blue_right)
            } else {
                Log.e(TAG, "show")

                minimize_pref = true
                recyclerview_prefered_promo.visibility = View.VISIBLE
                minimize_preferred.setImageResource(R.mipmap.ic_arrow_blue_down)


            }
        }
        btnRefreshNearby.setOnClickListener {

            getNearbyPromo(userLatLng)
            callTimes = false
        }


        if(globalUserPreferredTime!=null){
            Log.e(TAG,"FragmentCategory saveinstantstate saved ${categoryListAll}")

            var  CategoryAdapter = CategoryHomeAdapter(activity!!,categoryListAll)

            var myStagger = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
            recyclerCategory.layoutManager = myStagger
            recyclerCategory.adapter =  CategoryAdapter

            setHomeAdapter()
            getHottestPromo()
            setPreferencePromoAdapter(globalPreferePromoList)
        }
        else {
            promoDistanceSorted = ArrayList<PromoModel>()
            promoMatchedSorted = ArrayList<PromoModel>()
            promolist = ArrayList<PromoModel>()
            getUserPreferredTime()

            getCategories()

        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    fun setPreferencePromoAdapter(preferPromoList:ArrayList<PromoModel>){
        Log.e(TAG,"setPreferedPromoAdapter")
        var myAdapter = PreferedPromoAdapter(activity!!,preferPromoList)
        promoMatchedSorted = preferPromoList
        swipe.isRefreshing = false
        recyclerview_prefered_promo.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
        recyclerview_prefered_promo.adapter = myAdapter
        minimize_preferred.setImageResource(R.mipmap.ic_arrow_blue_down)
        if(preferPromoList.size== 0 ){
            minimize_preferred.setImageResource(R.mipmap.ic_arrow_blue_right)

        }
    }


    fun getCategories() {
        var database = FirebaseFirestore.getInstance()
        categoryListAll = ArrayList<CategoryParse>()
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
                        categoryListAll.add(uploaded)

                    }








                } else
                    toast("error")
            }

                   var  CategoryAdapter = CategoryHomeAdapter(activity!!,categoryListAll)
            var myStagger = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
            recyclerCategory.layoutManager = myStagger
            recyclerCategory.adapter =  CategoryAdapter

        }
    }


fun getPromos(){
    Log.e(TAG,"getting Promos")
    database.collection("Users").document(LoginActivity.userUIDS).get().addOnSuccessListener { document ->
        if (document != null) {
            Log.e(TAG, "DocumentSnapshot data: ${document.data}")

            var userDemography = document.toObject(UserModelParcelable::class.java)
            globalUserDemographyParce = document.toObject(UserModelParce::class.java)

            if(globalUserDemographyParce.Age.toInt()<13){
                globalUserDemographyParce.Age = "Kids"

            }
            else if(globalUserDemographyParce.Age.toInt()<20 && globalUserDemographyParce.Age.toInt()>12){
                globalUserDemographyParce.Age = "Teenager"

            }
            else if(globalUserDemographyParce.Age.toInt()>=20){
                globalUserDemographyParce.Age = "Adult"

            }
            Log.e(TAG,"${userDemography.FirstName}")


            globalUserDemography = UserModel(globalUserDemographyParce.FirstName, globalUserDemographyParce.LastName, globalUserDemographyParce.Email,"*****", globalUserDemographyParce.Age, globalUserDemographyParce.Gender, globalUserDemographyParce.Status)
        } else {
            Log.e(TAG, "No such document")
        }
    }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }



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
                    Log.e(TAG,"${   upload.promoID } ${upload.promoname}")

//                    if(upload.startDateMonth!=7){
//                        doAsync{
//
//                        var deleteOrnot = database.collection("PromoDetails").document(upload.promoID).delete().isSuccessful
//                        var deleteOrnotDemo = database.collection("PromoDemography").document(upload.promoID).delete().isSuccessful
//                        var deleteOrnotCategories = database.collection("PromoCategories").document(upload.promoID).delete().isSuccessful
//                        Log.e(TAG,"Document deleted or not ${deleteOrnot}   ${deleteOrnotDemo}   ${deleteOrnotCategories} ")
//                        }
//
//                    }


                    database.collection("PromoDemography").document(upload.promoID).collection("AgeTarget").document("AgeTarget").get().addOnSuccessListener { document ->
                        if (document != null && document.exists()) {

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
    if(upload.startDateYear.equals(2020) && upload.startDateDay.equals(9) ){
        Log.e(TAG,"mga yawa pisti animal")
                promolist.add(upload)
            }
}

//
//                    if (currentDate.timeInMillis <= upload.endDateCalendar.timeInMillis)
//                    {
//
//                        if(upload.approved) {
//                            promolist.add(upload)
//                        }
//                    }
//                    else{
//                        if(upload.approved) {
//                            promolist.add(upload)
//                        }
//
//                    }
                }
                try {
                    getPreferenceMatched()

                }
                catch (e:Exception){

                }


            } else
                toast("error")
        }






}
    }
fun setHomeAdapter(){

    var sortTedPromo = promolist.sortedWith(compareBy {it.distance})

    var finalList = ArrayList<PromoModel>()
    for(i in 0 until sortTedPromo.size){
        if(i<20) {
            promoDistanceSorted.add(sortTedPromo[i])
        }
        if(sortTedPromo[i].distance.toDouble()<4.0){
            finalList.add(sortTedPromo[i])
        }
    }
    mAdapter = HomeAdapter(activity!!,mSelected, finalList)
    try {
        my_recycler_view111.adapter = mAdapter
        if(my_recycler_view111.visibility == View.VISIBLE)
        minimize_nearby.setImageResource(R.mipmap.ic_arrow_blue_down)

    }catch (e:Exception){
        print(e)
    }


}
    fun getLocation() {

        locationCount= 0
        Log.e(TAG,"getLocation")
        locationManager = activity!!.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if(locationCount<1213012) {
                    doAsync {
                        userLatLng = LatLng(location.latitude, location.longitude)

                        getNearbyPromo(userLatLng)
                        locationCount += 1


                    }


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
                            activity!!.applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            activity!!.applicationContext,
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
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0f, locationListener)
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 0f, locationListener)

            // locationManager.!!requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f,);

        } else {
            if (ActivityCompat.checkSelfPermission(
                            activity!!.applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            activity!!.applicationContext, Manifest.permission
                            .ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                return
            } else {
                //       locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
                // locationManager!!.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
                try {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, locationListener)
                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, locationListener)

                }
                catch (e:Exception){
                    Log.e(TAG,"LOL")
                }
                Log.e(TAG, "this part")
            }

        }
    }
    fun getNearbyPromo(myLatlng:LatLng){




            var myLatLng = LatLng(myLatlng.latitude, myLatlng.longitude)
            try {
                for (i in 0 until promolist.size) {


                    var distanceFormatted = CalculationByDistance(myLatLng, promolist[i].promoLocation)
                    promolist[i].distance = distanceFormatted.toString()


                }



                runOnUiThread {
                    if(!callTimes) {
                        setHomeAdapter()
                        callTimes = true
                    }

                }


            } catch (e: Exception) {
                Log.e(TAG, "Exception raised $e")
            }

        detectGeofence(GeoLocation(myLatLng.latitude, myLatLng.longitude))


    }

    fun promoOnPreferredTime( myPromo:PromoModel):Boolean{
        var TAG = true
        if (globalUserPreferredTime!=null) {
             TAG = false
            var promoStart = Calendar.getInstance()
            var promoEnd = Calendar.getInstance()
            var userPrefStart = Calendar.getInstance()
            var userPrefEnd = Calendar.getInstance()
            promoStart.set(2019, 3, 27, myPromo.startTimeHour, myPromo.startTimeMinute)
            promoEnd.set(2019, 3, 27, myPromo.endTimeHour, myPromo.endTimeMinute)
            userPrefStart.set(2019, 3, 27, globalUserPreferredTime!!.startTimeHour, globalUserPreferredTime!!.startTimeMinutes)
            userPrefEnd.set(2019, 3, 27, globalUserPreferredTime!!.endTimeHour, globalUserPreferredTime!!.endTimeMinutes)
            if (globalUserPreferredTime!!.enabled) {
                TAG = (promoStart.timeInMillis >= userPrefStart.timeInMillis && promoStart.timeInMillis <= userPrefEnd.timeInMillis
                        || promoEnd.timeInMillis >= userPrefStart.timeInMillis && promoEnd.timeInMillis <= userPrefEnd.timeInMillis)
            } else {
                TAG = true
            }

        }
return TAG
    }
    fun detectGeofence(userGeo:GeoLocation) {
        Log.e(TAG,"detectGeofence")
        Log.e(TAG," detect geofence lagi uy yawa  ${ userGeo}")

        val geoQuery = geoFire.queryAtLocation(userGeo, 1.0)
        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onKeyEntered(key: String, location: GeoLocation) {

                    for (i in 0 until promolist.size) {
                        if (promolist[i].promoID.matches(key.toRegex())) {


                            doAsync {
                                try {
                                    var srt = java.net.URL(promolist[i].promoImageLink).openStream()
                                    var bitmap = BitmapFactory.decodeStream(srt)
                                    Log.e(TAG, "GAGO " + bitmap.toString())
                                    promolist[i].promoImageBitmap = bitmap

                                    uiThread {
                                        if (promolist[i].preferenceMatched != 0) {
                                            Log.e(TAG, "preferceMatch!=0")

                                            if (promoOnPreferredTime(promolist[i])) {


                                                Log.e(TAG, "onPreferredTime")
                                                    Log.e(TAG, " else notification pushed ${promolist[i].promoname} ID  ${promolist[i].promoID}")
                                                    var convertedDistance = promolist[i].distance.toDouble() * 1000

                                                    var halfDouble = promolist[i].areaSqm.toDouble() / 2
                                                    Log.e(TAG, "Condition convertedDisntance - ${convertedDistance} and ${halfDouble}")

                                                    if (convertedDistance <= halfDouble) {

                                                        displayNotification(key, notifIDCounter, promolist[i])

                                                        var myUserHistory = userHistory(promolist[i].promoStore, currentDate.toString())
                                                        database.collection("UserHistory").document(LoginActivity.userUIDS).collection("Promo").document(promolist[i].promoStore).set(myUserHistory)
                                                        Log.e(TAG + "yoyo", "${promolist[i].promoname}  is notified yeah")
                                                    }


                                            } else {
                                                Log.e(TAG, "Not on preferred Time")
                                            }
                                        }
                                        else{

                                            Log.e(TAG, "${promolist[i].promoname} not prefered by user ${promolist[i].promoID}")

                                        }
                                    }

                                } catch (e: Exception) {

//                                    print(e)
//                                    uiThread {
//                                        if(android.os.Build.MANUFACTURER=="Allwinner") {
//                                            Log.e(TAG,"notification pushed ${promolist[i].promoStore}")
//
//                                        }
//                                        else {
//                                            try {
//                                                displayNotification(key, notifIDCounter, promolist[i])
//
//                                            }
//                                            catch (e:IndexOutOfBoundsException){
//                                                print(e)
//                                                Log.e(TAG,"IndexOutofBounds ${e}")
//                                            }
//                                        }                                    }
                                    Log.e(TAG, "walakadetect")
                                }

                            }



                        }
                        else{


                        }
                    }




            }

            override fun onKeyExited(key: String) {
              //  Log.e(TAG,"atay na onkey exited")

            }

            override fun onKeyMoved(key: String, location: GeoLocation) {
              //  Log.e(TAG,"atay na onkey moved")

            }

            override fun onGeoQueryReady() {
               // Log.e(TAG,"atay na onkey ready")


            }

            override fun onGeoQueryError(error: DatabaseError) {
                Log.d("ERROR", "" + error)
            }
        })
    }
    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {

        val Radius = 6371// radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec)
        return Radius * c
    }
    @SuppressLint("NewApi")
    internal fun createNotificationChannel(Channel: String) {
        Log.e("CreateNotification", "Created")
        val name = "personal notification"
        val description = "include all notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Channel, name, importance)
        channel.description = description
        val notificationManager = activity!!.getSystemService<NotificationManager>(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
    }
    fun getHottestPromo(){
        var length = promolist.size
        if(promolist.size>=5)
        {
            length = 5
        }
        for( i in 0 until promolist.size){

            promolist[i].hottestPoints = (promolist[i].interested * 8) + (promolist[i].viewed * 5)
            Log.e(TAG,"${promolist[i].promoStore}  points ${promolist[i].hottestPoints}")
        }

        var hottestList = ArrayList<PromoModel>()
        var sortTedPromo = promolist.sortedWith(compareByDescending {it.hottestPoints})
        for(i in 0 until length){
            Log.e(TAG,"${promolist[i].promoStore}  pointsSorted ${promolist[i].hottestPoints}")

            hottestList.add(sortTedPromo[i])
        }



        var myAdapter = HottestPromoAdapter(activity!!,hottestList)
        reclerView_hottest_promo.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
        reclerView_hottest_promo.adapter = myAdapter
        minimize_hottest.setImageResource(R.mipmap.ic_arrow_blue_down)

    }
   fun  getPreferenceNoMached(){
       getLocation()

       Log.e(TAG,"getPreferenceNoMatched ${promolist.size}")
       var preferedPromoList = ArrayList<PromoModel>()
        for(i in 0 until promolist.size){
            for(j in 0 until promolist[i].subcategories.size){
                Log.e(TAG,"PromoList size ${promolist[i].subcategories.size}")
                promolist[i].subcategories[j]
                for(k in 0 until userCategories.size){
                    Log.e(TAG,"UserCategories size ${userCategories.size}")

                    for( p in 0 until userCategories[k].Subcategories.size){
                        if(userCategories[k].Subcategories[p].SubcategoryName==promolist[i].subcategories[j]){
                            if(userCategories[k].Subcategories[p].Selected) {
                                promolist[i].preferenceMatched = promolist[i].preferenceMatched + 1
                            }
                        }
                    }
                }



            }

            Log.e(TAG,"preference match for ${promolist[i].promoname} is ${promolist[i].preferenceMatched}")
            if(promolist[i].preferenceMatched!=0){
                preferedPromoList.add(promolist[i])
            }

        }
       globalPromoList = promolist
Log.e(TAG,"Mana jud")
globalPreferePromoList = preferedPromoList
       setPreferencePromoAdapter(preferedPromoList)
           getHottestPromo()
    }
    fun getUserPreferredTime(){
            database.collection("UserPreferredTime").document(LoginActivity.userUIDS).get().addOnSuccessListener { document ->

                if (document.exists()) {

                    var promoLikeCountParce = document.toObject(userPreferredTimeParce::class.java)
                    globalUserPreferredTime = promoLikeCountParce
                    Log.e(TAG,"userPreferredTime ${promoLikeCountParce.toString()}")
                    getUserCategories()

                } else {
                    Log.e(TAG, "dont exist userPreferredTime")
                }

            }

    }
   fun getPreferenceMatched() {

       Log.e(TAG,"getPreferenceMatched ${promolist.size}")
       var count = 0


       doAsync {
           for (i in 0 until promolist.size) {

               doAsync {
                   database.collection("PromoIntrested").document(promolist[i].promoStore).get().addOnSuccessListener { document ->
                       if (document.exists()) {

                           var promoLikeCountParce = document.toObject(promoLikesCountParce::class.java)
                           var promoLikes = promoLikeCountParce.LikeCount
                           promolist[i].interested = promoLikes
                           Log.e(TAG,"${promolist[i].promoStore} likes ${promolist[i].interested}")

                       } else {
                           Log.e(TAG, "dont exist")
                       }

                   }
               }

               doAsync {
                   var mPromoViews = promoViewsParde()
                   var promoViewsCount = 0
                   database.collection("PromoViews").document(promolist[i].promoStore)
                           .get().addOnSuccessListener { document ->
                               if (document.exists()) {
                                   mPromoViews = document.toObject(promoViewsParde::class.java)
                                   Log.e(TAG, "promo views get ${mPromoViews.promoViews}")
                                   promoViewsCount = mPromoViews.promoViews
                                   promolist[i].viewed = promoViewsCount
                                   Log.e(TAG,"${promolist[i].promoStore} views ${promolist[i].viewed}")
                               }
                           }
               }

               database.collection("PromoCategories").document(promolist[i].promoCategories).collection("Categories").get().addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       for (DocumentSnapshot in task.result) {
                           var category = DocumentSnapshot.toObject(promoCategoryParce::class.java)
                           if (!promolist[i].categories.contains(category.CategoryName)) {
                               promolist[i].categories.add(category.CategoryName)
                               Log.e(TAG, "${promolist[i].promoStore} - ${category.CategoryName}")
                           }

                       }

                       Log.e(TAG, "promoCategory size ${promolist[i].promoStore} - ${promolist[i].subcategories.size} index $count equals ${promolist.size}")


                   }
               }



    database.collection("PromoCategories").document(promolist[i].promoCategories).collection("Subcategories").get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            for (DocumentSnapshot in task.result) {
                var subcategory = DocumentSnapshot.toObject(promoSubcategoryParce::class.java)
                promolist[i].subcategories.add(subcategory.SubcategoryName)
                Log.e(TAG, "${promolist[i].promoStore} - ${subcategory.SubcategoryName}")

            }

            count+=1
            Log.e(TAG, "promosubCategory size ${promolist[i].promoStore} - ${promolist[i].subcategories.size} index $count equals ${promolist.size}")
            if(count==promolist.size){
                getPreferenceNoMached()
            }

        }






    }


}



       }
   }
    fun getUserCategories() {
        doAsync {
            Log.e(TAG, "retreiving ${LoginActivity.userUIDS} Categories")
            var pistira = ArrayList<CategoryParse>()
            database.collection("UserCategories").document(LoginActivity.userUIDS).collection("Categories").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        var userCategory = document.toObject(CategoryParse::class.java)
                        Log.e(TAG, pistira.size.toString() + " totalCount retrieve ${userCategory.categoryName}")

                        pistira.add(userCategory)
                    }
                   getPromos()

                    Log.e(TAG, task.result.toString() + "   atay     " + task.isSuccessful.toString())
                    userCategories = pistira
                }


            }.addOnFailureListener {
            }

        }
    }
fun repushPromoDetails(randomUIID:String,pEntity:PromoModelBusinessman){


    database.collection("PromoDetails").document(randomUIID).set(pEntity)
    toast("Success")
}

    fun displayNotification(Channel: String, notificationID: Int,myPromoModel:PromoModel)   {

        if (!notificationList.contains(Channel)) {
            notificationList.add(Channel)
            Log.e(TAG,Channel)
            notifIDCounter += 1

            var rand = Random()
            var n = rand.nextInt(1000)
            var NotifcationID2 = myPromoModel.promoID.length * myPromoModel.promoStore.length + myPromoModel.endTimeMinute.toInt()
            Log.e("Notification test", "Succeed")


            var resultIntent = Intent(activity!!,NotificationReceiver::class.java)
            val pendingResultIntent: PendingIntent = PendingIntent.getBroadcast(activity!!, NotifcationID2,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            resultIntent.putExtra("message",myPromoModel)
            resultIntent.putExtra("boolean",true)
            resultIntent.putExtra("sure","yawa")
            resultIntent.putExtra("notificationID",NotifcationID2)





            var actionIntent = Intent(activity!!, NotificationReceiver::class.java)
            val actionPendingIntent: PendingIntent = PendingIntent.getBroadcast(activity!!, NotifcationID2,actionIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            actionIntent.putExtra("message",myPromoModel)
            actionIntent.putExtra("boolean",false)
            actionIntent.putExtra("sure1","pisti")

            actionIntent.putExtra("notificationID",NotifcationID2)

            var actionIntent1 = Intent(activity!!, NotificationReceiverDismiss::class.java)
            val actionPendingIntent1: PendingIntent = PendingIntent.getBroadcast(activity!!, NotifcationID2,actionIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            actionIntent1.putExtra("message",myPromoModel)
            actionIntent.putExtra("notificationID",NotifcationID2)



            val normal_layout = RemoteViews(activity!!.packageName, R.layout.notification_fence_normal)
            val expanded_layout = RemoteViews(activity!!.packageName, R.layout.notification_fence_expanded)

            normal_layout.setTextViewText(R.id.tv_notifstore, myPromoModel.promoStore + " is having a promo")
            expanded_layout.setTextViewText(R.id.tv_notifstore2, myPromoModel.promoStore + " is having a promo")
            expanded_layout.setTextViewText(R.id.tv_notif_description, myPromoModel.promodescription)

            try {

                expanded_layout.setImageViewBitmap(R.id.iv_notifpromoimage, myPromoModel.promoImageBitmap)
            } catch (e: Exception) {

                print(e)
            }
            createNotificationChannel(Channel)

            val builder = NotificationCompat.Builder(activity!!, Channel)
            builder.setSmallIcon(R.drawable.hyperdealslogofinal)
            builder.priority = NotificationCompat.PRIORITY_DEFAULT
            builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
            builder.setCustomContentView(normal_layout)
            builder.setCustomBigContentView(expanded_layout)
            builder.setContentIntent(pendingResultIntent)
            builder.setAutoCancel(true)
            builder.color = Color.BLUE
            builder.setOnlyAlertOnce(true)
            builder.addAction(R.drawable.hyperdealslogofinal, "Interested", actionPendingIntent)
            builder.addAction(R.drawable.hyperdealslogofinal, "Dismiss", actionPendingIntent1)

            val notificationManagerCompat = NotificationManagerCompat.from(activity!!)
            notificationManagerCompat.notify(NotifcationID2, builder.build())

        }
    }

}






