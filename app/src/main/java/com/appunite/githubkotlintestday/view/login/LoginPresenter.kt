package com.appunite.githubkotlintestday.view.login

import com.appunite.githubkotlintestday.AuthoDao
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class LoginPresenter @Inject constructor(@Named("SignInClickObservable") val signInClickObservable: Observable<Pair<String, String>>, authoDao: AuthoDao) {


}