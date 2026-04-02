package com.adrianmalmierca.dijonevents.di

import com.adrianmalmierca.dijonevents.BuildConfig
import com.adrianmalmierca.dijonevents.data.api.DijonEventsApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module //because this class has functions that they know how to create dependencies
@InstallIn(SingletonComponent::class) //we define the scope where the dependencies will live
//in this case is singleton so all we provide here, will live while the app lives too
object AppModule {

    @Provides //to create an instance. Cause we cant use @inject because we use moshi, retrofit, OkHttp
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder() //Moshi is the json parser
        .addLast(KotlinJsonAdapterFactory()) //to manage nullables, default values...
        .build()

    //It creates the http client which will use retrofit
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }) //to take HTTP requests and responses
        //to avoid infinity blocks:
        .connectTimeout(30, TimeUnit.SECONDS) //max time to connect
        .readTimeout(30, TimeUnit.SECONDS) //max time ro receive a response
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL) //base API URL
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi)) //Transforms JSON ↔ objetos Kotlin (EventDto...)
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): DijonEventsApi =
        retrofit.create(DijonEventsApi::class.java) //it creates the implementation of the interface
}

/*
Workflow:
1. Hilt needs `DijonEventsApi`
2. Calls to `provideApi`
3. It needs `Retrofit`
4. It calls to `provideRetrofit`
5. Which needs:
   * `OkHttpClient` → `provideOkHttpClient`
   * `Moshi` → `provideMoshi`
Result: everything is automatically generated*/