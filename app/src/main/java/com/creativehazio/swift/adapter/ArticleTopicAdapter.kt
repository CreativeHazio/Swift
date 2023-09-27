package com.creativehazio.swift.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.creativehazio.swift.R
import com.google.android.material.button.MaterialButton

class ArticleTopicAdapter : RecyclerView.Adapter<ArticleTopicAdapter.ArticleTopicViewHolder>() {

    private val topicList = listOf("Crypto", "Tech", "Gaming", "AI", "StartUps", "Gadgets",
        "Blockchain", "Web3")

    inner class ArticleTopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleTopicViewHolder {
        return ArticleTopicViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.article_topic_cardview, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(holder: ArticleTopicViewHolder, position: Int) {
        val topic = topicList[position]

        holder.itemView.apply {
            this.findViewById<MaterialButton>(R.id.article_topic_button).text = topic
            this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_to_right_anim))

            setOnClickListener {
                _listener.onClick(topic)
            }
        }
    }

    private lateinit var _listener : Listener

    interface Listener {
        fun onClick(topic: String)
    }

    fun setListener(listener: Listener) {
        _listener = listener
    }

}