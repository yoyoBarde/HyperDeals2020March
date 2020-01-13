package com.example.kent.hyperdeals

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import com.example.kent.hyperdeals.Admin.Admin
import com.example.kent.hyperdeals.Admin.AdminAddCategoryAdapter
import com.example.kent.hyperdeals.Model.CategoryParse
import com.example.kent.hyperdeals.Model.SubcategoryModelx
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_category.*


import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

class EditCategory : AppCompatActivity() {
    private var mSelected: SparseBooleanArray = SparseBooleanArray()
    var subcategories = ArrayList<SubcategoryModelx>()
    val TAG = "EditCategory"
    val PICK_IMAGE_REQUEST = 11
    private var mStorage: FirebaseStorage? = null
    private var mStorageReference: StorageReference? = null
    private var db = FirebaseFirestore.getInstance()
    var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

      var myCategory =  intent.getParcelableExtra<CategoryParse>("category")
        tvCategoryName.setText(myCategory.categoryName)
        for(i in 0 until myCategory.Subcategories.size){
            subcategories.add(SubcategoryModelx(myCategory.Subcategories[i].SubcategoryName,myCategory.Subcategories[i].Selected))

        }
        Picasso.get()
                .load(myCategory.CategoryImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(adminpickimage)
        var mAdapter = AdminAddCategoryAdapter(mSelected,subcategories)
        add_category_recyclerview.adapter = mAdapter
        adminpickimage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }

        btn_publishcategories.setOnClickListener {
            uploadFile()

        }
        add_subcategory.setOnClickListener {


            val input = subcategoryinput.text.toString()


            if(input.isNotEmpty()) {
                subcategories.add(SubcategoryModelx(input, false))
                mAdapter = AdminAddCategoryAdapter(mSelected,subcategories)
                add_category_recyclerview.adapter = mAdapter
                subcategoryinput.setText("")
            }
            else{
                toast("Emptyu Subcategory")
            }
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

            for( i in 0 until subcategories.size) {

                val Subcategory = HashMap<String, Any?>()
                Subcategory["Selected"] = false
                Subcategory["SubcategoryName"] = subcategories[i].SubcategoryName



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
    override fun onActivityResult(requestCode: Int, resultCode: Int, dataa: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataa)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && dataa != null) {
            imageUri = dataa.data

            Picasso.get().load(imageUri).into(adminpickimage)


        }
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

}
