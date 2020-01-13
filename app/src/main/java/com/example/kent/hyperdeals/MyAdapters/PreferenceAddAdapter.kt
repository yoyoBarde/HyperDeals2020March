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

class PreferenceAddAdapter (var context: Context, var categoryList : ArrayList<CategoryParse>) : RecyclerView.Adapter<PreferenceAddAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: PreferenceAddAdapter.ViewHolder, position: Int) {
        var myCategory = categoryList[position]

        Picasso.get()
                .load(myCategory.CategoryImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(holder.profile_pic)

        holder.title.text = myCategory.categoryName
        holder.no_selected.text = myCategory.Subcategories.size.toString() + " Subcategories"







    holder.cardview.setOnClickListener {
        if (!holder.cb_check!!.isChecked) {

            holder.profile_pic.borderColor = context.resources.getColor(R.color.colorPrimary)
            categoryList[position].SelectedAll = true
            holder.cb_check!!.isChecked = true
            Log.e(TAG, "itemtrue")

        } else {
            holder.profile_pic.borderColor = context.resources.getColor(R.color.silver)
            categoryList[position].SelectedAll = false
            holder.cb_check!!.isChecked = false
            Log.e(TAG, "itemfalse")

        }
    }
    }

    var myList = categoryList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_model_categories, parent, false))

    override fun getItemCount(): Int = categoryList.size





    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var title = view.tv_categoryName!!
        var no_selected = view.tv_no_selected!!
        var profile_pic = view.profile_image!!
        var cb_check = view.checkBox_Category!!
        var cardview = view.cardview!!
    }
}