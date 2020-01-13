package com.example.kent.hyperdeals.Model

import android.os.Parcel
import android.os.Parcelable




data class CategoryModel(
        var categoryName: String,
        var SelectedAll:Boolean,
        var Subcategories:ArrayList<SubcategoryModelx>

)
{
    var CategoryImage = ""
}

data class SubcategoryModelx(
        var SubcategoryName: String,
        var Selected:Boolean


)

class CategoryParse():Parcelable{

    var SelectedAll = false
    var categoryName = " "
    var CategoryImage = " "
    var Subcategories = arrayListOf<SubcategoryParse>()
    var Categories = ArrayList<String>()
    var isShowing = false

    constructor(parcel: Parcel) : this() {
        SelectedAll = parcel.readByte() != 0.toByte()
        categoryName = parcel.readString()
        CategoryImage = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (SelectedAll) 1 else 0)
        parcel.writeString(categoryName)
        parcel.writeString(CategoryImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryParse> {
        override fun createFromParcel(parcel: Parcel): CategoryParse {
            return CategoryParse(parcel)
        }

        override fun newArray(size: Int): Array<CategoryParse?> {
            return arrayOfNulls(size)
        }
    }


}

 class SubcategoryParse():Parcelable{

    var Selected = false
    var SubcategoryName = " "

     constructor(parcel: Parcel) : this() {
         Selected = parcel.readByte() != 0.toByte()
         SubcategoryName = parcel.readString()
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeByte(if (Selected) 1 else 0)
         parcel.writeString(SubcategoryName)
     }

     override fun describeContents(): Int {
         return 0
     }

     companion object CREATOR : Parcelable.Creator<SubcategoryParse> {
         override fun createFromParcel(parcel: Parcel): SubcategoryParse {
             return SubcategoryParse(parcel)
         }

         override fun newArray(size: Int): Array<SubcategoryParse?> {
             return arrayOfNulls(size)
         }
     }

 }










