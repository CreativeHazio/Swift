package com.creativehazio.swift.di

import android.app.Application
import com.creativehazio.swift.data.localdb.ArticleDatabase
import com.creativehazio.swift.data.remote.ArticleService
import com.creativehazio.swift.data.repository.ArticleRepositoryImpl
import com.creativehazio.swift.domain.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideArticleService() : ArticleService {
        return ArticleService.create()
    }

    @Provides
    @Singleton
    fun provideArticleDatabase(app : Application) : ArticleDatabase {
        return ArticleDatabase(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(
        articleService: ArticleService,
        articleDatabase: ArticleDatabase
    ) : ArticleRepository {
        return ArticleRepositoryImpl(articleService, articleDatabase)
    }
}