package com.example.kent.hyperdeals.Admin

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


class DialogFragmentSelectPromo : DialogFragment() {
    var categoryList=ArrayList<CategoryParse>()
    lateinit var myAdapter:PreferenceAddAdapter
    val TAG = "DialogFragmentUser"
    fun newInstance(): DialogFragmentSelectPromo {
        return DialogFragmentSelectPromo()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {




        return inflater.inflate(R.layout.activity_dialog_fragment_category_user, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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



        return dialog
    }


}
