package com.creativehazio.swift.data.repository

import com.creativehazio.swift.data.localdb.ArticleDatabase
import com.creativehazio.swift.data.remote.ArticleService
import com.creativehazio.swift.data.remote.ArticlesResponse
import retrofit2.Response
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleService: ArticleService,
    private val articleDatabase: ArticleDatabase
) : com.creativehazio.swift.domain.repository.ArticleRepository {

    override suspend fun getArticles(page: Int, category: String?): Response<ArticlesResponse> {
        return articleService.getLatestArticles(pageNumber = page, category = category)
    }

    override suspend fun getArticleHeadlines(): ArticlesResponse {
        return articleService.getArticleHeadlines()
    }
}