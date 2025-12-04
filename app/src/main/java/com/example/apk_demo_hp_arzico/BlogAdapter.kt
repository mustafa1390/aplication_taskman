package com.example.aplication_aplication_taskman

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class BlogPost(
    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    val date: String
)

class BlogAdapter(private val blogPosts: List<BlogPost>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    inner class BlogViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvBlogTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvBlogContent)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvBlogAuthor)
        private val tvDate: TextView = itemView.findViewById(R.id.tvBlogDate)

        fun bind(post: BlogPost) {
            tvTitle.text = post.title
            tvContent.text = post.content
            tvAuthor.text = "نویسنده: ${post.author}"
            tvDate.text = post.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_blog_post, parent, false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(blogPosts[position])
    }

    override fun getItemCount(): Int = blogPosts.size
}
