package com.example.kent.hyperdeals.FragmentActivities

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import com.example.kent.hyperdeals.FragmentsBusiness.FragmentPromoSaleBusiness
import com.example.kent.hyperdeals.Model.myInterfacesAddItem
import com.example.kent.hyperdeals.Model.promoItem
import com.example.kent.hyperdeals.Model.promoItemParcelable
import com.example.kent.hyperdeals.MyAdapters.PromoListAdapter
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dialog_fragment_add_promo_item.*
import kotlinx.android.synthetic.main.zaddpromobusinessman.*
import org.jetbrains.anko.toast
import java.util.*
import android.R.attr.data
import android.support.v4.app.NotificationCompat.getExtras
import android.graphics.Bitmap
import android.content.Context
import android.os.Environment
import org.jetbrains.anko.doAsync
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.jar.Manifest


class DialogFragmentAddPromoItem : DialogFragment() {
    private val REQUEST_PICK_IMAGE = 111111
    private val REQUEST_TAKE_PICTURE = 222222
    var imageUri: Uri? = null

    val TAG = "DialogItem"
    var imageLink = ""
    fun newInstance(): DialogFragmentAddPromoItem {
        return DialogFragmentAddPromoItem()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.activity_dialog_fragment_add_promo_item, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        iv_close.setOnClickListener { dismiss() }

        iv_get_picture.setOnClickListener {
            var myIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(myIntent, REQUEST_PICK_IMAGE)


        }
        iv_take_picture.setOnClickListener {
            var myIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            var pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            var pictureName = getPictureName()
            var imageFIle = File(pictureDirectory,pictureName)
            imageUri = Uri.fromFile(imageFIle)
            Log.e(TAG,imageUri.toString())
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
            startActivityForResult(myIntent, REQUEST_TAKE_PICTURE)

        }
        btn_add_item.setOnClickListener {
            var inputName = input_item_name.text.toString()
            var inputOldPrice = input_item_old_price.text.toString().toInt()
            var inputnewPrice = input_item_new_price.text.toString().toInt()
            dismiss()
            uploadFile(inputName, inputOldPrice, inputnewPrice)


        }


    }
    fun getRandomInt():String {

       val min = Math.ceil(10.230)
       val max = Math.floor(100.230)
        var result = Math.floor(Math.random() * (max - min)) + min
        return result.toString()
    }
    fun getPictureName ():String{
        var sdf = SimpleDateFormat("yyyyMMdd HHmmss")
        sdf.format(Date())


        return "${getRandomInt()}TakePicture$sdf.jpg"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, com.example.kent.hyperdeals.R.style.Sample)
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setGravity(Gravity.BOTTOM)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        }
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog.window!!
                .attributes.windowAnimations = com.example.kent.hyperdeals.R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawableResource(com.example.kent.hyperdeals.R.color.black_alpha_80)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //  dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        return dialog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            Picasso.get().load(imageUri).into(iv_promo_item)


        }
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode == Activity.RESULT_OK && data != null) {
            Log.e(TAG,"take Picture")
            imageUri = data.data
            val bitmap = data.extras.get("data") as Bitmap
            Log.e(TAG,"$bitmap uri $imageUri")
            iv_promo_item.setImageBitmap(bitmap)
            Log.e(TAG,"$imageUri")


        }
    }

    private fun uploadFile(pName: String, oPrice: Int, nPrice: Int) {

        var mStorage = FirebaseStorage.getInstance()
        var mStorageReference = mStorage!!.reference

        if (imageUri != null) {
            val ref = mStorageReference!!.child("imagesItem/" + UUID.randomUUID().toString())
            ref.putFile(imageUri!!)
                    .addOnSuccessListener {


                        val image = it.downloadUrl.toString()
                Log.e(TAG,image)
                        imageLink = image
                        var myPromoItem = promoItem(pName, oPrice, nPrice, image)
                        AddItemToFB(myPromoItem)


                    }
                    .addOnFailureListener {
                        toast("Uploading Failed")
                    }

        }


    }


    fun AddItemToFB(myPromoItem: promoItem) {

        var database = FirebaseFirestore.getInstance()
doAsync {
        database.collection("StoreItems").document(PromoListAdapter.promoProfile.promoStore).collection("PromoItems").document().set(myPromoItem).addOnCompleteListener {
            Log.e(TAG, "Success AddItemToFB ")

            var myPromoItemParce = promoItemParcelable()
            myPromoItemParce.itemName = myPromoItem.itemName
            myPromoItemParce.itemOldPirce = myPromoItem.itemOldPirce
            myPromoItemParce.itemNewPrice = myPromoItem.itemNewPrice
            myPromoItemParce.itemImageLink = myPromoItem.itemImageLink
            var myInterface = FragmentPromoSaleBusiness() as (myInterfacesAddItem)
            Log.e(TAG, FragmentPromoSaleBusiness.promoItemListParcelable.size.toString())
            FragmentPromoSaleBusiness.promoItemListParcelable.add(myPromoItemParce)
            myInterface.addPromoItemBusiness(FragmentPromoSaleBusiness.promoItemListParcelable)

        }

        }


    }
}

