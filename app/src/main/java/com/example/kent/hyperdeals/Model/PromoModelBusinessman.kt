package com.example.kent.hyperdeals.Model

import com.google.firebase.firestore.GeoPoint

class   PromoModelBusinessman (



        var promoimage: String,
        var promoStore: String,
        var promoContactNumber: String,
        var promodescription: String,
       /* var promoPlace:LatLng,*/
        var promoPlace:String,

        var promoname:String,
        var promoLatLng:String,
        var promoImageLink: String,


    var promoGeo:GeoPoint,
        var subsubTag:String,

        var viewed:Int,
        var sent:Int,
        var interested:Int,
        var dismissed:Int,

        var startDateYear:Int,
        var startDateMonth:Int,
        var startDateDay:Int,
        var endDateYear:Int,
        var endDateMonth:Int,
        var endDateDay:Int,
        var startTimeHour:Int,
        var startTimeMinute:Int,
        var endTimeHour:Int,
        var endTimeMinute:Int



        ){
        var categoryLista=ArrayList<String>()

        var areaSqm:Double= 0.12
        var approved:Boolean = false
        var posterBy:String = " "


}