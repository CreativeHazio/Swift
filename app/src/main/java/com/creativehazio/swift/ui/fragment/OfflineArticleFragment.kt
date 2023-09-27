package com.creativehazio.swift.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.creativehazio.swift.databinding.FragmentOfflineArticleBinding
import com.creativehazio.swift.ui.viewmodel.ArticleViewModel

class OfflineArticleFragment : Fragment() {

    private var _binding : FragmentOfflineArticleBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ArticleViewModel

    private val args : OfflineArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOfflineArticleBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article
        binding.apply {
            //TODO: Load offline articles from database/file
        }
    }
}