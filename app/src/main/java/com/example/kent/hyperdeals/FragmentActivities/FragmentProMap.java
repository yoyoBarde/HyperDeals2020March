package com.example.kent.hyperdeals.FragmentActivities;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kent.hyperdeals.FragmentsBusiness.Business_PromoProfile;
import com.example.kent.hyperdeals.LoginActivity;
import com.example.kent.hyperdeals.Model.PromoModel;
import com.example.kent.hyperdeals.Model.subsubModel;
import com.example.kent.hyperdeals.Promo_Detail;
import com.example.kent.hyperdeals.R;



import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class FragmentProMap extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {
    public Boolean globalFirst = true;
    public static String keyoo;
    private GoogleMap mMap;
    private Circle mCircle;

    //Play Service Location
    private static final int MY_PERMISSION_REQUEST_CODE = 7192;
    private static final int PLAY_SERVICE_RESULATION_REQUEST = 300193;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient nGoogleApiClient;
    private Location mLastLocaiton;
    private static final int NOTIFICATION_ID_OPEN_ACTIVITY=9;
    private static int UPDATE_INTERVAL = 2000;
    private static int FATEST_INTERVAL = 1000;
    private static int DISPLACEMENT = 10;
    int count = 0;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));
    final static String TAG="Promap";
    Bitmap globalBitmap;
    View mView;
    GeoLocation userGeo;
    DatabaseReference ref;
    GeoFire geoFire;
    Marker mCurrent;
    VerticalSeekBar mVerticalSeekBar;
    MapView mMapView;
    ImageView btnSearch;
    Context context;
  ArrayList<subsubModel> arraysubsub=new ArrayList<subsubModel>();
    ArrayList<PromoModel> globalPromoList;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    private AutoCompleteTextView mSearchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView =inflater.inflate(R.layout.fragment_fragment_pro_map, container, false);
        return  mView;


    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG,"OnViewCreated");
        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
        ref = FirebaseDatabase.getInstance().getReference("Geofences");
        geoFire = new GeoFire(ref);
        mVerticalSeekBar = (VerticalSeekBar)getView().findViewById(R.id.verticalSeekBar);
        mVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(progress), 1500, null);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSearchText = (AutoCompleteTextView) getView().findViewById(R.id.input_search);
        btnSearch = (ImageView) getView().findViewById(R.id.imageView8);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"onClick");
                geoLocate();
            }
        });

        setUpdateLocation();


    }
    private void geoLocate(){
    Log.e(TAG,"geoLocate");
    String searchString = mSearchText.getText().toString();
    Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString,1);
        }
        catch (IOException e){
            Log.e(TAG,"Geolocate IOEXCEPTION"+e.getMessage());
        }

        if(list.size()>0){


            Address address = list.get(0);
            Log.e(TAG,address.toString());
            LatLng coordinate = new LatLng(address.getLatitude(),address.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 12);
            mMap.animateCamera(yourLocation);
        }

    }
    private void init(Context context){
        nGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(),mGoogleApiClient,LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
    mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            Log.e(TAG,"atay razz");
            if(i == EditorInfo.IME_ACTION_SEARCH || i ==EditorInfo.IME_ACTION_DONE || keyEvent.getAction()==KeyEvent.ACTION_DOWN
            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
            ){

                Log.e(TAG,"search map");
            geoLocate();
            }

            return false;
        }
    });
    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {


        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.e(TAG,"onMapReady");
       new GetImageFromURL().execute("asd");
displayLocation();
//add GeoQuery here
        Log.e(TAG,"onMapReady");
        init(getActivity());


    }
    public void putMarkerImage(final Bitmap mybitmap,GeoPoint mygeo,LatLng mypromoGeo,String promoName){

        final Bitmap resizedBitmap = Bitmap.createScaledBitmap(mybitmap, 50, 50, true);

        mMap.addMarker(new MarkerOptions().title(promoName)
                .position(mypromoGeo)
                .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
        mMap.setOnMarkerClickListener(this);

    }
    public void drawCircleMarker(LatLng position,double areaSqm){
        Log.e(TAG,"markerCircle");
        double radiusInMeters = areaSqm/2;
        //red outline
        int strokeColor = 0xffff0000;
        //opaque red fill
        int shadeColor = 0x44ff0000;


        CircleOptions circleOptions = new CircleOptions()
                .center(position)
                .radius(radiusInMeters)
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(2);
        mCircle = mMap.addCircle(circleOptions);


    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        count+=1;
        try {
            mCircle.remove();
        }
        catch(Exception e){
            Log.e(TAG,e.toString());

        }
        Log.e(TAG,marker.getTitle()+ "yoyo");
        int position =0 ;
        for (int i = 0; i < FragmentCategory.globalPromoList.size(); i++) {
            Log.e(TAG, marker.getTitle() + "  " + FragmentCategory.globalPromoList.get(i).getPromoname());
            if (marker.getTitle().equals(FragmentCategory.globalPromoList.get(i).getPromoname())) {
                drawCircleMarker(FragmentCategory.globalPromoList.get(i).getPromoLocation(),FragmentCategory.globalPromoList.get(i).getAreaSqm());

                position = i;
                Log.e(TAG, "Matched");


            }


        }

        if(count==2) {
            showDialog(FragmentCategory.globalPromoList.get(position),getActivity());

            count=0;
        }
        return false;
    }
    void showDialog(final PromoModel myPromoModel, final Context context) {



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.zzhomelistitem, null);

        dialogBuilder.setCancelable(true);

        dialogBuilder.setView(dialogView);

        DecimalFormat df = new DecimalFormat("#.##");
        ImageView promoImage = dialogView.findViewById(R.id.homePromoImage);
        TextView promoStore = dialogView.findViewById(R.id.homePromoStore);
        TextView promoname = dialogView.findViewById(R.id.homePromoName);
        TextView promoDistance = dialogView.findViewById(R.id.tv_distance);
        ConstraintLayout container = dialogView.findViewById(R.id.constraint_holder);
        Picasso.get()
                .load(myPromoModel.getPromoImageLink())
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(promoImage);

        promoStore.setText(myPromoModel.getPromoStore());
        promoname.setText(myPromoModel.getPromoname());
        String distanceFinal =df.format(Double.valueOf(myPromoModel.getDistance()));
        promoDistance.setText(distanceFinal);
        AlertDialog b = dialogBuilder.create();

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, Business_PromoProfile.class);
                myIntent.putExtra("object",myPromoModel);
                startActivity(myIntent);

            }
        });

        b.show();
        b.setCancelable(true);



    }

    public class GetImageFromURL2 extends AsyncTask<String,Void,Bitmap> {
        String Url ;
        GeoPoint mygeo;
        LatLng PromoGeo;
        String PromoName;


        public void OnNewSensorData(final Bitmap myBitmap, final GeoPoint myGeom, final LatLng myPromoGeo, final String myPromoName) {
            runOnUiThread(new Runnable() {
                public void run() {
                    putMarkerImage(myBitmap,myGeom,myPromoGeo,myPromoName);
                }
            });
        }
        @Override
        protected Bitmap doInBackground(String... url) {

            String urldisplay =url[0];
            Bitmap bitmaps = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(srt);
                bitmaps = myBitmap;
                OnNewSensorData(myBitmap,mygeo,PromoGeo,PromoName);
            } catch (Exception e){
                Log.d(TAG,"failed");
                e.printStackTrace();
            }

            return bitmaps;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }

    public class GetImageFromURL extends AsyncTask<String,Void,Bitmap> {


        @Override
        protected Bitmap doInBackground(String... url) {

            Log.e(TAG,"doInBackground");
            Bitmap bitmaps = null;
            String myUrl="default";
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("PromoDetails")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Boolean aprroved = document.getBoolean("approved");
//                                    if(!aprroved) {
                                        String Url = document.getString("promoImageLink");
                                        GetImageFromURL2 myImageUrl = new GetImageFromURL2();
                                        GeoPoint mygeo = document.getGeoPoint("promoGeo");
                                        LatLng PromoGeo = new LatLng(mygeo.getLatitude(), mygeo.getLongitude());
                                        String PromoName = document.getString("promoname");
                                        myImageUrl.mygeo = mygeo;
                                        myImageUrl.PromoGeo = PromoGeo;
                                        myImageUrl.PromoName = PromoName;
                                        myImageUrl.execute(Url);
                                 //   }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


            return bitmaps;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }


    public void detectGeofence(){

        GeoQuery geoQuery = geoFire.queryAtLocation(userGeo, 20);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.e("OnkeyEnted ",key );

                getGeofenceDetails(key);


            }

            @Override
            public void onKeyExited(String key) {
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d("ERROR", ""+error);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayService()) {
                        createLocationRequest();
                        buildGoogleApiClient();
displayLocation();
                    }
                }
                break;
        }

    }

    private void setUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayService()) {
                buildGoogleApiClient();
                createLocationRequest();
displayLocation();
            }
        }
    }
    public  class disPlayLocationAsync extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            displayLocation();
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }


        mLastLocaiton = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocaiton != null) {
            final double latitude = mLastLocaiton.getLatitude();
            final double longitude = mLastLocaiton.getLongitude();
            userGeo = new GeoLocation(latitude,longitude);
            if (mCurrent != null)
                mCurrent.remove();
            runOnUiThread(new Runnable() {
                public void run() {
                    mCurrent = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("You"));
                    Log.e(TAG,"Marker");
                }
            });

                LatLng coordinate = new LatLng(latitude, longitude);

            if(globalFirst) {
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 12);
                mMap.animateCamera(yourLocation);
                globalFirst = false;
            }
