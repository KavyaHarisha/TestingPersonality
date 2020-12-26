package com.testingpersonality.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val category: String,
    val question: String,
    val question_type: QuestionType
)