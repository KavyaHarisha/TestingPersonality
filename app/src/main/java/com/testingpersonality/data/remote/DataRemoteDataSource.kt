package com.testingpersonality.data.remote

import com.testingpersonality.network.DataService
import javax.inject.Inject


class DataRemoteDataSource @Inject constructor(private val dataService: DataService) {

    suspend fun fetchAllData() = dataService.getDataResponse()
}