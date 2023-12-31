package com.creativehazio.swift.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    val author : String?,
    val content : String?,
    val description : String?,
    val publishedAt : String?,
    val source : Source?,
    val title : String?,
    val url : String?,
    val urlToImage : String?
) : Parcelable
