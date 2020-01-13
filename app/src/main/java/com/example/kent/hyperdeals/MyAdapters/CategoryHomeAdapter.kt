package com.example.kent.hyperdeals.MyAdapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.CategoryHome
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.Model.CategoryParse
import com.example.kent.hyperdeals.Model.SubcategoryParse
import com.example.kent.hyperdeals.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.model_categories.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class CategoryHomeAdapter (var context: Context, var categoryList : ArrayList<CategoryParse>) : RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>(){


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var categoryNamee = view.tv_categoryName
        var categoryImage = view.ivCategory

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.model_categories,parent,false))

    override fun getItemCount(): Int = categoryList.size


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var myCategory = categoryList[position]

        holder.categoryNamee.text = myCategory.categoryName
        doAsync {
            uiThread {
                Picasso.get()
                        .load(myCategory.CategoryImage)
                        .placeholder(R.drawable.hyperdealslogofinal)
                        .into(holder.categoryImage)
            }
        }

        holder.categoryImage.setOnClickListener {

            FragmentCategory.categoryHome = myCategory
            context.startActivity(Intent(context,CategoryHome::class.java))

        }


    }





}