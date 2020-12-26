package com.testingpersonality.network

import com.testingpersonality.model.DataResponse
import retrofit2.Response
import retrofit2.http.GET

interface DataService {

    @GET("database/personality_test.json")
    suspend fun getDataResponse() : Response<DataResponse>
}