package com.example.kent.hyperdeals.MyAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.SubcategoryParse
import com.example.kent.hyperdeals.NavigationOptionsActivity.UserPreference
import com.example.kent.hyperdeals.R
import kotlinx.android.synthetic.main.model_subcategory_businessman.view.*


class SubcategoryAdaperUser (var context: Context, var subcategoryList : ArrayList<SubcategoryParse>, var myPosition:Int) : RecyclerView.Adapter<SubcategoryAdaperUser.ViewHolder>(){
    var myList = subcategoryList


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        //   val btn_subcat = view.btn_subcategory!!

        val btn_subcat =view.btn_subcategory!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.model_subcategory_businessman,parent,false))

    override fun getItemCount(): Int = subcategoryList.size


    override fun onBindViewHolder(holder: SubcategoryAdaperUser.ViewHolder, position: Int) {
        val mysubCategory = subcategoryList[position]

        Log.e(TAG,mysubCategory.Selected.toString())
        try {
            holder.btn_subcat.text = mysubCategory.SubcategoryName
        }
        catch(e:Exception){
            print(e)
        }

        if(mysubCategory.Selected)
        {
            holder.btn_subcat.setBackgroundResource(R.drawable.subcategory_shape_selected)
            holder.btn_subcat.setTextColor(context.resources.getColor(R.color.white))

        }

        holder.btn_subcat.setOnClickListener {
            if(subcategoryList[position].Selected){
                subcategoryList[position].Selected = false
                UserPreference.categoryList[myPosition].Subcategories[position].Selected = false
                 PreferenceChangeAdapter.categoryList1[myPosition].Subcategories[position].Selected = false

                holder.btn_subcat.setBackgroundResource(R.drawable.subcategory_shape)
                holder.btn_subcat.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))


            }
            else{
                subcategoryList[position].Selected = true
                UserPreference.categoryList[myPosition].Subcategories[position].Selected = true
                PreferenceChangeAdapter.categoryList1[myPosition].Subcategories[position].Selected = true

                holder.btn_subcat.setBackgroundResource(R.drawable.subcategory_shape_selected)
                holder.btn_subcat.setTextColor(context.resources.getColor(R.color.white))

            }


        }


    }





}