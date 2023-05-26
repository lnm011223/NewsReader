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
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsreader.*
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
class UserWorkAdapter(val workList: List<WorkItem>, val activity: Activity, val auth: String) :
    RecyclerView.Adapter<UserWorkAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_text: TextView = view.findViewById(R.id.worktitle_text)
        val source_text: TextView = view.findViewById(R.id.worksource_text)
        val content_text: TextView = view.findViewById(R.id.workcontent_text)
        val work_img: ImageView = view.findViewById(R.id.work_img)
        val delete_btn: TextView = view.findViewById(R.id.delete_btn)
        val edit_btn: TextView = view.findViewById(R.id.edit_btn)
        val review_btn: TextView = view.findViewById(R.id.review_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.userwork_item, parent, false)
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
        viewHolder.edit_btn.setOnClickListener {
            val position = viewHolder.adapterPosition
            val work = workList[position]
            val intent = Intent(activity,WorkUpdateActivity::class.java)
            intent.apply {
                putExtra("id",work.id)
                putExtra("auth",auth)
                putExtra("title",work.title)
                putExtra("content",work.content)

            }
            if (!work.imgs.isEmpty()) {
                intent.putExtra("img",work.imgs[0].toString())
            }
            activity.startActivity(intent)

        }
        viewHolder.delete_btn.setOnClickListener {
            val position = viewHolder.adapterPosition
            val work = workList[position]
            val appService = UserServiceCreator.create<WorkService>()
            appService.delete(auth,work.id)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {


                        AlertDialog.Builder(activity).apply {
                            setTitle("提醒：")
                            setMessage("删除成功。请重新进入")

                            setPositiveButton("是") { _, _ ->
                              activity.finish()

                            }


                            show()
                        }

                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val work = workList[position]
        holder.title_text.text = work.title
        holder.source_text.text = work.username
        holder.content_text.text = work.content
        if (work.is_virify){
            holder.review_btn.text = "已通过"
        }
        if (work.imgs.isEmpty()) {
            holder.work_img.visibility = View.GONE
        } else {
            val appService = UserServiceCreator.create<WorkService>()
            appService.getImage(auth, work.imgs[0])
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful()) {
                            Log.d("newsresult", response.body().toString())

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