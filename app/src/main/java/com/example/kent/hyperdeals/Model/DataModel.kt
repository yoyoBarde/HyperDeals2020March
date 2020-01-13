package com.example.kent.hyperdeals.Model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.kent.hyperdeals.LoginActivity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import org.jetbrains.anko.doAsync


interface myInterfacesUpdateSubcategoryCount {
    fun saveSubcategoryUpdate( )


}
 interface myInterfaces{
    fun saveCategoriesBusiness( myCategoryList:ArrayList<CategoryParse>)

}


interface myInterfacesCategories{
    fun addCategoriesUser(myCategoryList:ArrayList<CategoryParse>)

}
interface  myInterfacesAddItem{

    fun addPromoItemBusiness(promoItemListParcelable: ArrayList<promoItemParcelable>)
}


class UserModelParcelable():Parcelable{
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    companion object CREATOR : Parcelable.Creator<UserModelParcelable> {
        override fun createFromParcel(parcel: Parcel): UserModelParcelable {
            return UserModelParcelable(parcel)
        }

        override fun newArray(size: Int): Array<UserModelParcelable?> {
            return arrayOfNulls(size)
        }
    }


}
data class promoSubcategory(var subcategoryName:String)


data class promoItem(var itemName:String,
                      var itemOldPirce:Int,
                      var itemNewPrice:Int,
                      var itemImageLink:String
)
class promoItemParcelable():Parcelable{
    var itemName = ""
    var itemOldPirce = 0
    var itemNewPrice = 0
    var itemImageLink = ""

    constructor(parcel: Parcel) : this() {
        itemName = parcel.readString()
        itemOldPirce = parcel.readInt()
        itemNewPrice = parcel.readInt()
        itemImageLink = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemName)
        parcel.writeInt(itemOldPirce)
        parcel.writeInt(itemNewPrice)
        parcel.writeString(itemImageLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<promoItemParcelable> {
        override fun createFromParcel(parcel: Parcel): promoItemParcelable {
            return promoItemParcelable(parcel)
        }

        override fun newArray(size: Int): Array<promoItemParcelable?> {
            return arrayOfNulls(size)
        }
    }

}

data class userPromoiked(var storeName:String)
class userPromoLikedParce() :Parcelable{
    var storeName = ""

    constructor(parcel: Parcel) : this() {
        storeName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<userPromoLikedParce> {
        override fun createFromParcel(parcel: Parcel): userPromoLikedParce {
            return userPromoLikedParce(parcel)
        }

        override fun newArray(size: Int): Array<userPromoLikedParce?> {
            return arrayOfNulls(size)
        }
    }
}
data class promoLikesCount(var LikeCount:Int)
class promoLikesCountParce() :Parcelable{
    var LikeCount = 0

