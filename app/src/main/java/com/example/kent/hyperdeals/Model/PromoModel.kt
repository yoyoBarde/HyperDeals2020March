package com.example.kent.hyperdeals.Model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import java.util.*


class PromoModel() : Parcelable {

        var promoID:String = " "
        var promoimage: String = ""
        var promoname: String = ""

        var promodescription: String = ""
        var promoPlace: String = ""
        var promoContactNumber: String = ""
        var promoCount: Int = 0
        var promoStore: String = ""
        var promoLatLng: String = ""
        var promoImageLink: String = ""
        var promoLocation: LatLng = LatLng(123.213, 12331.23)

        var promoCategories: String = ""

        var sent: Int = 0
        var viewed: Int = 0
        var interested: Int = 0
        var dismissed: Int = 0
        var distance: String = "0"
        lateinit var promoImageBitmap: Bitmap

        var startDateYear: Int = 0
        var startDateMonth: Int = 0
        var startDateDay: Int = 0
        var endDateYear: Int = 0
        var endDateMonth: Int = 0
        var endDateDay: Int = 0
        var startTimeHour: Int = 0
        var startTimeMinute: Int = 0
        var endTimeHour: Int = 0
        var endTimeMinute: Int = 0

        var relatedPromoMatched:Int = 0
        var preferenceMatched: Int = 0
        var categories = ArrayList<String>()
        var subcategories = ArrayList<String>()
        var approved: Boolean = false
        var posterBy: String = " "
        var areaSqm:Double = 0.21


        var startDateCalendar = Calendar.getInstance()
        var endDateCalendar =Calendar.getInstance()
        var hottestPoints = 0

        var userListView = ArrayList<UserModelParce>()
        var userListLikes = ArrayList<UserModelParce>()
        var userListPreferred = ArrayList<UserModelParce>()
        var userListAvailed = ArrayList<UserModelParce>()
        var userListDismissed = ArrayList<UserModelParce>()

        var demography_likesPoints:Double = 0.0
        var demography_viewPoints:Double = 0.0
        var demography_preferencePoints:Double = 0.0
        var demography_availedPoints:Double = 0.0
        var demography_dismissedPoints:Double = 0.0
        var demography_totalPoints:Double = 0.0

        var subcategory_likesCount:Int = 0
        var subcategory_viewCount:Int = 0
        var subcategory_preferenceCount:Int = 0
        var subcategory_availedCount:Int = 0
        var subcategory_dismissedCount:Int = 0

        var subcategory_likesPoints:Double = 0.0
        var subcategory_viewPoints:Double = 0.0
        var subcategory_preferencePoints:Double = 0.0
        var subcategory_availedPoints:Double = 0.0
        var subcategory_dismissedPoints:Double = 0.0
        var subcategory_totalPoints:Double = 0.0

        var totalPoints :Double = 0.0

        var genderMatchViews = 0
        var ageMatchViews = 0
        var statusMatchViews = 0

        var genderMatchLikes = 0
        var ageMatchLikes = 0
        var statusMatchLikes = 0

        var genderMatchPreferred = 0
        var ageMatchPreferred = 0
        var statusMatchPreferred = 0

        var genderMatchAvailed = 0
        var ageMatchAvailed = 0
        var statusMatchAvailed = 0

        var genderMatchDismissed = 0
        var ageMatchDismissed = 0
        var statusMatchDismissed = 0

        var genderMatchViewsPts = 0
        var ageMatchViewsPts = 0
        var statusMatchViewsPts = 0

        var genderMatchLikesPts = 0
        var ageMatchLikesPts = 0
        var statusMatchLikesPts = 0

        var genderMatchPreferredPts = 0
        var ageMatchPreferredPts = 0
        var statusMatchPreferredPts = 0

        var genderMatchAvailedPts = 0
        var ageMatchAvailedPts = 0
        var statusMatchAvailedPts = 0

        var genderMatchDismissedPts = 0
        var ageMatchDismissedPts = 0
        var statusMatchDismissedPts= 0




        constructor(parcel: Parcel) : this() {
                promoCategories = parcel.readString()
                promoimage = parcel.readString()
                promoname = parcel.readString()
                promodescription = parcel.readString()
                promoPlace = parcel.readString()
                promoContactNumber = parcel.readString()
                promoCount = parcel.readInt()
                promoStore = parcel.readString()
                promoLatLng = parcel.readString()
                promoImageLink = parcel.readString()
                promoLocation = parcel.readParcelable(LatLng::class.java.classLoader)
                sent = parcel.readInt()
                viewed = parcel.readInt()
                interested = parcel.readInt()
                dismissed = parcel.readInt()
                distance = parcel.readString()
                startDateYear = parcel.readInt()
                startDateMonth = parcel.readInt()
                startDateDay = parcel.readInt()
                endDateYear = parcel.readInt()
                endDateMonth = parcel.readInt()
                endDateDay = parcel.readInt()
                startTimeHour = parcel.readInt()
                startTimeMinute = parcel.readInt()
                endTimeHour = parcel.readInt()
                endTimeMinute = parcel.readInt()
                preferenceMatched = parcel.readInt()
                areaSqm = parcel.readDouble()
                approved = parcel.readByte() != 0.toByte()
                posterBy = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {

                parcel.writeString(promoCategories)
                parcel.writeString(promoimage)
                parcel.writeString(promoname)
                parcel.writeString(promodescription)
                parcel.writeString(promoPlace)
                parcel.writeString(promoContactNumber)
                parcel.writeInt(promoCount)
                parcel.writeString(promoStore)
                parcel.writeString(promoLatLng)
                parcel.writeString(promoImageLink)
                parcel.writeParcelable(promoLocation, flags)
                parcel.writeInt(sent)
                parcel.writeInt(viewed)
                parcel.writeInt(interested)
                parcel.writeInt(dismissed)
                parcel.writeString(distance)
                parcel.writeInt(startDateYear)
                parcel.writeInt(startDateMonth)
                parcel.writeInt(startDateDay)
                parcel.writeInt(endDateYear)
                parcel.writeInt(endDateMonth)
                parcel.writeInt(endDateDay)
                parcel.writeInt(startTimeHour)
                parcel.writeInt(startTimeMinute)
                parcel.writeInt(endTimeHour)
                parcel.writeInt(endTimeMinute)
                parcel.writeInt(preferenceMatched)
               parcel.writeDouble(areaSqm)

                parcel.writeByte(if (approved) 1 else 0)
                parcel.writeString(posterBy)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<PromoModel> {
                override fun createFromParcel(parcel: Parcel): PromoModel {
                        return PromoModel(parcel)
                }

                override fun newArray(size: Int): Array<PromoModel?> {
                        return arrayOfNulls(size)
                }
        }


}