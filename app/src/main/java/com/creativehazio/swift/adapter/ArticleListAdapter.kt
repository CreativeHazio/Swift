package com.creativehazio.swift.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creativehazio.swift.R
import com.creativehazio.swift.domain.model.Article

class ArticleListAdapter
    : PagingDataAdapter<Article, ArticleListAdapter.ArticleListViewHolder>(ARTICLE_COMPARATOR) {

    override fun onBindViewHolder(holder: ArticleListViewHolder, position: Int) {
        val article = getItem(position)

        article?.let {
            holder.bind(it)
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(article!!) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListViewHolder {
        return ArticleListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.article_cardview, parent, false)
        )
    }

    inner class ArticleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(article : Article) {
            Glide.with(itemView).load(article.urlToImage).placeholder(R.drawable.app_logo)
                .into(itemView.findViewById(R.id.article_image))
            itemView.findViewById<TextView>(R.id.article_title).text = article.title
            itemView.findViewById<TextView>(R.id.article_description).text = article.description
            itemView.findViewById<TextView>(R.id.article_source).text = article.source?.name
        }

    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {

            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

        }
    }

    private var onItemClickListener : ((Article) -> Unit)? = null
    fun setOnClickListener(listener : (Article) -> Unit) {
        onItemClickListener = listener
    }

}