    constructor(parcel: Parcel) : this() {
        LikeCount = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(LikeCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<promoLikesCountParce> {
        override fun createFromParcel(parcel: Parcel): promoLikesCountParce {
            return promoLikesCountParce(parcel)
        }

        override fun newArray(size: Int): Array<promoLikesCountParce?> {
            return arrayOfNulls(size)
        }
    }

}

data class userLike(var userName:String,var liked:Boolean)

 class userLikeParce() :Parcelable

 {
     var userName=" "
     var liked= false

     constructor(parcel: Parcel) : this() {
         userName = parcel.readString()
         liked = parcel.readByte() != 0.toByte()
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(userName)
         parcel.writeByte(if (liked) 1 else 0)
     }

     override fun describeContents(): Int {
         return 0
     }

     companion object CREATOR : Parcelable.Creator<userLikeParce> {
         override fun createFromParcel(parcel: Parcel): userLikeParce {
             return userLikeParce(parcel)
         }

         override fun newArray(size: Int): Array<userLikeParce?> {
             return arrayOfNulls(size)
         }
     }
 }




data class promoViews(var promoViews:Int)
class promoViewsParde() :Parcelable{

    var promoViews = 0

    constructor(parcel: Parcel) : this() {
        promoViews = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(promoViews)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<promoViewsParde> {
        override fun createFromParcel(parcel: Parcel): promoViewsParde {
            return promoViewsParde(parcel)
        }

        override fun newArray(size: Int): Array<promoViewsParde?> {
            return arrayOfNulls(size)
        }
    }
}
data class userPromoViews(var date:String)
class userPromoViewsParce():Parcelable{

    var date = " "

    constructor(parcel: Parcel) : this() {
        date = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<userPromoViewsParce> {
        override fun createFromParcel(parcel: Parcel): userPromoViewsParce {
            return userPromoViewsParce(parcel)
        }

        override fun newArray(size: Int): Array<userPromoViewsParce?> {
            return arrayOfNulls(size)
        }
    }





}



class promoCategoryParce():Parcelable{
    var CategoryName = " "

    constructor(parcel: Parcel) : this() {
        CategoryName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(CategoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<promoCategoryParce> {
        override fun createFromParcel(parcel: Parcel): promoCategoryParce {
            return promoCategoryParce(parcel)
        }

        override fun newArray(size: Int): Array<promoCategoryParce?> {
            return arrayOfNulls(size)
        }
    }

}


class promoSubcategoryParce() :Parcelable{
    var SubcategoryName = " "

    constructor(parcel: Parcel) : this() {
        SubcategoryName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(SubcategoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<promoSubcategoryParce> {
        override fun createFromParcel(parcel: Parcel): promoSubcategoryParce {
            return promoSubcategoryParce(parcel)
        }

        override fun newArray(size: Int): Array<promoSubcategoryParce?> {
            return arrayOfNulls(size)
        }
    }


}



data class UserBusinessman(var firstname:String,
                           var lastname:String,
                           var email:String,
                           var password:String,
                           var  stores:ArrayList<String>

)


 class UserBusinessmanvarParce() :Parcelable{
     var firstname:String = " "
     var lastname: String = " "
     var email: String = " "
     var password: String = " "
     var stores = ArrayList<String>()

     constructor(parcel: Parcel) : this() {
         firstname = parcel.readString()
         lastname = parcel.readString()
         email = parcel.readString()
         password = parcel.readString()
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(firstname)
         parcel.writeString(lastname)
         parcel.writeString(email)
         parcel.writeString(password)
     }

     override fun describeContents(): Int {
         return 0
     }

     companion object CREATOR : Parcelable.Creator<UserBusinessmanvarParce> {
         override fun createFromParcel(parcel: Parcel): UserBusinessmanvarParce {
             return UserBusinessmanvarParce(parcel)
         }

         override fun newArray(size: Int): Array<UserBusinessmanvarParce?> {
             return arrayOfNulls(size)
         }
     }
 }

 class AgeTargetParcelable() :Parcelable {
     override fun writeToParcel(dest: Parcel?, flags: Int) {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

     override fun describeContents(): Int {
         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
     }

     var young: Boolean = false
        var teenager: Boolean = false
        var adult: Boolean = false

     constructor(parcel: Parcel) : this() {
         young = parcel.readByte() != 0.toByte()
         teenager = parcel.readByte() != 0.toByte()
         adult = parcel.readByte() != 0.toByte()
     }

     companion object CREATOR : Parcelable.Creator<AgeTargetParcelable> {
         override fun createFromParcel(parcel: Parcel): AgeTargetParcelable {
             return AgeTargetParcelable(parcel)
         }

         override fun newArray(size: Int): Array<AgeTargetParcelable?> {
             return arrayOfNulls(size)
         }
     }
 }
data class AgeTarget (
        var young: Boolean,
        var teenager: Boolean,
        var adult: Boolean
)

data class GenderTarget(
        var male: Boolean,
        var female : Boolean
)
class GenderTargetParcelable() :Parcelable{
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var male: Boolean = false
    var female : Boolean = false

    constructor(parcel: Parcel) : this() {
        male = parcel.readByte() != 0.toByte()
        female = parcel.readByte() != 0.toByte()
    }

    companion object CREATOR : Parcelable.Creator<GenderTargetParcelable> {
        override fun createFromParcel(parcel: Parcel): GenderTargetParcelable {
            return GenderTargetParcelable(parcel)
        }

        override fun newArray(size: Int): Array<GenderTargetParcelable?> {
            return arrayOfNulls(size)
        }
    }
}

data class StatusTarget(
        var single: Boolean,
        var inarelationshop: Boolean
)

class StatusTargetParcelable():Parcelable{
    var single: Boolean = false
    var inarelationshop: Boolean = false

    constructor(parcel: Parcel) : this() {
        single = parcel.readByte() != 0.toByte()
        inarelationshop = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (single) 1 else 0)
        parcel.writeByte(if (inarelationshop) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StatusTargetParcelable> {
        override fun createFromParcel(parcel: Parcel): StatusTargetParcelable {
            return StatusTargetParcelable(parcel)
        }

        override fun newArray(size: Int): Array<StatusTargetParcelable?> {
            return arrayOfNulls(size)
        }
    }

}
data class DemoTarget(var promoID:String , var myAgeTarget:AgeTarget, var myGenderTarger: GenderTarget, var myStatusTarget: StatusTarget)







data class StoreModel (
            var storeImage:String,
            var storeName:String,
            var storeContact:String,
            var storeDescription:String,
            var storeLink:String,
            var storeLatLng: GeoPoint,
            var storeAddress:String,
            var storeCategories:ArrayList<String>,
            var storeOpenTime:String,
            var storeCloseTime:String,
            var storeBy:String




            )
 class storeModelParce():Parcelable{
     var storeImage = " "
     var storeName = " "
     var storeContact = " "
     var storeDescription = " "
     var storeLink = " "
         var storeLatLng = GeoPoint(121.12,123.123)
     var storeAddress= " "
     var storeCategories =ArrayList<String> ()
     var storeOpenTime = " "
     var storeCloseTime = " "
     var storeBy = " "

     constructor(parcel: Parcel) : this() {
         storeImage = parcel.readString()
         storeName = parcel.readString()
         storeContact = parcel.readString()
         storeDescription = parcel.readString()
         storeLink = parcel.readString()
         storeAddress = parcel.readString()
         storeOpenTime = parcel.readString()
         storeCloseTime = parcel.readString()
         storeBy = parcel.readString()
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(storeImage)
         parcel.writeString(storeName)
         parcel.writeString(storeContact)
         parcel.writeString(storeDescription)
         parcel.writeString(storeLink)
         parcel.writeString(storeAddress)
         parcel.writeString(storeOpenTime)
         parcel.writeString(storeCloseTime)
         parcel.writeString(storeBy)
     }

     override fun describeContents(): Int {
         return 0
     }

     companion object CREATOR : Parcelable.Creator<storeModelParce> {
         override fun createFromParcel(parcel: Parcel): storeModelParce {
             return storeModelParce(parcel)
         }

         override fun newArray(size: Int): Array<storeModelParce?> {
             return arrayOfNulls(size)
         }
     }

 }
data class userPreferredTime(var startTimeHour:Int,
                             var startTimeMinutes:Int,
                             var endTimeHour:Int,
                             var endTimeMinutes:Int,
                            var enabled:Boolean
                        )

 class userPreferredTimeParce() :Parcelable{

    var startTimeHour:Int= 0
    var startTimeMinutes:Int= 0
    var endTimeHour:Int= 0
    var endTimeMinutes:Int= 0
    var enabled = false

     constructor(parcel: Parcel) : this() {
         startTimeHour = parcel.readInt()
         startTimeMinutes = parcel.readInt()
         endTimeHour = parcel.readInt()
         endTimeMinutes = parcel.readInt()
         enabled = parcel.readByte() != 0.toByte()
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeInt(startTimeHour)
         parcel.writeInt(startTimeMinutes)
         parcel.writeInt(endTimeHour)
         parcel.writeInt(endTimeMinutes)
         parcel.writeByte(if (enabled) 1 else 0)
     }

     override fun describeContents(): Int {
         return 0
     }

     companion object CREATOR : Parcelable.Creator<userPreferredTimeParce> {
         override fun createFromParcel(parcel: Parcel): userPreferredTimeParce {
             return userPreferredTimeParce(parcel)
         }

         override fun newArray(size: Int): Array<userPreferredTimeParce?> {
             return arrayOfNulls(size)
         }
     }

 }
data class userHistory(var promoName:String,var date:String)
class userHistoryParce():Parcelable{

    var promoName = " "
    var date = " "

    constructor(parcel: Parcel) : this() {
        promoName = parcel.readString()
        date = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(promoName)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<userHistoryParce> {
        override fun createFromParcel(parcel: Parcel): userHistoryParce {
            return userHistoryParce(parcel)
        }

        override fun newArray(size: Int): Array<userHistoryParce?> {
            return arrayOfNulls(size)
        }
    }





}


data class UserCategoriesPreferences(
        var CategoryName:String,
        var viewCount:Int

)

data class UserSubcategoriesPreferences(
        var subCategoryName:String,
        var viewCount:Int

)
class UserSubcategoriesPreferencesParcelable():Parcelable{

    var subCategoryName:String = ""
    var viewCount:Int = 0

    constructor(parcel: Parcel) : this() {
        subCategoryName = parcel.readString()
        viewCount = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subCategoryName)
        parcel.writeInt(viewCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserSubcategoriesPreferencesParcelable> {
        override fun createFromParcel(parcel: Parcel): UserSubcategoriesPreferencesParcelable {
            return UserSubcategoriesPreferencesParcelable(parcel)
        }

        override fun newArray(size: Int): Array<UserSubcategoriesPreferencesParcelable?> {
            return arrayOfNulls(size)
        }
    }

}
class UserCategoriesPreferencesParcelabl() :Parcelable{
    var CategoryName:String = " "
    var viewCount:Int = 0

    constructor(parcel: Parcel) : this() {
        CategoryName = parcel.readString()
        viewCount = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(CategoryName)
        parcel.writeInt(viewCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserCategoriesPreferencesParcelabl> {
        override fun createFromParcel(parcel: Parcel): UserCategoriesPreferencesParcelabl {
            return UserCategoriesPreferencesParcelabl(parcel)
        }

        override fun newArray(size: Int): Array<UserCategoriesPreferencesParcelabl?> {
            return arrayOfNulls(size)
        }
    }

}





