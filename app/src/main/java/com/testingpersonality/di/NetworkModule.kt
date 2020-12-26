package com.testingpersonality.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.testingpersonality.utils.Config
import com.testingpersonality.data.remote.DataRemoteDataSource
import com.testingpersonality.network.DataService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    private const val baseUrl = Config.BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit =  Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): DataService =
        retrofit.create(DataService::class.java)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userService: DataService) = DataRemoteDataSource(userService)
}