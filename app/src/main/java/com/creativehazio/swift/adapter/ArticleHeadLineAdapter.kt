package com.creativehazio.swift.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creativehazio.swift.R
import com.creativehazio.swift.domain.model.Article

class ArticleHeadLineAdapter : RecyclerView.Adapter<ArticleHeadLineAdapter.HeadlineViewHolder>() {

    private var differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class HeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_headline_cardview, parent, false)
        return HeadlineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(context).load(article.urlToImage).placeholder(R.drawable.app_logo)
                .into(this.findViewById(R.id.headline_img))
            this.findViewById<TextView>(R.id.headline_title).text = article.title
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(article!!) }
        }
    }

    private var onItemClickListener : ((Article) -> Unit)? = null
    fun setOnClickListener(listener : (Article) -> Unit) {
        onItemClickListener = listener
    }
}