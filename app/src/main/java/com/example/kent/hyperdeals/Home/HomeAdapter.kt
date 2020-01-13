package com.example.kent.hyperdeals.Home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.kent.hyperdeals.FragmentsBusiness.Business_PromoProfile
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.*
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.zzhomelistitem.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.uiThread
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter( private val context:Context, private val selectedItem: SparseBooleanArray, private val promolist : List<PromoModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>(){
val database = FirebaseFirestore.getInstance()
    val TAG = "HomeAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            HomeAdapter.ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.zzhomelistitem,parent,false))

    override fun getItemCount(): Int  = promolist.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val promos = promolist[position]
    Picasso.get()
            .load(promos.promoImageLink)
            .placeholder(R.drawable.hyperdealslogofinal)
            .into(holder.ivHomeImage)

    holder.tvHomeText.text = promos.promoname
    holder.tvHomeStore.text = promos.promoStore
        var distance = "0.0"
   try {
      distance = "${promos.distance.get(0)}${promos.distance.get(1)}${promos.distance.get(2)}${promos.distance.get(3)}${promos.distance.get(4)}"
   } catch(e:StringIndexOutOfBoundsException){
       print(e)
       distance = promos.distance

   }
        holder.tv_distance.text = distance + " KM"
    holder.container.setOnClickListener { showDialog(position) }


    }


    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val ivHomeImage = view.homePromoImage!!
        val tvHomeText = view.homePromoName!!
        val tvHomeStore = view.homePromoStore!!
        val tv_distance = view.tv_distance!!
        val container = view.constraint_holder!!
    }


    fun showDialog(position:Int) {



        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogbox, null)

        dialogBuilder.setCancelable(false)

        dialogBuilder.setView(dialogView)




        val interested = dialogView.findViewById(R.id.interested) as ImageView
        val call = dialogView.findViewById(R.id.call) as ImageView
        val showNavigation = dialogView.findViewById(R.id.map) as ImageView
        val promoPicture = dialogView.findViewById(R.id.promoPicture) as ImageView
        val promoStore = dialogView.findViewById(R.id.promoSTORE) as TextView
        val promoDescription = dialogView.findViewById(R.id.promoDESCRIPTION) as TextView
        val promoName = dialogView.findViewById(R.id.promoNAME) as TextView
        val promoNumber = dialogView.findViewById(R.id.promoNUMBER) as TextView
        val promoLocation = dialogView.findViewById(R.id.promoLOCATION) as TextView
        val ccntainer = dialogView.findViewById(R.id.specificPromoContainer) as ConstraintLayout



        var promoLikes = 0
        database.collection("PromoIntrested").document(promolist[position].promoStore).
                get().addOnSuccessListener { document ->

            if(document.exists())
            {

                var promoLikeCountParce = document.toObject(promoLikesCountParce::class.java)
                promoLikes = promoLikeCountParce.LikeCount
                Log.e(TAG,"promolikes ${promoLikes}")
            }
            else{
                Log.e(TAG,"dont exist")
            }

        }





        var likeRetrieved = false
        database.collection("PromoIntrested").document(promolist[position].promoStore).
                collection("interested_users").document(LoginActivity.userUIDS).get().addOnSuccessListener { document -> Log.e(TAG,"Naa")

            if(document.exists())
            {

                var userLikeParce = document.toObject(userLikeParce::class.java)
                likeRetrieved =true

                interested.setImageResource(R.drawable.ic_liked_k)
            }
            else{
                Log.e(TAG,"dont exist")
                likeRetrieved =false

            }

        }.addOnFailureListener {

            Log.e(TAG,"WALA")
        }




        Picasso.get()
                .load(promolist[position].promoImageLink)
                .placeholder(R.drawable.hyperdealslogofinal)
                .into(promoPicture)
        promoStore.text = promolist[position].promoStore
        promoDescription.text = promolist[position].promodescription
        promoName.text = promolist[position].promoname
        promoNumber.text = promolist[position].promoContactNumber
        promoLocation.text = promolist[position].promoPlace


        val b = dialogBuilder.create()
        b.show()
        b.setCancelable(true)


        ccntainer.setOnClickListener {
            incrementPromos(promolist[position])





            b.dismiss()
            val intent = Intent(context, Business_PromoProfile::class.java)
            PromoListAdapter.promoProfile = promolist[position]
            context.startActivity(intent)



        }
        interested.setOnClickListener {
            Log.e(TAG,"atay")
            doAsync {


                if (!likeRetrieved) {
                    uiThread {     interested.setImageResource(R.drawable.ic_liked_k)}
                    var myUserLike = userLike(LoginActivity.userUIDS, true)
                    database.collection("PromoIntrested").document(promolist[position].promoStore).collection("interested_users").document(LoginActivity.userUIDS).set(myUserLike).addOnCompleteListener {
                        Log.e(TAG, "liked")
                        likeRetrieved = true
                        database.collection("UserLikes").document(LoginActivity.userUIDS).collection("Promo").document(promolist[position].promoStore).set(userPromoiked(promolist[position].promoStore))
                        database.collection("PromoIntrested").document(promolist[position].promoStore).set(promoLikesCount(promoLikes+1))
                    }

                } else {
                    uiThread {   interested.setImageResource(R.drawable.interested)}
                    database.collection("PromoIntrested").document(promolist[position].promoStore).collection("interested_users").document(LoginActivity.userUIDS).delete().addOnCompleteListener {
                        Log.e(TAG, "deleted")
                        likeRetrieved = false
                        database.collection("UserLikes").document(LoginActivity.userUIDS).collection("Promo").document(promolist[position].promoStore).delete()
                        database.collection("PromoIntrested").document(promolist[position].promoStore).set(promoLikesCount(promoLikes-1))

                    }


                }

            }



        }


        call.setOnClickListener {

            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${promolist[position].promoContactNumber}")
            context.startActivity(intent)

        }
        showNavigation.setOnClickListener {

            val gmmIntentUri = Uri.parse("google.navigation:q="+ promolist[position].promoLatLng)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            context.startActivity(mapIntent)
        }
    }
    fun incrementPromos(myModel: PromoModel){

        var  dateFormat = SimpleDateFormat("dd/yyyy")
        Log.e(TAG," current date $ ${dateFormat.format(Calendar.getInstance().time)}")
        database.collection("PromoViews").document(myModel.promoStore).collection("UserViews").document(LoginActivity.userUIDS).get().addOnSuccessListener {
            document ->
            if(document.exists()){
                var userViewParce = document.toObject(userPromoViewsParce::class.java)

                if(userViewParce.date.equals(dateFormat.format(Calendar.getInstance().time))){
                    Log.e(TAG,"dateMatched no Action")
                }
                else {
                    doAsync {

                        database.collection("PromoViews").document(myModel.promoStore).collection("UserViews").document(LoginActivity.userUIDS)
                                .set(userPromoViews(dateFormat.format(Calendar.getInstance().time)))
                        var mPromoViews = promoViewsParde()

                        database.collection("PromoViews").document(myModel.promoStore)
                                .get().addOnSuccessListener { document ->

                                    if (document.exists()) {
                                        mPromoViews = document.toObject(promoViewsParde::class.java)
                                        Log.e(TAG, "promo views get ${mPromoViews.promoViews}")
                                        var fPromoViews = promoViews(mPromoViews.promoViews + 1)
                                        database.collection("PromoViews").document(myModel.promoStore).set(fPromoViews)
                                    }
                                    else{
                                        var fPromoViews = promoViews(mPromoViews.promoViews + 1)
                                        database.collection("PromoViews").document(myModel.promoStore).set(fPromoViews)
                                    }
                                }


                    }
                }

            }
            else{
                doAsync {
                    database.collection("PromoViews").document(myModel.promoStore).collection("UserViews").document(LoginActivity.userUIDS)
                            .set(userPromoViews(dateFormat.format(Calendar.getInstance().time)))
                    var mPromoViews = promoViewsParde()
                    database.collection("PromoViews").document(myModel.promoStore)
                            .get().addOnSuccessListener { document ->

                                if (document.exists()) {
                                    mPromoViews = document.toObject(promoViewsParde::class.java)
                                    Log.e(TAG, "promo views get ${mPromoViews.promoViews}")
                                    var fPromoViews = promoViews(mPromoViews.promoViews + 1)
                                    database.collection("PromoViews").document(myModel.promoStore).set(fPromoViews)
                                }
                                else{
                                    var fPromoViews = promoViews(mPromoViews.promoViews + 1)
                                    database.collection("PromoViews").document(myModel.promoStore).set(fPromoViews)
                                }
                            }


                }
            }

        }


    }

}