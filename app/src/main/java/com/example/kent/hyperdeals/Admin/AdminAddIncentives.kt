package com.example.kent.hyperdeals.Admin


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.MyAdapters.TAG
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.admin_add_incentives.*
import kotlinx.android.synthetic.main.fragmentpromaplist.*
import org.jetbrains.anko.support.v4.toast

class AdminAddIncentives: Fragment(){


    private var rewardslist = ArrayList<AdminRewardsModelToRetrieve>()
    private var mAdapter : AdminAddIncentivesAdapter? = null
    private var mSelected: SparseBooleanArray = SparseBooleanArray()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_add_incentives,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addincentives.setOnClickListener{
                val intent = Intent (this.context,AdminAddIncentivesInputs::class.java)
                startActivity(intent)
        }


        rewardslist = ArrayList()


        val database = FirebaseFirestore.getInstance()

        val layoutManager = LinearLayoutManager(context)
        adminincentivesrecyclerview.layoutManager = layoutManager
        adminincentivesrecyclerview.itemAnimator = DefaultItemAnimator()

        database.collection("Rewards").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (DocumentSnapshot in task.result) {
                    val upload = DocumentSnapshot.toObject(AdminRewardsModelToRetrieve::class.java)
                    Log.d(TAG, DocumentSnapshot.getId() + " => " + DocumentSnapshot.data)
                    rewardslist.add(upload)
                    toast("success")

                    mAdapter = AdminAddIncentivesAdapter(mSelected, rewardslist)
                    adminincentivesrecyclerview.adapter = mAdapter
                }

            } else
                toast("error")
        }

    }
}