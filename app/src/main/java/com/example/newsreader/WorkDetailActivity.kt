package com.example.newsreader

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.newsreader.databinding.ActivityWorkBinding
import com.example.newsreader.databinding.ActivityWorkDetailBinding
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.logic.network.WorkService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val title = intent.getStringExtra("title")
        val source = intent.getStringExtra("source")
        val content = intent.getStringExtra("content")
        val auth = intent.getStringExtra("auth")
        val img = intent.getStringExtra("img")
        binding.worktitleText.text = title
        binding.worksourceText.text = source
        binding.workcontentText.text = content
        val appService = UserServiceCreator.create<WorkService>()
        appService.getImage(auth.toString(), img.toString())
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
                        Glide.with(binding.workImg)
                            .load(bitmap)
                            .into(binding.workImg)
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