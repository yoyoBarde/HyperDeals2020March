package com.example.kent.hyperdeals.NavigationOptionsActivity

import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.TimePicker
import com.example.kent.hyperdeals.LoginActivity
import com.example.kent.hyperdeals.Model.userPreferredTime
import com.example.kent.hyperdeals.Model.userPreferredTimeParce
import com.example.kent.hyperdeals.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_preferred_time.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class UserPreferredTime : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var selected = 0
    val TAG = "UserPreferredTime"
    var startTimeHour =8
    var startTimeMinutes = 0
    var endTimeHour = 5
    var endTimeMinutes = 0
    var prefTimeEnabled:Boolean = false
    var database = FirebaseFirestore.getInstance()
    lateinit var userPrefer:userPreferredTimeParce
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_preferred_time)
        doAsync {
            database.collection("UserPreferredTime").document(LoginActivity.userUIDS).get().addOnSuccessListener { document ->

                if (document.exists()) {

                    var promoLikeCountParce = document.toObject(userPreferredTimeParce::class.java)
                    uiThread {
                        var amorpm = "am"
                        var amorpmend= "pm"
                        var subHour = promoLikeCountParce.startTimeHour.toString()
                        var subMinute  = promoLikeCountParce.startTimeMinutes.toString()
                        var subHourEnd = promoLikeCountParce.endTimeHour.toString()
                        var subMinuteEnd = promoLikeCountParce.endTimeMinutes.toString()
                        if(promoLikeCountParce.startTimeHour>=12){

                                amorpm = "pm"
                               subHour =  (promoLikeCountParce.startTimeHour - 12).toString()


                        }
                        if(subMinute.toString().length==1)
                            subMinute ="0${subMinute.toString()}"


                        if(promoLikeCountParce.endTimeHour>=12){

                            amorpmend = "pm"
                            subHourEnd =  (promoLikeCountParce.endTimeHour - 12).toString()


                        }
                        if(subMinuteEnd.toString().length==1)
                            subMinuteEnd ="0${subMinuteEnd.toString()}"



                        tv_startTime.text = "${subHour}:${subMinute} $amorpm"
                        tv_endTime.text = "${subHourEnd}:${subMinuteEnd} ${amorpmend}"

                    switch_on.isChecked = promoLikeCountParce.enabled
                    }
                    startTimeHour = promoLikeCountParce.startTimeHour
                    startTimeMinutes = promoLikeCountParce.startTimeMinutes
                    endTimeHour =  promoLikeCountParce.endTimeHour
                    endTimeMinutes = promoLikeCountParce.endTimeMinutes
                    userPrefer = promoLikeCountParce
                    setTimePicker(promoLikeCountParce)

                } else {
                    Log.e(TAG, "dont exist")
                }

            }
        }




        switch_on.setOnCheckedChangeListener { compoundButton, b ->

            prefTimeEnabled = b
            btn_set.visibility = View.VISIBLE
        }

        btn_set.setOnClickListener {
            btn_set.visibility = View.GONE

          var userPrefTIme  =   userPreferredTime(startTimeHour,startTimeMinutes,endTimeHour,endTimeMinutes,prefTimeEnabled)
            database.collection("UserPreferredTime").document(LoginActivity.userUIDS).set(userPrefTIme)

        finish()

        }
    }

    fun setTimePicker(userPrefered:userPreferredTimeParce){
        tv_startTime.setOnClickListener {
            selected  = 1
            var myTimepicker =  TimePickerDialog(this,this,userPrefered.startTimeHour,userPrefered.startTimeMinutes, DateFormat.is24HourFormat(this))
            myTimepicker.show()
        }
        tv_endTime.setOnClickListener {
            selected = 2
            var myTimepicker =  TimePickerDialog(this,this,userPrefered.endTimeHour,userPrefered.endTimeMinutes,DateFormat.is24HourFormat(this))
            myTimepicker.show()

        }


    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        var subHour=p1.toString()
        var subMinute=p2.toString()
        var amorpm = "am"
        btn_set.visibility = View.VISIBLE
        if(selected==1){
            if(p1==0)
                p1 -1
            if(p1>=12) {
                amorpm = "pm"
                p1 - 12

            }
            var kk = Calendar.getInstance()
            kk.set(2019,3,27,p1,p2)
            startTimeHour = p1
            startTimeMinutes= p2
            Log.e(TAG,"${kk.time}   ${kk.timeInMillis}")
            if(p2.toString().length==1)
                subMinute ="0${p2.toString()}"

            if(p1>12)
                subHour =(p1-12).toString()


            tv_startTime.text = "${subHour}:${subMinute} $amorpm"

        }
        else if (selected==2){
            if(p1==0)
                p1 -1
            if(p1>=12) {
                amorpm = "pm"
                p1 - 12

            }
            var kk = Calendar.getInstance()
            kk.set(2019,3,27,p1,p2)
            endTimeHour = p1
            endTimeMinutes = p2
            Log.e(TAG,"${kk.time}   ${kk.timeInMillis}")
            if(p2.toString().length==1)
                subMinute ="0${p2.toString()}"

            if(p1>12)
                subHour =(p1-12).toString()
            tv_endTime.text = "${subHour}:${subMinute} $amorpm"


        }

    }
}
