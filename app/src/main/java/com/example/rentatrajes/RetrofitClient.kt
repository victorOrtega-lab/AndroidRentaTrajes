package com.example.rentatrajes

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Asegúrate que termina en "/"
    private const val BASE_URL = "https://gibraltar-volumes-subaru-borough.trycloudflare.com/api2/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) // 1) texto plano (e.g. "correcto")
            .addConverterFactory(GsonConverterFactory.create())    // 2) JSON (GET list)
            .build()
            .create(ApiService::class.java)
    }
}