//
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, latitude), 12.0f));
            //   }
            //  });

//            detectGeofence();
            Log.d(TAG, String.format("Your last location was chaged: %f / %f", latitude, longitude));
        } else {
            Log.d(TAG, "Can not get your location.");
        }
    }
//    private void getLocation() {
//                LocationManager locationManager = null;
//        LocationListener locationListener = null;
//        Log.e(TAG,"getLocation");
//        locationManager = getActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) (LocationManager)
//        locationManager = this!!.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
//        locationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//
//
//            }
//
//            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
//
//            }
//
//            override fun onProviderEnabled(provider: String) {
//
//            }
//
//            override fun onProviderDisabled(provider: String) {
//
//            }
//        }
//        if (Build.VERSION.SDK_INT < 23) {
//            if (ActivityCompat.checkSelfPermission(
//                    this!!.applicationContext,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this!!.applicationContext,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//
//            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
//            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
//
//            // locationManager.!!requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f,);
//
//        } else {
//            if (ActivityCompat.checkSelfPermission(
//                    this!!.applicationContext,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                    this!!.applicationContext, Manifest.permission
//                    .ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(this!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//                return
//            } else {
//                //       locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
//                // locationManager!!.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
//                try {
//                    locationManager!!.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f, locationListener)
//                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
//
//                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
//
//                }
//                catch (e: Exception){
//                    Log.e(TAG,"LOL")
//                }
//                Log.e(TAG, "this part")
//            }
//
//        }
//    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);


    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();


    }

    private boolean checkPlayService() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this.getContext());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this.getActivity(), result, PLAY_SERVICE_RESULATION_REQUEST).show();
            } else {
                Toast.makeText(this.getActivity(), "This Device is not supported.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void sendNotification(String title, String content,String key) {
        Intent intent = new Intent(this.getContext(), Promo_Detail.class);
        if(key!=null)
            saveInfos(key);
        PendingIntent contentIntent = PendingIntent.getActivity(this.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder builder = new Notification.Builder(this.getContext())
                .setSmallIcon(R.drawable.hyperdealslogo)
                .setContentTitle(title)
                .setContentText(content);

        NotificationManager manager = (NotificationManager)this.getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        manager.notify(new Random().nextInt(), notification);
    }
    private void saveInfos(String key){

        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("userInfo",this.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key",key);
        editor.apply();
        Log.d(TAG,"Shared Preferencedd");
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocaiton = location;
        runOnUiThread(new Runnable() {
            public void run() {
                 mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLastLocaiton.getLatitude(), mLastLocaiton.getAltitude()))
                        .title("You"));
                Log.e(TAG,"Marker on Location change");
            }
        });

displayLocation();
Log.e(TAG,"onLocationChange "+location.toString() );




    }

    private void getGeofenceDetails(final String key){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("PromoDetails").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String storeName = document.getString("promoStore");
                        String promoName = document.getString("promoname");
                        GeoPoint promoLocation = document.getGeoPoint("promoGeo");
                        String subsubTag = document.getString("subsubTag");
                            sendNotification(storeName, promoName, key);
                        Log.d(TAG, storeName+promoName+key);
                        Log.d(TAG, "There such document");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        Log.d(TAG,"get geofence");
    }
    private void geoUserPreference(){
        String userID=LoginActivity.Companion.getUserUIDS();
        Log.d(TAG,userID+"asdkmadkmas");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userID).collection("Cetegories").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String subsubCategory=document.getString("subsubcategoryName");
                                Boolean clicked=document.getBoolean("checked");
                                arraysubsub.add(new subsubModel(clicked,subsubCategory));
                                Log.d(TAG,subsubCategory+clicked.toString());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });




    }


}

