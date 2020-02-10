package com.example.kent.hyperdeals.Model

import android.os.Parcel
import android.os.Parcelable

class UserModel(var FirstName:String,var LastName:String,var Email:String,var Password:String,
                var Age: String, var Gender: String, var Status: String){

}
class UserModelParce():Parcelable{
    var FirstName:String = " "
    var LastName:String = " "
    var Email:String = " "
    var Password:String = " "
    var Age: String = " "
    var Gender: String = " "
    var Status: String = " "

    constructor(parcel: Parcel) : this() {
        FirstName = parcel.readString()
        LastName = parcel.readString()
        Email = parcel.readString()
        Password = parcel.readString()
        Age = parcel.readString()
        Gender = parcel.readString()
        Status = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(FirstName)
        parcel.writeString(LastName)
        parcel.writeString(Email)
        parcel.writeString(Password)
        parcel.writeString(Age)
        parcel.writeString(Gender)
        parcel.writeString(Status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModelParce> {
        override fun createFromParcel(parcel: Parcel): UserModelParce {
            return UserModelParce(parcel)
        }

        override fun newArray(size: Int): Array<UserModelParce?> {
            return arrayOfNulls(size)
        }
    }

}

