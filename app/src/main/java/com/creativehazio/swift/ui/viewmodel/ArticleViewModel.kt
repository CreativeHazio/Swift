package com.creativehazio.swift.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.creativehazio.swift.R
import com.creativehazio.swift.data.remote.ArticlesResponse
import com.creativehazio.swift.data.repository.ArticleRepositoryImpl
import com.creativehazio.swift.domain.model.Article
import com.creativehazio.swift.paging.ArticlePagingSource
import com.creativehazio.swift.util.ConnectivityObserver
import com.creativehazio.swift.util.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepositoryImpl,
    private val application: Application
) : ViewModel() {

    private val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)

    private val _networkStatus = MutableSharedFlow<String>()
    val networkStatus = _networkStatus.asSharedFlow()

    private val _articleHeadlineFlow = MutableStateFlow(
        ArticlesResponse(
            mutableListOf(Article(
                null, null,null, null, null,
                null, null, null, null
            )),
            "initialValue",
            0
        )
    )
    val articleHeadLineFlow = _articleHeadlineFlow.asStateFlow()

    init {
        observeNetworkAndMakeArticleHeadlineRequest()
    }

    val articlesFlow = Pager(PagingConfig(pageSize = 20)) {
        ArticlePagingSource(articleRepository)
    }.flow.cachedIn(viewModelScope)

    private fun getArticleHeadlines() {
        viewModelScope.launch {
            _articleHeadlineFlow.value = articleRepository.getArticleHeadlines()
        }
    }

    private fun observeNetworkAndMakeArticleHeadlineRequest() {
        connectivityObserver.observe().onEach {
            when(it) {
                ConnectivityObserver.Status.Available -> {
                    getArticleHeadlines()

                    _networkStatus.emit(
                        application.applicationContext.getString(R.string.network_available)
                    )
                }
                ConnectivityObserver.Status.Unavailable -> {
                    _networkStatus.emit(
                        application.applicationContext.getString(R.string.network_unavailable)
                    )
                }
                ConnectivityObserver.Status.Losing -> {
                    _networkStatus.emit(
                        application.applicationContext.getString(R.string.slow_internet_connection)
                    )
                }
                ConnectivityObserver.Status.Lost -> {
                    _networkStatus.emit(
                        application.applicationContext.getString(R.string.network_lost)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}