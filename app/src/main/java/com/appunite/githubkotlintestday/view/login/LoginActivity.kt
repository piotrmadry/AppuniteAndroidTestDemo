package com.appunite.githubkotlintestday.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.appunite.githubkotlintestday.R
import com.appunite.githubkotlintestday.dagger.annotations.DaggerAnnotation
import com.appunite.githubkotlintestday.dagger.annotations.Scope
import com.appunite.githubkotlintestday.dagger.module.BaseActivityModule
import com.appunite.githubkotlintestday.view.BaseActivity
import com.jakewharton.rxbinding.view.RxView
import dagger.Binds
import dagger.Provides
import rx.Observable
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Named

class LoginActivity : BaseActivity() {

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }

    @Inject lateinit var presenter: LoginPresenter

    private val subscription: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        subscription.addAll(
                presenter.signInClickObservable
                        .subscribe { Toast.makeText(this, "CLICKED: " + it.first + " " + it.second, Toast.LENGTH_SHORT).show() }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.unsubscribe()
    }

    @dagger.Module(includes = [(BaseActivityModule::class)])
    abstract class Module {

        @Binds
        @DaggerAnnotation.ForActivity
        abstract fun provideActivity(activity: LoginActivity): Activity


        @dagger.Module
        companion object {

            @JvmStatic
            @Provides
            @Scope.Activity
            @Named("SignInClickObservable")
            fun provideUserCredentials(activity: LoginActivity): Observable<Pair<String, String>> = RxView
                    .clicks(activity.findViewById(R.id.login_button)).map {
                Pair(
                        activity.findViewById<EditText>(R.id.username_edit_text).text.toString(),
                        activity.findViewById<EditText>(R.id.password_edit_text).text.toString()
                )
            }.share()
        }
    }
}