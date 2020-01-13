package com.example.kent.hyperdeals.Admin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.PromoModel
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_admin_aprrove_promo.*
import kotlinx.android.synthetic.main.fragmentpromaplist.*
import org.jetbrains.anko.support.v4.toast
import java.util.*

class AdminApprovePromoFragment : Fragment() {
    val database = FirebaseFirestore.getInstance()
    val TAG = "AdminAprrove"
    var currentDate = Calendar.getInstance()

    private var mSelected: SparseBooleanArray = SparseBooleanArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_aprrove_promo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPromoTobeAprroved()
    }


    fun getPromoTobeAprroved(){
     var promolist = ArrayList<PromoModel>()
        val layoutManager = LinearLayoutManager(context)
        recyclerPendingPromo.layoutManager = layoutManager
        recyclerPendingPromo.itemAnimator = DefaultItemAnimator()
        database.collection("PendingPromoDetails").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (DocumentSnapshot in task.result) {
                    val upload = DocumentSnapshot.toObject(PromoModel::class.java)
                    Log.d(TAG, DocumentSnapshot.getId() + " => " + DocumentSnapshot.getData())
                    var geoPoint = DocumentSnapshot.getGeoPoint("promoGeo")
                    upload.promoLocation = LatLng(geoPoint.latitude, geoPoint.longitude)
                    upload.startDateCalendar.set(upload.startDateYear, upload.startDateMonth - 1, upload.startDateDay)
                    upload.endDateCalendar.set(upload.endDateYear, upload.endDateMonth - 1, upload.endDateDay)
                    upload.promoID = DocumentSnapshot.id
                    Log.e(TAG,"areaSqm ${upload.areaSqm}")

                    if (currentDate.timeInMillis <= upload.endDateCalendar.timeInMillis)
                    {

                        if(!upload.approved) {
                            promolist.add(upload)
                        }
                    }
                    toast("success")

                   var mAdapter = PendingPromoAdapter(activity!!,promolist)
                    recyclerPendingPromo.adapter = mAdapter

                }

            } else
                toast("error")
        }
    }

}
