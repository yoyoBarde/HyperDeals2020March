package com.example.kent.hyperdeals.MyAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.kent.hyperdeals.Admin.AddCategoryInput
import com.example.kent.hyperdeals.EditCategory
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.NavigationOptionsActivity.UserPreference
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_model_preferences.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.uiThread


    class PreferenceChangeAdapterAdmin (var context: Context, var categoryList : ArrayList<CategoryParse>) : RecyclerView.Adapter<PreferenceChangeAdapterAdmin.ViewHolder>(){
    companion object{
        var categoryList1 = ArrayList<CategoryParse>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_model_preferences,parent,false))

    override fun getItemCount(): Int = categoryList.size


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var myCategory = categoryList[position]


        Picasso.get()
                .load(myCategory.CategoryImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(holder.profile_pic)
        holder.profile_pic.setOnClickListener {
            categoryList1=categoryList

            val intent = Intent (context, EditCategory::class.java)
            intent.putExtra("request","PreferenceChangeAdapterAdmin")
            intent.putExtra("category",myCategory)
            context.startActivity(intent)

        }
        holder.title.text = myCategory.categoryName


         holder.no_selected.text = "${myCategory.Subcategories.size} subcategories"




        holder.ivClose.setOnClickListener{

        }


    }



    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var title = view.tv_categoryName
        var no_selected = view.tv_no_selected
        var profile_pic = view.profile_image
        var ivClose = view.iv_close
    }






}