package com.testingpersonality.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testingpersonality.model.QuestionType

class QuestionTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun questionTypeToString(questionType: QuestionType): String {
        return gson.toJson(questionType)
    }

    @TypeConverter
    fun questionTypeToOption(data: String): QuestionType {
        val questionType = object : TypeToken<QuestionType>() {
        }.type
        return gson.fromJson(data, questionType)
    }
}