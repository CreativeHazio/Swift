package com.creativehazio.swift.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.creativehazio.swift.R
import com.creativehazio.swift.adapter.ArticleHeadLineAdapter
import com.creativehazio.swift.adapter.ArticleListAdapter
import com.creativehazio.swift.adapter.ArticleTopicAdapter
import com.creativehazio.swift.databinding.FragmentHomeBinding
import com.creativehazio.swift.paging.ArticlePagingSource
import com.creativehazio.swift.ui.viewmodel.ArticleViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var articleHeadLineAdapter: ArticleHeadLineAdapter
    private lateinit var articleTopicAdapter: ArticleTopicAdapter
    private lateinit var articleAdapter : ArticleListAdapter

    private val viewModel : ArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.apply {
            hiTxt.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slower_top_to_down_anim))
            latestArticleTxt.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_to_right_anim))
            headlineTxt.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_to_right_anim))
            latestTxt.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_to_right_anim))

            articleHeadLineAdapter = ArticleHeadLineAdapter()
            articleHeadlinesRecyclerView.adapter = articleHeadLineAdapter
            articleHeadlinesRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


            articleTopicAdapter = ArticleTopicAdapter()
            articleTopicRecyclerView.adapter = articleTopicAdapter
            articleTopicRecyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)

            articleAdapter = ArticleListAdapter()
            articlesRecyclerView.adapter = articleAdapter
            articlesRecyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.networkStatus.collectLatest {
                    networkStatusShowToast(it, view)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.articleHeadLineFlow.collectLatest {
                    if (it.status == "initialValue") {
                        showHeadlinesShimmerLoadingAnimation()
                    } else {
                        articleHeadLineAdapter.differ.submitList(it.articles)
                        hideHeadlinesShimmerLoadingAnimation()
                    }
                }
            }
        }

        articleHeadLineAdapter.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToArticleFragment(it)
            )
        }

        articleTopicAdapter.setListener(object : ArticleTopicAdapter.Listener{
            override fun onClick(topic: String) {
                ArticlePagingSource.articleCategory = topic
            }

        })

        //TODO: Reload article on network available
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.articlesFlow.collectLatest {
                    articleAdapter.submitData(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                articleAdapter.loadStateFlow.collect { loadStates ->
                    val state = loadStates.refresh
                    if (state is LoadState.Loading || state is LoadState.Error) {
                        showArticlesShimmerLoadingAnimation()
                    } else {
                        hideArticlesShimmerLoadingAnimation()
                    }
                }
            }
        }

        articleAdapter.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToArticleFragment(it)
            )
        }

    }

    private fun networkStatusShowToast(it: String, view: View) {
        val snackbar = Snackbar.make(
            view, getString(R.string.turn_on_mobile_data), Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(getString(R.string.ok)) {
            snackbar.dismiss()
        }
        when(it) {
            getString(R.string.network_available) -> {
                println("Network available")
                snackbar.dismiss()
            }
            getString(R.string.network_unavailable) -> {
                Toast.makeText(
                    context, getString(R.string.network_unavailable), Toast.LENGTH_LONG
                ).show()
            }
            getString(R.string.slow_internet_connection) -> {
                Toast.makeText(
                    context, getString(R.string.slow_internet_connection), Toast.LENGTH_LONG
                ).show()
            }
            getString(R.string.network_lost) -> {
                snackbar.show()
            }
        }
    }

    private fun showHeadlinesShimmerLoadingAnimation() {
        binding.apply {
            articleHeadlinesShimmerLayout.startShimmer()
            articleHeadlinesShimmerLayout.visibility = View.VISIBLE
        }
    }
    private fun hideHeadlinesShimmerLoadingAnimation() {
        binding.apply {
            articleHeadlinesShimmerLayout.stopShimmer()
            articleHeadlinesShimmerLayout.visibility = View.GONE
        }
    }

    private fun showArticlesShimmerLoadingAnimation() {
        binding.apply {
            articlesShimmerLayout.startShimmer()
            articlesShimmerLayout.visibility = View.VISIBLE
        }
    }
    private fun hideArticlesShimmerLoadingAnimation() {
        binding.apply {
            articlesShimmerLayout.stopShimmer()
            articlesShimmerLayout.visibility = View.GONE
        }
    }

}