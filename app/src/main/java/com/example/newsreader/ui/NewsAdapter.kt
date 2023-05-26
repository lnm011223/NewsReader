package com.example.newsreader.ui


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsreader.NewsWebActivity
import com.example.newsreader.R
import com.example.newsreader.logic.model.NewsItem

/**

 * @Author liangnuoming
 * @Date 2023/5/23-01:59

 */
class NewsAdapter(val NewsList: List<NewsItem>, val activity: Activity) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_text: TextView = view.findViewById(R.id.newstitle_text)
        val digest_text: TextView = view.findViewById(R.id.newsdigest_text)
        val source_text: TextView = view.findViewById(R.id.newssource_text)
        val ptime_text: TextView = view.findViewById(R.id.newsptime_text)
        val news_img: ImageView = view.findViewById(R.id.news_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val news = NewsList[position]
            if (news.url != "") {
                val intent = Intent(parent.context, NewsWebActivity::class.java)
                intent.putExtra("newsUrl", news.url)
                activity.startActivity(intent)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = NewsList[position]
        holder.title_text.text = news.title
        holder.digest_text.text = news.digest
        holder.source_text.text = news.source
        holder.ptime_text.text = news.ptime
        if (news.imgsrc == "") {
            holder.news_img.visibility = View.GONE
        } else {
            Glide.with(holder.news_img)
                .load(news.imgsrc)
                .into(holder.news_img)
        }
    }

    override fun getItemCount() = NewsList.size
}