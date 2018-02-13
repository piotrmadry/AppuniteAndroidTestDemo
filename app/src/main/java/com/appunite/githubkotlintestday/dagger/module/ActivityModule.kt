package com.appunite.githubkotlintestday.dagger.module

import com.appunite.githubkotlintestday.dagger.annotations.Scope
import com.appunite.githubkotlintestday.view.login.LoginActivity
import com.appunite.githubkotlintestday.view.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @Scope.Activity
    @ContributesAndroidInjector(modules = [(LoginActivity.Module::class)])
    abstract fun provideLoginActivity(): LoginActivity

    @Scope.Activity
    @ContributesAndroidInjector(modules = [(MainActivity.Module::class)])
    abstract fun provideMainActivity(): MainActivity

}
