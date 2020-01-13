package com.example.kent.hyperdeals.NavigationOptionsActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseBooleanArray
import com.example.kent.hyperdeals.Admin.AdminAddIncentivesAdapter
import com.example.kent.hyperdeals.Admin.AdminRewardsModelToRetrieve
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rewards_user.*
import org.jetbrains.anko.toast

class UserRewards : AppCompatActivity() {


    private var rewardslist = ArrayList<AdminRewardsModelToRetrieve>()
    private var mAdapter : AdminAddIncentivesAdapter? = null
    private var mSelected: SparseBooleanArray = SparseBooleanArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards_user)


        rewardslist = ArrayList()

        val database = FirebaseFirestore.getInstance()

        val layoutManager = LinearLayoutManager(applicationContext)
        rewardsrecyclerview.layoutManager = layoutManager
        rewardsrecyclerview.itemAnimator = DefaultItemAnimator()

        database.collection("Rewards").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (DocumentSnapshot in task.result) {
                    val upload = DocumentSnapshot.toObject(AdminRewardsModelToRetrieve::class.java)
                    rewardslist.add(upload)
                    toast("success")

                    mAdapter = AdminAddIncentivesAdapter(mSelected, rewardslist)
                    rewardsrecyclerview.adapter = mAdapter

                }

            } else
                toast("error")
        }
    }
}
