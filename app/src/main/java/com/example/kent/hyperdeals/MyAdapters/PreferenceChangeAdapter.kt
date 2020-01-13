package com.example.kent.hyperdeals.MyAdapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.NavigationOptionsActivity.UserPreference
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_model_preferences.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.uiThread


class PreferenceChangeAdapter (var context: Context, var categoryList : ArrayList<CategoryParse>) : RecyclerView.Adapter<PreferenceChangeAdapter.ViewHolder>(){
    companion object{
        var categoryList1 = ArrayList<CategoryParse>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_model_preferences,parent,false))

    override fun getItemCount(): Int = categoryList.size


    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        var myCategory = categoryList[position]

        Picasso.get()
                .load(myCategory.CategoryImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(holder.profile_pic)
        holder.profile_pic.setOnClickListener {
            categoryList1=categoryList

            showDialog(myCategory,position)
        }
        holder.title.text = myCategory.categoryName
        doAsync {


            var myCount = 0

            for (i in 0 until myCategory.Subcategories.size) {
                if (myCategory.Subcategories[i].Selected) {
                    myCount += 1
                }

            }
           uiThread {
               holder.no_selected.text = "$myCount selected"
           }
        }


        holder.ivClose.setOnClickListener{

            showAreyouSureDialog(myCategory.categoryName,position)

        }


    }



    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var title = view.tv_categoryName
        var no_selected = view.tv_no_selected
        var profile_pic = view.profile_image
        var ivClose = view.iv_close
    }


    @SuppressLint("InflateParams")
    fun showDialog(categoryParse:CategoryParse, myPosition:Int) {

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_set_subcategories_user, null)


        dialogBuilder.setCancelable(false)
        dialogBuilder.setView(dialogView)



        val categoryImage = dialogView.findViewById(R.id.category_image) as de.hdodenhof.circleimageview.CircleImageView
        val categoryName = dialogView.findViewById(R.id.categoryName) as TextView
        val recyclerSubcat = dialogView.findViewById(R.id.reclerView_edit_subcategories) as RecyclerView
        val ivClose = dialogView.findViewById(R.id.iv_close) as ImageView
        val myBtnSet = dialogView.findViewById(R.id.btn_set) as Button

                Picasso.get()
                .load(categoryParse.CategoryImage)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(categoryImage)
        categoryName.text = categoryParse.categoryName

       var  mySubcatAdapter = SubcategoryAdaperUser(context,categoryParse.Subcategories,myPosition)
        var myStagger = StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL)
        recyclerSubcat.layoutManager = myStagger
        recyclerSubcat.adapter =  mySubcatAdapter

        val b = dialogBuilder.create()
        b.show()

        ivClose.setOnClickListener {
            b.dismiss()

        }

        myBtnSet.setOnClickListener {

            categoryList=categoryList1
            this.notifyDataSetChanged()

            var tempSubcategoryModel = ArrayList<SubcategoryModelx>()
            for(i in 0 until  UserPreference.categoryList[myPosition].Subcategories.size){
              var tempSubmodel =   SubcategoryModelx(UserPreference.categoryList[myPosition].Subcategories[i].SubcategoryName,UserPreference.categoryList[myPosition].Subcategories[i].Selected)
                tempSubcategoryModel.add(tempSubmodel)
            }
            Log.e(TAG,"tempSubcategoryModel ${tempSubcategoryModel.size}")
           var tempCatModel =  CategoryModel(UserPreference.categoryList[myPosition].categoryName, UserPreference.categoryList[myPosition].SelectedAll, tempSubcategoryModel)
            tempCatModel.CategoryImage = UserPreference.categoryList[myPosition].CategoryImage

var database = FirebaseFirestore.getInstance()
            database.collection("UserCategories").document(LoginActivity.userUIDS).collection("Categories")
                    .document(UserPreference.categoryList[myPosition].categoryName).set(tempCatModel).addOnCompleteListener {Log.e(TAG,"success edit")
                    }
            b.dismiss()
        }


    }
    fun showAreyouSureDialog(categoryName:String,myPosition: Int) {


        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setCancelable(true)
        dialogBuilder.setTitle("Are you sure?")
        dialogBuilder.setMessage("Do you wish to remove Category?")


        dialogBuilder.setPositiveButton("Remove", DialogInterface.OnClickListener { dialogInterface, i ->
            var database = FirebaseFirestore.getInstance()
            database.collection("UserCategories").document(LoginActivity.userUIDS).collection("Categories").document(categoryName).delete().addOnCompleteListener {
                Log.e(TAG,"$categoryName Deleted from database")
                categoryList.removeAt(myPosition)

                notifyDataSetChanged()

            }




        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

        })
        val b = dialogBuilder.create()
        b.show()




    }



}