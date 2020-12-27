package com.testingpersonality.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personalityData")
data class PersonalityData(
    @PrimaryKey
    val question: String,
    val option: String)