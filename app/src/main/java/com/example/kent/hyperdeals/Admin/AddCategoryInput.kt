package com.example.kent.hyperdeals.Admin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.SparseBooleanArray
import com.example.kent.hyperdeals.Model.CategoryModel
import com.example.kent.hyperdeals.Model.SubcategoryModelx
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_category_input.*
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class AddCategoryInput : AppCompatActivity() {

    val TAG = "AddCategoryInput"
    var subcategoryarraylist = ArrayList<SubcategoryModelx>()

    private var mAdapter: AdminAddCategoryAdapter? = null
    private var mSelected: SparseBooleanArray = SparseBooleanArray()

    val PICK_IMAGE_REQUEST = 11
    private var mStorage: FirebaseStorage? = null
    private var mStorageReference: StorageReference? = null
    private var db = FirebaseFirestore.getInstance()
    var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category_input)




        val layoutManager = LinearLayoutManager(applicationContext)
        add_category_recyclerview.layoutManager = layoutManager
        add_category_recyclerview.itemAnimator = DefaultItemAnimator()

        add_subcategory.setOnClickListener {


            val input = subcategoryinput.text.toString()


            if(input.isNotEmpty()) {
                subcategoryarraylist.add(SubcategoryModelx(input, false))
                mAdapter = AdminAddCategoryAdapter(mSelected, subcategoryarraylist)
                add_category_recyclerview.adapter = mAdapter
                subcategoryinput.setText("")
            }
            else{
                toast("Emptyu Subcategory")
            }
        }


        adminpickimage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }

        adminsetimage.setOnClickListener {
        }

        btn_publishcategories.setOnClickListener {
            uploadFile()

        }


    }






        fun storeCategoriesToFirestore(imageLink:String) {


            val categoryname = adminentercategoryname.text.toString()
            val Category = HashMap<String, Any?>()
            Category["CategoryImage"] = imageLink
            Category["categoryName"] = categoryname
            Category["SelectedAll"] = false


        var randomUIID = UUID.randomUUID().toString()


                    db.collection("Categories").document(randomUIID).set(Category).addOnSuccessListener {

                        Log.e(TAG,"success adding Category")

                    for( i in 0 until subcategoryarraylist.size) {

                        val Subcategory = HashMap<String, Any?>()
                        Subcategory["Selected"] = false
                        Subcategory["SubcategoryName"] = subcategoryarraylist[i].SubcategoryName



                   db.collection("Categories").document(randomUIID).collection("Subcategories").document().set(Subcategory).addOnSuccessListener {

                       Log.e(TAG,"Subcategory added")
                   }



                    }

                    }
                            .addOnFailureListener {
                                Log.e(TAG,"Fail adding Category")

                            }


            val intent = Intent(this, Admin::class.java)
            startActivity(intent)


        }

        fun uploadFile() {

            mStorage = FirebaseStorage.getInstance()
            mStorageReference = mStorage!!.reference

            if (imageUri != null) {
                val ref = mStorageReference!!.child("CategoryPhotos/" + UUID.randomUUID().toString())
                ref.putFile(imageUri!!)
                        .addOnSuccessListener {

                            val image = it.downloadUrl.toString()

                            storeCategoriesToFirestore(image)
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

                Picasso.get().load(imageUri).into(adminpickimage)


            }
        }

    }

















