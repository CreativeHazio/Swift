package com.creativehazio.swift.data.remote

import com.creativehazio.swift.domain.model.Article

data class ArticlesResponse(
    val articles : MutableList<Article>,
    val status : String,
    val totalResults : Int
)