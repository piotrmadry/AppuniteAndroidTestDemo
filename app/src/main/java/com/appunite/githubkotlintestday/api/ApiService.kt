package com.appunite.githubkotlintestday.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import rx.Observable

interface ApiService {

    @GET("/authorizations")
    fun authorizeUser(): Observable<ResponseBody>

}
