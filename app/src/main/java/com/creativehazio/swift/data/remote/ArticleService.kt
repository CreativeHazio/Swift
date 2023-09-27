package com.creativehazio.swift.data.remote

import com.creativehazio.swift.BuildConfig
import com.creativehazio.swift.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ArticleService {

    @GET("top-headlines")
    suspend fun getArticleHeadlines(
        @Query("category")
        category: String? = "technology",
        @Query("page")
        pageNumber : Int = 1,
        @Query("country")
        country : String = "us",
        @Query("apiKey")
        apiKey : String = BuildConfig.NEWS_API_KEY
    ) : ArticlesResponse

    @GET("everything")
    suspend fun getLatestArticles(
        @Query("q")
        category: String? = "technology",
        @Query("pageSize")
        pageSize : Int = 30,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey: String = BuildConfig.NEWS_API_KEY
    ) : Response<ArticlesResponse>

    companion object {

        fun create(): ArticleService {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ArticleService::class.java)
        }
    }

}