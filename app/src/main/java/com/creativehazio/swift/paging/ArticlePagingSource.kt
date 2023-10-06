package com.creativehazio.swift.paging

import android.app.Application
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.creativehazio.swift.R
import com.creativehazio.swift.data.repository.ArticleRepositoryImpl
import com.creativehazio.swift.domain.model.Article
import com.creativehazio.swift.util.ConnectivityObserver
import com.creativehazio.swift.util.Constants.Companion.NETWORK_PAGE_SIZE
import com.creativehazio.swift.util.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val NEWS_STARTING_PAGE_INDEX = 1

class ArticlePagingSource(
    private val articleRepository: ArticleRepositoryImpl,
) : PagingSource<Int, Article>() {

    companion object {
        var articleCategory : String? = "technology"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageIndex = params.key ?: NEWS_STARTING_PAGE_INDEX
        return try {

            val response = articleRepository.getArticles(
                page = pageIndex,
                category = articleCategory
            )

            val articles = response.body()?.articles.orEmpty()

            if (articles.isEmpty()) {
                LoadResult.Page(emptyList(), null, null)
            } else {
                val nextKey = pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                LoadResult.Page(
                    data = articles,
                    prevKey = if (pageIndex == NEWS_STARTING_PAGE_INDEX) null else pageIndex - 1,
                    nextKey = if (articles.isEmpty()) null else nextKey
                )
            }
        } catch (exception : IOException) {
            return LoadResult.Error(exception)
        } catch (exception : HttpException) {
            return LoadResult.Error(exception)
        } catch (exception : NullPointerException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true

}