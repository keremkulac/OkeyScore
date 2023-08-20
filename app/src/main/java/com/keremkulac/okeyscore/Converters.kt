package com.keremkulac.okeyscore

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    /*
    @TypeConverter
    fun fromIntegerList(list: List<Int?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIntegerList(json: String?): List<Int?>? {
        val type = object : TypeToken<List<Int?>>() {}.type
        val gson = Gson()
        return gson.fromJson(json, type)
    }

     */
    @TypeConverter
    fun fromIntegerList(list: List<Int?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIntegerList(json: String?): List<Int?>? {
        val type = object : TypeToken<List<Int?>>() {}.type
        val gson = Gson()
        return gson.fromJson(json, type)
    }
}