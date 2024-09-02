package com.maestrovs.slovo.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.maestrovs.slovo.data.repository.MainRemoteData
import com.maestrovs.slovo.data.repository.MainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   /* @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(context: Context): Context = context*/

    @Provides
    fun providesBaseUrl() : String = "http://sum.in.ua/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL : String) : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client())
        .addConverterFactory(GsonConverterFactory.create(gson()))
        .build()

    fun client(): OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient()
        httpClient.newBuilder().addInterceptor(interceptor)
        //val client: OkHttpClient = Builder().addInterceptor(interceptor).build()

        return  httpClient
    }


    fun gson() = GsonBuilder()
            .setLenient()
            .create()


    @Provides
    @Singleton
    fun provideMainService(retrofit : Retrofit) : MainService = retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideMainRemoteData(mainService : MainService) : MainRemoteData = MainRemoteData(mainService)
}