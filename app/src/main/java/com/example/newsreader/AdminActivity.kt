package com.example.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.ActivityAdminBinding
import com.example.newsreader.logic.model.WorkItem
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.logic.network.WorkService
import com.example.newsreader.ui.AdminWorkAdapter
import com.example.newsreader.ui.UserWorkAdapter
import com.example.newsreader.ui.WorkAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var worklist: List<WorkItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = intent.getStringExtra("auth").toString()
        val appService = UserServiceCreator.create<WorkService>()
        appService.getWorks(auth)
            .enqueue(object : Callback<List<WorkItem>> {
                override fun onResponse(
                    call: Call<List<WorkItem>>,
                    response: Response<List<WorkItem>>
                ) {

                    worklist = response.body()!!
                    val layoutManager = LinearLayoutManager(this@AdminActivity)
                    binding.WorkRecyclerView.layoutManager = layoutManager
                    val adapter = AdminWorkAdapter(worklist, this@AdminActivity, auth)
                    binding.WorkRecyclerView.adapter = adapter

                }

                override fun onFailure(call: Call<List<WorkItem>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        binding.sendNoticeBtn.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
            intent.putExtra("auth", auth)
            startActivity(intent)
        }
        binding.swipefresh.setOnRefreshListener {

            appService.getWorks(auth)
                .enqueue(object : Callback<List<WorkItem>> {
                    override fun onResponse(
                        call: Call<List<WorkItem>>,
                        response: Response<List<WorkItem>>
                    ) {

                        worklist = response.body()!!
                        val layoutManager = LinearLayoutManager(this@AdminActivity)
                        binding.WorkRecyclerView.layoutManager = layoutManager
                        val adapter = AdminWorkAdapter(worklist, this@AdminActivity, auth)
                        binding.WorkRecyclerView.adapter = adapter
                        binding.swipefresh.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<WorkItem>>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
    }
}