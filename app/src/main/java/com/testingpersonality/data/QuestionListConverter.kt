package com.testingpersonality.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testingpersonality.model.Question

class QuestionListConverter {
    var gson = Gson()

    @TypeConverter
    fun optionToString(option: List<Question>): String {
        return gson.toJson(option)
    }

    @TypeConverter
    fun stringToOption(data: String): List<Question> {
        val option = object : TypeToken<List<Question>>() {
        }.type
        return gson.fromJson(data, option)
    }
}