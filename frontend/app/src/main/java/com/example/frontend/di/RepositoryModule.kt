package com.example.frontend.di

import com.example.frontend.data.EventRepositoryImpl
import com.example.frontend.data.ForumRepositoryImpl
import com.example.frontend.data.UserRepositoryImpl
import com.example.frontend.repository.EventRepository
import com.example.frontend.repository.ForumRepository
import com.example.frontend.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindForumRepository(
        forumRepositoryImpl: ForumRepositoryImpl
    ): ForumRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}

