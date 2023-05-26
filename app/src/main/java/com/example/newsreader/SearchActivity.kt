package com.example.newsreader

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.ActivitySearchBinding
import com.example.newsreader.databinding.ActivityWorkBinding
import com.example.newsreader.logic.model.WorkItem
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.logic.network.WorkService
import com.example.newsreader.ui.SearchViewModel
import com.example.newsreader.ui.WorkAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var worklist: List<WorkItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val auth = intent.getStringExtra("auth")


        binding.searchBtn.setOnClickListener {
            val text = binding.searchField.editText?.text.toString()
            if (text.isNotEmpty()){
                val appService = UserServiceCreator.create<WorkService>()
                appService.searchWorks(auth.toString(), text)
                    .enqueue(object : Callback<List<WorkItem>> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<List<WorkItem>>,
                            response: Response<List<WorkItem>>
                        ) {
                            worklist = response.body()!!
                            val virifyList = worklist.toMutableList()
                            for (work in worklist){
                                if (!work.is_virify){
                                    virifyList.remove(work)
                                }
                            }
                            worklist = virifyList.toList()
                            val layoutManager = LinearLayoutManager(this@SearchActivity)
                            binding.WorkRecyclerView.layoutManager = layoutManager
                            val adapter = WorkAdapter(worklist, this@SearchActivity, auth.toString())
                            binding.WorkRecyclerView.adapter = adapter
                        }

                        override fun onFailure(call: Call<List<WorkItem>>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            }

        }
    }
}