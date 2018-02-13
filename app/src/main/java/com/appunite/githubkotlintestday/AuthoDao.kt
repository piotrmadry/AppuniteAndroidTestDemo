package com.appunite.githubkotlintestday

import com.appunite.githubkotlintestday.api.ApiService
import com.appunite.rx.ResponseOrError
import com.appunite.rx.dagger.NetworkScheduler
import okhttp3.ResponseBody
import rx.Observable
import rx.Scheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthoDao @Inject constructor(
        private val apiService: ApiService,
        @NetworkScheduler private val networkScheduler: Scheduler
) {
    fun authorizeUser(): Observable<ResponseOrError<ResponseBody>> {
        return apiService.authorizeUser()
                .subscribeOn(networkScheduler)
                .compose(ResponseOrError.toResponseOrErrorObservable())
    }
}