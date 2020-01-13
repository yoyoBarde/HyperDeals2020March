package com.example.kent.hyperdeals

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.SparseBooleanArray
import android.widget.LinearLayout
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.Model.CategoryParse
import com.example.kent.hyperdeals.Model.PromoModel
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_category_home.*

class CategoryHome : AppCompatActivity() {
val TAG = "CategoryHome"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_home)

        var myCategory = FragmentCategory.categoryHome
        tv_categoryName.text = myCategory.categoryName
        Picasso.get()
                .load(myCategory.CategoryImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(ivCategory)

        ic_back.setOnClickListener {
            finish()
        }




            getPromos(myCategory)
    }
    fun getPromos(myCategory:CategoryParse){

        var myPromoList = FragmentCategory.globalPromoList
        var filteredPromoList = ArrayList<PromoModel>()
        for(i in 0 until myPromoList.size){
//        for(w in 0 until myPromoList[i].subcategories.size){
//            Log.e(TAG,"Subcategories for ${myPromoList[i].promoname} ${myPromoList[i].subcategories[w]}")
//        }

            for(j in 0 until  myCategory.Subcategories.size){
                var matchalready = false
                for(k in 0 until myPromoList[i].subcategories.size){

                    if(myCategory.Subcategories[j].SubcategoryName.matches(myPromoList[i].subcategories[k].toRegex())){
                        filteredPromoList.add(myPromoList[i])
                        matchalready = true
                        break

                    }
                    if(matchalready){
                        break
                    }
                }
                if(matchalready){
                    break
                }
            }




        }
        Log.e(TAG," size ${filteredPromoList.size}")
         var mSelected: SparseBooleanArray = SparseBooleanArray()

        var myAdapter = PromoListAdapter(this,mSelected, filteredPromoList)
        recyclerPromo.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recyclerPromo.adapter = myAdapter
//        var  CategoryAdapter = CategoryHomeAdapter(activity!!,categoryListAll)
//        var myStagger = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
//        recyclerCategory.layoutManager = myStagger
//        recyclerCategory.adapter =  CategoryAdapter
        for (i in 0 until filteredPromoList.size){
            Log.e(TAG, "promo filtered  "+filteredPromoList[i].promoname)
        }

    }
}
