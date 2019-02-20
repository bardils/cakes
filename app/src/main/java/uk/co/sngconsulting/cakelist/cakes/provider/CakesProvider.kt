/*
 * Copyright (c) 2019.  SnG Technologies Ltd
 */

package uk.co.sngconsulting.cakelist.cakes.provider

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.sngconsulting.cakelist.BuildConfig
import uk.co.sngconsulting.cakelist.cakes.model.data.CakesDatabase
import uk.co.sngconsulting.cakelist.cakes.model.repository.CakeRepository
import uk.co.sngconsulting.cakelist.cakes.model.retrofit.CakeListService

/**
 * Provider, used for lightweight dependency injection
 */
object CakesProvider {

    private const val baseUrl = "https://gist.githubusercontent.com"

    lateinit var cakeRepository : CakeRepository

    fun initialise(context: Context){

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val cakeListService = retrofit.create(CakeListService::class.java)

        val cakeRepositoryHandlerThread = HandlerThread("cakeRepositoryHandlerThread")
        cakeRepositoryHandlerThread.start()
        val cakeRepositoryHandler = Handler(cakeRepositoryHandlerThread.looper)


        val cakesDatabase = CakesDatabase.getInstance(context.applicationContext)

        cakeRepository = CakeRepository(cakeRepositoryHandler, cakeListService, cakesDatabase.cakesDao)
    }
}