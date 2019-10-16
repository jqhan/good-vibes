package com.luttu.good_vibes

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PostGameAPIService {
    @Headers("Content-Type: application/json")
    @POST("rocke/api/create.php")
    fun doPostGame(
        //@Query("Authorization") authorizationKey: String, // authentication header
        @Body gamePostData: Game
    ): Observable<PostGameResponse> // body data
}

