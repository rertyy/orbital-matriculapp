package com.example.frontend.di

import android.content.Context
import com.example.frontend.data.TokenStore
import com.example.frontend.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


private const val BASE_URL =
    "http://10.0.2.2:8080/"
//// NB https://stackoverflow.com/questions/5495534/java-net-connectexception-localhost-127-0-0-18080-connection-refused

//private const val BASE_URL =
//    "https://orbital-backend-6z61.onrender.com"


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenStore(@ApplicationContext context: Context): TokenStore {
//        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return TokenStore(context)
    }


//
//    @Provides
//    @Singleton
//    @Named("hello2")
//    fun provideString2() = "Hello 2"

}