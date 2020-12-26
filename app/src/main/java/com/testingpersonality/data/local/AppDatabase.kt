package com.testingpersonality.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.testingpersonality.data.OptionConverter
import com.testingpersonality.data.QuestionListConverter
import com.testingpersonality.data.QuestionTypeConverter
import com.testingpersonality.model.Question

@Database(entities = [Question::class], version = 1)
@TypeConverters(OptionConverter::class, QuestionListConverter::class,
                QuestionTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}