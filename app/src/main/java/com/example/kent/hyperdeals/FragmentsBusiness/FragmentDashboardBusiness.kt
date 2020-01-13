package com.example.kent.hyperdeals.FragmentsBusiness

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.promoLikesCountParce
import com.example.kent.hyperdeals.Model.promoViewsParde
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_fragment_dashboard_business.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class FragmentDashboardBusiness : Fragment() {
val TAG = "fragmentBusiness"
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


        var database = FirebaseFirestore.getInstance()

        doAsync {

     database.collection("PromoIntrested").document(PromoListAdapter.promoProfile.promoStore).
                get().addOnSuccessListener { document ->

            if(document.exists())
            {

                var promoLikeCountParce = document.toObject(promoLikesCountParce::class.java)
                var promoLikes = promoLikeCountParce.LikeCount
                uiThread { tv_interest_count.text = promoLikes.toString() }
                Log.e(TAG,"promolikes ${promoLikes}")
            }
            else{
                Log.e(TAG,"dont exist")
            }

        }
        }



        doAsync {
                var mPromoViews = promoViewsParde()
                var promoViewsCount = 0
                database.collection("PromoViews").document(PromoListAdapter.promoProfile.promoStore)
                        .get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                mPromoViews = document.toObject(promoViewsParde::class.java)
                                Log.e(TAG, "promo views get ${mPromoViews.promoViews}")
                                promoViewsCount = mPromoViews.promoViews
                                uiThread { tv_promo_views.text  = promoViewsCount.toString() }
                            }
            }
        }



    }
}
