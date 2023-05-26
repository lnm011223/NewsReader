package com.example.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.ActivityWorkBinding
import com.example.newsreader.logic.model.WorkItem
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.logic.network.WorkService
import com.example.newsreader.ui.UserWorkAdapter
import com.example.newsreader.ui.WorkAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkBinding
    private lateinit var worklist: List<WorkItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appService = UserServiceCreator.create<WorkService>()
        val auth = intent.getStringExtra("auth").toString()
        val username = intent.getStringExtra("username").toString()
        appService.getUserWorks(auth, username)
            .enqueue(object : Callback<List<WorkItem>> {
                override fun onResponse(
                    call: Call<List<WorkItem>>,
                    response: Response<List<WorkItem>>
                ) {


                    worklist = response.body()!!
                    val layoutManager = LinearLayoutManager(this@WorkActivity)
                    binding.WorkRecyclerView.layoutManager = layoutManager
                    val adapter = UserWorkAdapter(worklist,this@WorkActivity, auth)
                    binding.WorkRecyclerView.adapter = adapter

                }

                override fun onFailure(call: Call<List<WorkItem>>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        binding.publishBtn.setOnClickListener {
            val intent = Intent(this,PublishActivity::class.java)
            intent.putExtra("auth",auth)
            startActivity(intent)
        }
        binding.swipefresh.setOnRefreshListener {
            appService.getUserWorks(auth, username)
                .enqueue(object : Callback<List<WorkItem>> {
                    override fun onResponse(
                        call: Call<List<WorkItem>>,
                        response: Response<List<WorkItem>>
                    ) {


                        worklist = response.body()!!
                        val layoutManager = LinearLayoutManager(this@WorkActivity)
                        binding.WorkRecyclerView.layoutManager = layoutManager
                        val adapter = UserWorkAdapter(worklist,this@WorkActivity, auth)
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