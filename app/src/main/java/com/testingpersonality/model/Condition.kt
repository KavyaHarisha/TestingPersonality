package com.testingpersonality.model

import com.myapplication.model.Predicate

data class Condition(
    val if_positive: IfPositive,
    val predicate: Predicate
)