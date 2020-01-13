package com.example.kent.hyperdeals.MyAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.CategoryParse
import com.example.kent.hyperdeals.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_model_categories.view.*


var TAG = "CategoryAdapter"





class CategoryAdapter (var context: Context,  var categoryList : ArrayList<CategoryParse>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

 var myList = categoryList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_model_categories,parent,false))

    override fun getItemCount(): Int = categoryList.size


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

        val myCategory = categoryList[position]


        Picasso.get()
                .load(myCategory.CategoryImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(holder.myProfileImage)


        holder.categoryCheckbox.isChecked = myCategory.SelectedAll
        holder.myCategoryHolder.text = myCategory.categoryName
        holder.cardView.setOnClickListener {

            if (!holder.categoryCheckbox.isChecked){

                holder.myProfileImage.borderColor = context.resources.getColor(R.color.colorPrimary)
                categoryList[position].SelectedAll = true
                holder.categoryCheckbox.isChecked = true
                Log.e(TAG,"itemClicked")

            }
            else{
                holder.myProfileImage.borderColor = context.resources.getColor(R.color.silver)
                categoryList[position].SelectedAll = false
                holder.categoryCheckbox.isChecked = false
                Log.e(TAG,"itemClicked")

            }
        }

    }



    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        var myCategoryHolder = view.tv_categoryName!!
        var myProfileImage = view.profile_image!!
        var cardView = view.cardview!!
        var categoryCheckbox = view.checkBox_Category!!

    }


}