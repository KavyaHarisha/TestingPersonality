package com.testingpersonality.data.local

import androidx.room.*
import com.testingpersonality.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    @Query("SELECT * FROM question")
    fun getAll(): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllQuestions(movies: List<Question>)

    @Query("DELETE FROM question")
    suspend fun deleteAllQuestion()

}