package com.example.frontend.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.frontend.data.TokenStore
import com.example.frontend.network.RestApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


//private const val BASE_URL =
//    "http://10.0.2.2:8080/"
//// NB https://stackoverflow.com/questions/5495534/java-net-connectexception-localhost-127-0-0-18080-connection-refused

private const val BASE_URL =
    "https://orbital-backend-6z61.onrender.com"


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    @Singleton
//    fun provideRestApi(): RestApiService {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(RestApiService::class.java)
//    }

    @Provides
    @Singleton
    fun provideTokenStore(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

//    @Provides
//    @Singleton
//    @Named("EventRepository")
//    fun provideEventRepository(api: RestApiService): EventRepository {
//        return EventRepositoryImpl(api)
//    }
//
//    @Provides
//    @Singleton
//    @Named("ForumRepository")
//    fun provideForumRepository(api: RestApiService): ForumRepository {
//        return ForumRepositoryImpl(api)
//    }
//
//    @Provides
//    @Singleton
//    @Named("UserRepository")
//    fun provideUserRepository(api: RestApiService): UserRepository {
//        return UserRepositoryImpl(api)
//    }

//
//    @Provides
//    @Singleton
//    @Named("hello2")
//    fun provideString2() = "Hello 2"

}