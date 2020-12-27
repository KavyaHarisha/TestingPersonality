package com.testingpersonality.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonalityDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(personalityData: PersonalityData)

    @Query("SELECT * FROM personalityData")
    fun getPersonalityData(): List<PersonalityData>
}