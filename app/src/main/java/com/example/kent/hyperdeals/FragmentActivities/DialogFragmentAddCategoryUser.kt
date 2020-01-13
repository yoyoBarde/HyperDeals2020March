package com.example.kent.hyperdeals.FragmentActivities

import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*
import com.example.kent.hyperdeals.Model.CategoryParse
import com.example.kent.hyperdeals.Model.SubcategoryParse
import com.example.kent.hyperdeals.MyAdapters.PreferenceAddAdapter
import com.example.kent.hyperdeals.NavigationOptionsActivity.UserPreference
import com.example.kent.hyperdeals.R
import com.example.kent.hyperdeals.Model.myInterfacesCategories

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dialog_fragment_category_user.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class DialogFragmentAddCategoryUser : DialogFragment() {
    var categoryList=ArrayList<CategoryParse>()
    lateinit var myAdapter:PreferenceAddAdapter
    val TAG = "DialogFragmentUser"
    fun newInstance(): DialogFragmentAddCategoryBusiness {
        return DialogFragmentAddCategoryBusiness()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {




        return inflater.inflate(R.layout.activity_dialog_fragment_category_user, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        iv_close.setOnClickListener {

            dismiss()
        }
        Log.e(TAG," onViewCreated ${UserPreference.categoryListAll.size}  OKAY  ${UserPreference.categoryList.size}")

        btn_add_preference.setOnClickListener {
            var myInterface =  UserPreference() as (myInterfacesCategories)

            myInterface.addCategoriesUser(myAdapter.categoryList)
            dismiss()

        }
        filterList(UserPreference.categoryListAll)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Sample)
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
              dialog.window!!.setGravity(Gravity.BOTTOM)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.white )
        }
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog.window!!
                .attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawableResource(R.color.black_alpha_80)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //  dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        return dialog
    }


    fun getCategories() {
        var database = FirebaseFirestore.getInstance()

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

                        categoryList.add(uploaded)

                    }
                    filterList(categoryList)








                } else
                    toast("error")
            }


        }

    }


    fun filterList(choiceList:ArrayList<CategoryParse>){

       var filteredList = ArrayList<CategoryParse>()
        for(i in 0 until choiceList.size) {
            var noMatches = true


            for(j in 0 until UserPreference.categoryList.size) {
                if (choiceList[i].categoryName == UserPreference.categoryList[j].categoryName) {
                    noMatches = false
                }
            }
            if(noMatches)
            filteredList.add(choiceList[i])


        }
        Log.e(TAG,filteredList.size.toString()+" filteredList size")
        myAdapter = PreferenceAddAdapter(activity,filteredList)
        recycler_add_preference_user.layoutManager = GridLayoutManager(activity,2)
        recycler_add_preference_user.adapter = myAdapter

    }






}



