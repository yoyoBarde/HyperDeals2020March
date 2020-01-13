package com.example.kent.hyperdeals.Admin

import android.content.Context
import android.util.SparseBooleanArray
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Home.HomeAdapter
import com.example.kent.hyperdeals.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.admin_add_incentives_inputs.view.*
import kotlinx.android.synthetic.main.admin_add_incentives_layout_list.view.*
import kotlinx.android.synthetic.main.zzhomelistitem.view.*


class AdminAddIncentivesAdapter(private val selectedItem: SparseBooleanArray,
                                 private val rewardslist : ArrayList<AdminRewardsModelToRetrieve>) : RecyclerView.Adapter<AdminAddIncentivesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        AdminAddIncentivesAdapter.ViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_add_incentives_layout_list,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val rewards = rewardslist[position]

        Picasso.get()
                .load(rewards.adminIncentiveImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(holder.adminaddincentivesimage)

        holder.adminaddincentivesrewardsname.text = rewards.adminIncentiveRewardName
        holder.adminaddincentivesrewardspoints.text = rewards.adminIncentiveRewardPoints
    }

    override fun getItemCount(): Int = rewardslist.size



    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val adminaddincentivesimage = view.adminincentiveslayoutlistimage!!
        val adminaddincentivesrewardsname = view.adminincentiveslayoutlistrewardname!!
        val adminaddincentivesrewardspoints = view.adminincentiveslayoutlistrewardpoints!!

    }
}