package com.appunite.githubkotlintestday.view.main

import android.app.Activity
import android.os.Bundle
import com.appunite.githubkotlintestday.R
import com.appunite.githubkotlintestday.TokenPreferences
import com.appunite.githubkotlintestday.dagger.annotations.DaggerAnnotation
import com.appunite.githubkotlintestday.dagger.module.BaseActivityModule
import com.appunite.githubkotlintestday.view.BaseActivity
import com.appunite.githubkotlintestday.view.login.LoginActivity
import dagger.Binds
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject lateinit var tokenPreferences: TokenPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (tokenPreferences.getToken() == null) {
            finishAffinity()
            startActivity(LoginActivity.newInstance(this))
        }
    }

    @dagger.Module(includes = [(BaseActivityModule::class)])
    abstract class Module {

        @Binds
        @DaggerAnnotation.ForActivity
        abstract fun provideActivity(activity: LoginActivity): Activity
    }
}
