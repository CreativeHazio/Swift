package com.creativehazio.swift.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creativehazio.swift.R
import com.creativehazio.swift.adapter.ArticleListAdapter
import com.creativehazio.swift.databinding.FragmentSavedArticlesBinding
import com.creativehazio.swift.ui.viewmodel.ArticleViewModel
import com.google.android.material.snackbar.Snackbar

class SavedArticlesFragment : Fragment() {

    private var _binding : FragmentSavedArticlesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels()

    private lateinit var adapter: ArticleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedArticlesBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        binding.apply {
            adapter = ArticleListAdapter()
            savedArticlesRecyclerview.adapter = adapter
            savedArticlesRecyclerview.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: Get saved article from viewmodel and pass it to adapter differ

        adapter.setOnClickListener {
            findNavController().navigate(SavedArticlesFragmentDirections
                .actionSavedArticlesFragmentToOfflineArticleFragment(it))
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                //TODO: Get article and use viewmodel to delete it from database
                context?.getString(R.string.article_deleted)
                    ?.let {
                        Snackbar.make(view, it, Snackbar.LENGTH_LONG).apply {
                            setAction(context.getString(R.string.undo)){
                                //TODO: Resave the article
                            }
                            show()
                        }
                    }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedArticlesRecyclerview)
        }
    }

}