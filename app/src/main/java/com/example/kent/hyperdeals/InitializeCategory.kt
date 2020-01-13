package com.example.kent.hyperdeals

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.MyAdapters.CategoryAdapter
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.NavigationBar.DashboardActivity

import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_initialize_category.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class InitializeCategory : AppCompatActivity() {
    lateinit var  ref: DatabaseReference
    val database = FirebaseFirestore.getInstance()
    private var     mFirebaseFirestore = FirebaseFirestore.getInstance()
    val TAG = "InitializeCategory"
    var msubcategories = ArrayList<SubcategoryModelx>()
    lateinit var myCategoryModel:CategoryModel
    lateinit var myAdapter:CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize_category)

        getCategories()

        button2.setOnClickListener {

            LoginActivity.userUIDS = RegistrationActivity.globalRegisteredUsername
            parseTOunparse2( myAdapter.categoryList)
            setPreferedTIme()
            startActivity(Intent(this,DashboardActivity::class.java))

        }
        button_set.setOnClickListener{
            LoginActivity.userUIDS = RegistrationActivity.globalRegisteredUsername

            parseTOunparse( myAdapter.categoryList)
            val intent = Intent(this,DashboardActivity::class.java)
            setPreferedTIme()
            startActivity(intent)
        }



    }

    fun setPreferedTIme(){


        var userPrefTIme  =   userPreferredTime(0,0,23,59,false)
        database.collection("UserPreferredTime").document(RegistrationActivity.globalRegisteredUsername).set(userPrefTIme)
    }

    fun getCategories() {

        var categoryList=ArrayList<CategoryParse>()
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

                                    var mySubcategoryParse = SubcategoryParse()
                                    mySubcategoryParse.SubcategoryName = upload.SubcategoryName
                                    mySubcategoryParse.Selected = false
                                    uploaded.Subcategories.add(mySubcategoryParse)


                                }


                            } else
                                toast("error")
                        }
                        var myCategoryParse = CategoryParse()
                        myCategoryParse.Subcategories = uploaded.Subcategories
                        myCategoryParse.SelectedAll=false
                        myCategoryParse.CategoryImage = uploaded.CategoryImage
                        myCategoryParse.categoryName = uploaded.categoryName
                        myCategoryParse.isShowing = uploaded.isShowing
                        categoryList.add(myCategoryParse)

                    }

                    myAdapter = CategoryAdapter(this@InitializeCategory,categoryList)
                    recylerviewCategory.layoutManager = GridLayoutManager(this@InitializeCategory,2)
                    recylerviewCategory.adapter = myAdapter



                } else
                    toast("error")
            }


        }

    }



    fun parseTOunparse( categoryParseList : ArrayList<CategoryParse>):ArrayList<CategoryModel>{
        var  categoryModelList = ArrayList<CategoryModel>()

        for(i in 0 until categoryParseList.size) {
            if (categoryParseList[i].SelectedAll){
                var tempCategoryName = categoryParseList[i].categoryName
                var tempSelectedAll = categoryParseList[i].SelectedAll
                var tempSubcategories = ArrayList<SubcategoryModelx>()
                var tempCategoryImage = categoryParseList[i].CategoryImage
                for (j in 0 until categoryParseList[i].Subcategories.size) {
                    if (tempSelectedAll) {
                        tempSubcategories.add(SubcategoryModelx(categoryParseList[i].Subcategories[j].SubcategoryName, true))
                    }

                }
                var final = CategoryModel(tempCategoryName, tempSelectedAll, tempSubcategories)
                final.CategoryImage = tempCategoryImage
                categoryModelList.add(final)
            }

        }

        var userCategoryFinal = categoryModelList

        for(i in 0 until userCategoryFinal.size){
            storeDatatoFirestore(userCategoryFinal[i])
            Log.e(TAG,"CATEGORY NAME - ${userCategoryFinal[i].categoryName}  SELECTED - ${userCategoryFinal[i].SelectedAll}")
            for(j in 0 until userCategoryFinal[i].Subcategories.size){
                Log.e(TAG,"subcategories - ${userCategoryFinal[i].Subcategories[j]}")


            }

        }



        return categoryModelList
    }
    fun parseTOunparse2( categoryParseList : ArrayList<CategoryParse>):ArrayList<CategoryModel>{
        var  categoryModelList = ArrayList<CategoryModel>()

        for(i in 0 until categoryParseList.size){
            var tempCategoryName = categoryParseList[i].categoryName
            var tempSelectedAll = true
            var tempSubcategories =ArrayList<SubcategoryModelx>()
            var tempCategoryImage = categoryParseList[i].CategoryImage
            for(j in 0 until categoryParseList[i].Subcategories.size){
                tempSubcategories.add(SubcategoryModelx(categoryParseList[i].Subcategories[j].SubcategoryName,true))


            }

            var final = CategoryModel(tempCategoryName,tempSelectedAll,tempSubcategories)
            final.CategoryImage = tempCategoryImage
            categoryModelList.add(final)


        }

        var userCategoryFinal = categoryModelList

        for(i in 0 until userCategoryFinal.size){
            storeDatatoFirestore(userCategoryFinal[i])
            Log.e(TAG,"CATEGORY NAME - ${userCategoryFinal[i].categoryName}  SELECTED - ${userCategoryFinal[i].SelectedAll}")
            for(j in 0 until userCategoryFinal[i].Subcategories.size){
                Log.e(TAG,"subcategories - ${userCategoryFinal[i].Subcategories[j]}")


            }

        }



        return categoryModelList
    }




    private fun storeDatatoFirestore(categoryModel:CategoryModel){
        doAsync {
            Log.e(TAG,categoryModel.categoryName+ " save to database")
            mFirebaseFirestore.collection("UserCategories").document(RegistrationActivity.globalRegisteredUsername).collection("Categories").document(categoryModel.categoryName).set(categoryModel)

        }

    }



}
