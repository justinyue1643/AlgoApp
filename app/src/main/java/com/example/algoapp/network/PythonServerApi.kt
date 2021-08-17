package com.example.algoapp.network

import com.example.algoapp.models.PythonResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://algoapp-python.herokuapp.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface PythonServerService {
    @GET("/run")
    suspend fun getResult(@Query("setup_code") setupCode: String, @Query("runnable_code") runnableCode: String): PythonResponse
}

object PythonServerApi {
    val retrofitService: PythonServerService by lazy {
        retrofit.create(PythonServerService::class.java)
    }
}
