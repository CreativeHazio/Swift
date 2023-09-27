package com.creativehazio.swift.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.creativehazio.swift.databinding.FragmentArticleBinding
import com.creativehazio.swift.ui.viewmodel.ArticleViewModel
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.OutputStreamWriter

class ArticleFragment : Fragment() {

    private var _binding : FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ArticleViewModel

    private val args : ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticleBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article
        binding.apply {
            articleWebview.apply {
                zoomIn()
                zoomOut()
                webViewClient = WebViewClient()
                article.url?.let {
                    loadUrl(it)
                }
            }

            //TODO How to download and read offline version of articles
            saveArticleBtn.setOnClickListener {

            }
        }
    }

}