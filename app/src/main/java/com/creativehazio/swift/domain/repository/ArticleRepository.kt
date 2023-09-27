package com.creativehazio.swift.domain.repository

import com.creativehazio.swift.data.remote.ArticlesResponse
import retrofit2.Response

interface ArticleRepository {

    suspend fun getArticles(
        page : Int, category: String? = "technology"
    ) : Response<ArticlesResponse>

    suspend fun getArticleHeadlines() : ArticlesResponse
}