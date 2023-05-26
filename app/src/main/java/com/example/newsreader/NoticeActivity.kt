package com.example.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.newsreader.databinding.ActivityNoticeBinding
import com.example.newsreader.databinding.ActivityPublishBinding
import com.example.newsreader.logic.model.LoginResponse
import com.example.newsreader.logic.network.LoginService
import com.example.newsreader.logic.network.NoticeService
import com.example.newsreader.logic.network.UserServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = intent.getStringExtra("auth")


        binding.sendBtn.setOnClickListener {
            val title = binding.titleField.editText?.text.toString()
            val content = binding.contentField.editText?.text.toString()
            val appService = UserServiceCreator.create<NoticeService>()



            appService.add(auth.toString(), title, content)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Log.d("newsresult", response.toString())
                        if (response.message() == "OK") {

                            AlertDialog.Builder(this@NoticeActivity).apply {
                                setTitle("提醒：")
                                setMessage("发布成功。")

                                setPositiveButton("是") { _, _ ->

                                    finish()

                                }


                                show()
                            }

                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("newsresult", t.message.toString())

                        t.printStackTrace()
                    }
                })
        }

    }
}