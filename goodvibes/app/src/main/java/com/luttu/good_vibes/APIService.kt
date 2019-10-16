package com.luttu.good_vibes

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class ApiService(val url: String) {
    fun postGameAPICall() = Retrofit.Builder()
        .baseUrl(url)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ApiWorker.gsonConverter)
        .client(ApiWorker.client)
        .build()
        .create(PostGameAPIService::class.java)!!
}