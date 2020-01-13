package com.example.kent.hyperdeals.Admin

import android.content.Intent
import android.support.v4.app.Fragment


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kent.hyperdeals.Model.CategoryParse
import com.example.kent.hyperdeals.Model.SubcategoryParse
import com.example.kent.hyperdeals.MyAdapters.PreferenceChangeAdapterAdmin
import com.example.kent.hyperdeals.R

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.admin_add_category.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast

class AdminAddCategory: Fragment(){
val TAG = "AdminAddCategory"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.admin_add_category,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCategoriesAll()
        adminaddcategorybutton.setOnClickListener{
            val intent = Intent (this.context,AddCategoryInput::class.java)
            startActivity(intent)
        }

    }


    fun setAdapter(categoryList:ArrayList<CategoryParse>){
        var changed = PreferenceChangeAdapterAdmin(activity!!,categoryList)
        recyclerView.layoutManager = GridLayoutManager(activity!!,2)
        recyclerView.adapter = changed


    }

    fun getCategoriesAll() {
        var database = FirebaseFirestore.getInstance()
   var categoryListAll = ArrayList<CategoryParse>()
        doAsync {
            database.collection("Categories").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (DocumentSnapshot in task.result) {

                        var uploaded = DocumentSnapshot.toObject(CategoryParse::class.java)
                        Log.e(TAG, DocumentSnapshot.getId() + " => " + DocumentSnapshot.getData())


                        database.collection("Categories").document(DocumentSnapshot.id).collection("Subcategories").get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (DocumentSnapshot in task.result) {
                                    var upload = DocumentSnapshot.toObject(SubcategoryParse::class.java)
                                    Log.e(TAG, " Subcategory - " + upload.toString())
                                    uploaded.Subcategories.add(upload)


                                }


                            } else
                                toast("error")
                        }

                        categoryListAll.add(uploaded)

                    }






                    setAdapter(categoryListAll)

                } else
                    toast("error")
            }


        }

    }

}