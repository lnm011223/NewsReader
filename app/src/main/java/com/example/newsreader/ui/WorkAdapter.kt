package com.example.newsreader.ui


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsreader.NewsWebActivity
import com.example.newsreader.R
import com.example.newsreader.WorkActivity
import com.example.newsreader.WorkDetailActivity
import com.example.newsreader.logic.model.NewsItem
import com.example.newsreader.logic.model.WorkItem
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.logic.network.WorkService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**

 * @Author liangnuoming
 * @Date 2023/5/23-01:59

 */
class WorkAdapter(val workList: List<WorkItem>, val activity: Activity, val auth: String) :
    RecyclerView.Adapter<WorkAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_text: TextView = view.findViewById(R.id.worktitle_text)
        val source_text: TextView = view.findViewById(R.id.worksource_text)
        val content_text: TextView = view.findViewById(R.id.workcontent_text)
        val work_img: ImageView = view.findViewById(R.id.work_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.work_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val work = workList[position]

            val intent = Intent(parent.context, WorkDetailActivity::class.java)
            intent.apply {
                putExtra("id", work.id)
                putExtra("auth", auth)
                putExtra("title", work.title)
                putExtra("content", work.content)
                putExtra("source",work.username)

            }
            if (!work.imgs.isEmpty()) {
                intent.putExtra("img", work.imgs[0].toString())
            }
            activity.startActivity(intent)

        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val work = workList[position]
        holder.title_text.text = work.title
        holder.source_text.text = work.username
        holder.content_text.text = work.content
        if (work.imgs.isEmpty()) {
            holder.work_img.visibility = View.GONE
        } else {
            val appService = UserServiceCreator.create<WorkService>()
            appService.getImage(auth,work.imgs[0])
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful()) {
                            Log.d("newsresult",response.body().toString())

                            val bytes = response.body()?.bytes()
                            // 将字节数组转换成 Bitmap 等类型进行处理
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
                            Glide.with(holder.work_img)
                            .load(bitmap)
                            .into(holder.work_img)
                        } else {
                            // 处理请求失败的情况
                        }
//                        Glide.with(holder.work_img)
//                            .load(response)
//                            .into(holder.work_img)

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("Error", t.message!!)

                        t.printStackTrace()
                    }
                })

        }
    }

    override fun getItemCount() = workList.size
}