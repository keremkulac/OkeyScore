package com.keremkulac.okeyscore.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayerConverter {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return if (value.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(value, listType)
        }
    }
    @TypeConverter
    fun fromList(list: List<String>?): String {
        return Gson().toJson(list)
    }
}