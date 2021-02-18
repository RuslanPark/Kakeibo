package com.ruslanpark.kakeiboapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "purchaseTable")
data class Table (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "day_id") val dayId : Int,
    @ColumnInfo(name = "purchases") val purchases : ArrayList< Pair<String, Int> >
)

class PurchasesTypeConverter {
    @TypeConverter
    fun listToJson(value: ArrayList< Pair<String, Int> >): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) : ArrayList< Pair<String, Int> > {
        val listType =  object: TypeToken< ArrayList< Pair<String, Int> > >(){}.type
        return Gson().fromJson(value, listType)
    }
}