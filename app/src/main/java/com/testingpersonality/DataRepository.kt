package com.testingpersonality

import com.testingpersonality.data.local.PersonalityData
import com.testingpersonality.data.local.PersonalityDataDao
import kotlinx.coroutines.flow.Flow
import com.testingpersonality.data.local.DataDao
import com.testingpersonality.data.remote.DataRemoteDataSource
import com.testingpersonality.model.DataResponse
import com.testingpersonality.model.Question
import com.testingpersonality.utils.NetworkDataRepository
import com.testingpersonality.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class DataRepository @Inject constructor(
    private val remoteDataSource: DataRemoteDataSource,
    private val dataDao: DataDao,
    private val personalityDataDao: PersonalityDataDao
) {

    private var categoryList = listOf<String>()

    fun getAllCategoriesAndQuestions(): Flow<State<List<Question>>> {
        return object : NetworkDataRepository<List<Question>, DataResponse>() {

            override suspend fun saveRemoteData(response: DataResponse) {
                categoryList = response.categories
                dataDao.deleteAllQuestion()
                dataDao.insertAllQuestions(response.questions)
            }

            override fun fetchFromLocal(): Flow<List<Question>> = dataDao.getAll()

            override suspend fun fetchFromRemote(): Response<DataResponse> = remoteDataSource.fetchAllData()
        }.asFlow()
    }

    fun getCategoryList() = categoryList

    fun savePersonalityData(personalityData: PersonalityData){
        personalityDataDao.insert(personalityData)
    }

    fun getPersonalityData() = personalityDataDao.getPersonalityData()
}