package com.example.kent.hyperdeals.NavigationOptionsActivity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.kent.hyperdeals.FragmentActivities.DialogFragmentAddCategoryUser
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.PreferenceChangeAdapter
import com.example.kent.hyperdeals.R
import com.example.kent.hyperdeals.Model.myInterfacesCategories
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_user_preference.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class UserPreference : AppCompatActivity(), myInterfacesCategories,myInterfacesUpdateSubcategoryCount {
    override fun saveSubcategoryUpdate() {
        Log.e(TAG,"updated Subcategory")
        setAdapter()


    }



    val database = FirebaseFirestore.getInstance()
    lateinit var myAdapter:PreferenceChangeAdapter
    companion object {
        lateinit var recylerview:RecyclerView
        var categoryList = ArrayList<CategoryParse>()
        var categoryListAll = ArrayList<CategoryParse>()

    }

    lateinit var myContext:Context
    val TAG = "UserPreference"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_preference)
        imageView7.setOnClickListener { finish() }

        myContext = applicationContext
        getCategories()

        recylerview = findViewById<RecyclerView>(R.id.recycler_edit_user_preference)
        getCategoriesAll()

        btn_add_categories.setOnClickListener {



            var myDialog = DialogFragmentAddCategoryUser()

            myDialog.show(fragmentManager,"myCustomDialog")
        }


    }

    fun getCategoriesAll() {
        var database = FirebaseFirestore.getInstance()
        categoryListAll = ArrayList<CategoryParse>()
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








                } else
                    toast("error")
            }


        }

    }


    override fun addCategoriesUser(myCategoryList: ArrayList<CategoryParse>) {
        for(i in 0 until myCategoryList.size){
            if(myCategoryList[i].SelectedAll)
            {
                var tempCategoryName = myCategoryList[i].categoryName
                var tempSelectedAll = myCategoryList[i].SelectedAll
                var tempSubcategories = ArrayList<SubcategoryModelx>()
                var tempCategoryImage = myCategoryList[i].CategoryImage
                for(j in 0 until myCategoryList[i].Subcategories.size){


                    tempSubcategories.add(SubcategoryModelx(myCategoryList[i].Subcategories[j].SubcategoryName, true))
                    Log.e(TAG, myCategoryList[i].Subcategories[j].Selected.toString()+"Bantog ra")
                    myCategoryList[i].Subcategories[j].Selected=true
                }
                var final = CategoryModel(tempCategoryName, tempSelectedAll, tempSubcategories)
                final.CategoryImage = myCategoryList[i].CategoryImage
                doAsync {
                    database.collection("UserCategories").document(LoginActivity.userUIDS).collection("Categories").document(final.categoryName).set(final).addOnCompleteListener {
                        Log.e(TAG, "pushed to db")
                    }
                }
                categoryList.add(myCategoryList[i])
            }

        }

        setAdapter()


    }

    fun setAdapter(){
        var changed = PreferenceChangeAdapter(this,categoryList)
        recylerview.layoutManager = GridLayoutManager(this,2)
        recylerview.adapter = changed


    }
    fun getCategories() {
        Log.e(TAG,"retreiving ${LoginActivity.userUIDS} Categories")
        categoryList = ArrayList<CategoryParse>()
        database.collection("UserCategories").document(LoginActivity.userUIDS).collection("Categories").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    var userCategory = document.toObject(CategoryParse::class.java)
                    Log.e(TAG,categoryList.size.toString()+" totalCount retrieve")

                    categoryList.add(userCategory)
                }
                Log.e(TAG,categoryList.size.toString()+" totalCount retrieve")
                Log.e(TAG,task.result.toString()+"   atay     " + task.isSuccessful.toString())
                getUserCategoryUpdate()



            }


        }.addOnFailureListener {

            Log.e(TAG,"Fail so bad")
        }
    }

    fun getUserCategoryUpdate(){

        database.collection("UserCategories").document(LoginActivity.userUIDS).collection("Categories").addSnapshotListener(EventListener<QuerySnapshot> { snapshots, e ->
            if (e != null) {
                Log.w(TAG, "listen:error", e)
                return@EventListener
            }

            for (dc in snapshots!!.documentChanges) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    var uploaded = dc.document.toObject(CategoryParse::class.java)
                    var changed = PreferenceChangeAdapter(this, categoryList)
                    UserPreference.recylerview.layoutManager = GridLayoutManager(this,2)
                    UserPreference.recylerview.adapter = changed
                    Log.e(TAG, "New Item: ${dc.document.data} ${uploaded.categoryName}")

                }
            }
        })

    }
}



