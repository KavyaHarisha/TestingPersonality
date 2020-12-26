package com.testingpersonality.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OptionConverter {
    var gson = Gson()

    @TypeConverter
    fun optionToString(option: List<String>): String {
        return gson.toJson(option)
    }

    @TypeConverter
    fun stringToOption(data: String): List<String> {
        val option = object : TypeToken<List<String>>() {
        }.type
        return gson.fromJson(data, option)
    }
}