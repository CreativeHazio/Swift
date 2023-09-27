package com.creativehazio.swift.data.localdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.creativehazio.swift.domain.model.Article
import com.creativehazio.swift.domain.model.OfflineArticle

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: Article) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOfflineArticle(article: OfflineArticle) : Long

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllSavedArticles() : LiveData<List<Article>>

    @Query("SELECT * FROM offline_articles WHERE url = :url")
    fun getOfflineArticle(url: String): OfflineArticle?
}