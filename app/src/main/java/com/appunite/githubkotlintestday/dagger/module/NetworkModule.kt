package com.appunite.githubkotlintestday.dagger.module

import com.appunite.githubkotlintestday.BuildConfig
import com.appunite.githubkotlintestday.TokenPreferences
import com.appunite.githubkotlintestday.api.ApiService
import com.appunite.githubkotlintestday.api.Constants
import com.appunite.githubkotlintestday.api.interceptors.HttpLoggingInterceptor
import com.appunite.rx.android.MyAndroidSchedulers
import com.appunite.rx.dagger.NetworkScheduler
import com.appunite.rx.dagger.UiScheduler
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, tokenPreferences: TokenPreferences): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder?.addHeader("Accept", "application/vnd.github.v3+json")
                    tokenPreferences.getToken()?.let { builder?.addHeader("Authorization", it) }
                    chain.proceed(builder.build())
                }
                .addInterceptor(loggingInterceptor)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideCache(@Named("CacheDir") cacheDir: File): Cache {
        val httpCacheDir = File(cacheDir, "cache")
        val httpCacheSize = (150 * 1024 * 1024).toLong() // 150 MiB
        return Cache(httpCacheDir, httpCacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.HOST_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    @NetworkScheduler
    fun provideNetworkScheduler() = Schedulers.io()

    @Provides
    @Singleton
    @UiScheduler
    fun provideUiScheduler() = MyAndroidSchedulers.mainThread()
}
