package com.example.kent.hyperdeals.Admin

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.SubcategoryModelx
import com.example.kent.hyperdeals.R
import kotlinx.android.synthetic.main.admin_add_incentives_layout_list.view.*
import kotlinx.android.synthetic.main.admin_subcategory_list.view.*

class AdminAddCategoryAdapter (private val selectedItem: SparseBooleanArray,  private val subcategorylsit : ArrayList<SubcategoryModelx>)
    : RecyclerView.Adapter<AdminAddCategoryAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            AdminAddCategoryAdapter.ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_subcategory_list,parent,false))



    override fun getItemCount(): Int = subcategorylsit.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.admin_subcategory_list.text = subcategorylsit[position].SubcategoryName
    }


    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val admin_subcategory_list = view.admin_subcategory_list!!

    }
}