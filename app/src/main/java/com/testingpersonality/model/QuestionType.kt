package com.testingpersonality.model

import com.testingpersonality.model.Condition

data class QuestionType(
    val condition: Condition?,
    val options: List<String>,
    val type: String
)