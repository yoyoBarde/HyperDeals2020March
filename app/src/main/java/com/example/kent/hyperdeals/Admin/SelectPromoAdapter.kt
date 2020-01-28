package com.example.kent.hyperdeals.Admin


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notification_layout_row.view.*
import java.util.*

class SelectPromoAdapter(val context:Context , private val promolist : ArrayList<PromoModel>) : RecyclerView.Adapter<SelectPromoAdapter.ViewHolder>(){
    companion object {
        lateinit  var promoProfile: PromoModel
    }
    val database = FirebaseFirestore.getInstance()
    val TAG = "PromoListAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):

            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notification_layout_row,parent,false))


    override fun getItemCount(): Int  = promolist.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val promos = promolist[position]


        Picasso.get()
                .load(promos.promoImageLink)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(holder.ivPromoImage)

        /* holder.ivPromoImage.setImageResource(R.drawable.bench)*/

        holder.tvPromoTitile.text = promos.promoname
        holder.tvPromoDescription.text = promos.promodescription
        holder.tvPromoLocation.text = promos.promoPlace
        holder.tvPromoContact.text = promos.promoContactNumber
        holder.tvPromoStore.text = promos.promoStore

        holder.container.setOnClickListener {


            var myInterface =  DataControl() as (myInterfaceSelectPromo)
            DataControl.selectedPromoo = promolist[position]

            myInterface.selectedPromo(promolist[position])

        }



    }



    inner class ViewHolder (view:View): RecyclerView.ViewHolder(view){
        val ivPromoImage = view.PromoImage!!
        val tvPromoTitile = view.PromoTitle!!
        val tvPromoDescription = view.PromoDescription!!
        val tvPromoLocation = view.PromoPlace!!
        val tvPromoContact = view.PromoConctact!!
        val tvPromoStore = view.PromoStore!!




        val container = view.PromoContainer!!
    }





}