package com.example.kent.hyperdeals.FragmentsBusiness

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.Model.PromoModel
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.MyAdapters.RelatedPromoAdapter
import com.example.kent.hyperdeals.R
import kotlinx.android.synthetic.main.fragment_related_promos.*


class FragmentRelatedPromos : Fragment() {
    lateinit var currentPromo:PromoModel
     var relatedPromoList = ArrayList<PromoModel>()
    var  localList  = ArrayList<PromoModel>()
    val TAG = "FragRelated"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_related_promos, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        relatedPromoList  = ArrayList<PromoModel>()
        for(i in 0 until FragmentCategory.globalPromoList.size){
            if(PromoListAdapter.promoProfile.promoStore==FragmentCategory.globalPromoList[i].promoStore)
            {
                currentPromo = FragmentCategory.globalPromoList[i]
            }
    }
        Log.e(TAG,"current promo ${currentPromo.promoStore}")

       localList = FragmentCategory.globalPromoList

        getRelatedMatch(localList)
    }


    fun getRelatedMatch(localList:ArrayList<PromoModel>){
        for(i in 0 until localList.size){
            localList[i].relatedPromoMatched = 0
            for(j in 0 until localList[i].subcategories.size){
                for(k in 0 until currentPromo.subcategories.size){
                    if(localList[i].subcategories[j]==currentPromo.subcategories[k]){
                        localList[i].relatedPromoMatched += 1
                    } } }
            if(currentPromo.promoStore==localList[i].promoStore)
            {
            }
            else{
                if (localList[i].relatedPromoMatched!=0)
                relatedPromoList.add(localList[i])

            }
            Log.e(TAG,"${localList[i].promoStore}  ${localList[i].relatedPromoMatched}")

        }

        var myAdapter = RelatedPromoAdapter(activity!!,relatedPromoList)
        recyclerSimilar.layoutManager = LinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
        recyclerSimilar.adapter = myAdapter
    }
}
