package com.example.kent.hyperdeals.MyAdapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.promoItemParcelable
import com.example.kent.hyperdeals.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.model_sale_item.view.*


class PromoItemAdapterBusinessman (var context: Context, var itemList : ArrayList<promoItemParcelable>) : RecyclerView.Adapter<PromoItemAdapterBusinessman.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.model_sale_item,parent,false))

    override fun getItemCount(): Int = itemList.size


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
    var myItem = itemList[position]


        holder.itemName.text = myItem.itemName
        holder.itemOldPrice.text = myItem.itemOldPirce.toString()
        holder.itemNewPrice.text = myItem.itemNewPrice.toString()
        Picasso.get()
                .load(myItem.itemImageLink)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(holder.image)


    }



    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){


        var image = view.imageView6
        var itemName = view.textView19
        var itemOldPrice = view.textView21
        var itemNewPrice = view.tv_nPrice


    }


}