package com.creativehazio.swift.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_articles")
data class OfflineArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    val htmlContent: String
)
