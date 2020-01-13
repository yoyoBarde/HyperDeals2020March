package com.example.kent.hyperdeals.Admin

import android.os.Parcel
import android.os.Parcelable


class AdminRewardsModelToRetrieve() : Parcelable {
    var adminImage: String = ""
    var adminIncentiveImage: String = ""
    var adminIncentiveRewardName: String = ""
    var adminIncentiveRewardPoints: String = ""


    constructor(parcel: Parcel) : this() {
        adminImage = parcel.readString()
        adminIncentiveImage = parcel.readString()
        adminIncentiveRewardName = parcel.readString()
        adminIncentiveRewardPoints = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(adminImage)
        parcel.writeString(adminIncentiveImage)
        parcel.writeString(adminIncentiveRewardName)
        parcel.writeString(adminIncentiveRewardPoints)
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdminRewardsModelToRetrieve> {
        override fun createFromParcel(parcel: Parcel): AdminRewardsModelToRetrieve {
            return AdminRewardsModelToRetrieve(parcel)
        }

        override fun newArray(size: Int): Array<AdminRewardsModelToRetrieve?> {
            return arrayOfNulls(size)
        }
    }
}
