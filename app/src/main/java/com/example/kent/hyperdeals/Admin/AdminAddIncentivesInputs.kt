package com.example.kent.hyperdeals.Admin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseBooleanArray
import android.view.View
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.admin_add_incentives_inputs.*
import kotlinx.android.synthetic.main.zaddpromobusinessman.*
import org.jetbrains.anko.downloadManager
import org.jetbrains.anko.toast
import java.util.*

class AdminAddIncentivesInputs : AppCompatActivity() {

    val PICK_IMAGE_REQUEST = 11
    private var mStorage: FirebaseStorage? = null
    private var mStorageReference: StorageReference? = null
    private var mFirebaseFirestore = FirebaseFirestore.getInstance()
    var imageUri: Uri? = null
    private var mImageLink: UploadTask.TaskSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_add_incentives_inputs)

/*-------------------------------------------------------------------------------------------------------------------------------*/
        /*Adding rewards to Firestore*/


        adminincentiveimage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }

        adminincentivetextviewsetimage.setOnClickListener {
            uploadFile()
        }


        adminincentivepublishreward.setOnClickListener {
            storeRewardToFirestore()
        }


    }


    fun storeRewardToFirestore() {

        val rewardimagelink = adminincentiveimagelink.text.toString()
        val rewardpoints = adminincentiverewardpoints.text.toString() + " points"
        val rewardname = adminincentiverewardname.text.toString()


        val Rewards = AdminRewardsModel(downloadImageLink().toString(),rewardimagelink, rewardname, rewardpoints)
        mFirebaseFirestore.collection("Rewards").document().set(Rewards)

        toast("Sucess")

        val intent = Intent(this, Admin::class.java)
        startActivity(intent)

    }

    //
    private fun downloadImageLink(){
        mStorageReference!!.child("images/").downloadUrl.addOnSuccessListener {


            mImageLink!!.downloadUrl

            adminincentiveimagelink.setText(mImageLink.toString())

            toast("Nice")

        }
    }

    fun uploadFile() {

        mStorage = FirebaseStorage.getInstance()
        mStorageReference = mStorage!!.reference

        if (imageUri != null) {
            val ref = mStorageReference!!.child("images/" + UUID.randomUUID().toString())
            ref.putFile(imageUri!!)
                    .addOnSuccessListener {


                        val image = it.downloadUrl.toString()

                        adminincentiveimagelink.setText(image)

                        toast("Image Uploaded Successfully")


                    }
                    .addOnFailureListener {
                        toast("Uploading Failed")
                    }
        }

    }


        override fun onActivityResult(requestCode: Int, resultCode: Int, dataa: Intent?) {
            super.onActivityResult(requestCode, resultCode, dataa)

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && dataa != null) {
                imageUri = dataa.data

                Picasso.get().load(imageUri).into(adminincentiveimage)


            }
        }

    }

