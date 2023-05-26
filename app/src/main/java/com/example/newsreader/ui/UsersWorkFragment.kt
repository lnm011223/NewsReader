package com.example.newsreader.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.R
import com.example.newsreader.SearchActivity
import com.example.newsreader.WorkActivity
import com.example.newsreader.databinding.FragmentSportsBinding
import com.example.newsreader.databinding.FragmentUsersWorkBinding
import com.example.newsreader.logic.model.SportsListResponse
import com.example.newsreader.logic.model.WorkItem
import com.example.newsreader.logic.model.WorkListResponse
import com.example.newsreader.logic.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsersWorkFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentUsersWorkBinding
    private lateinit var worklist: List<WorkItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        // Inflate the layout for this fragment
        binding = FragmentUsersWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appService = UserServiceCreator.create<WorkService>()
        appService.getWorks(mainViewModel.auth)
            .enqueue(object : Callback<List<WorkItem>> {
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
                    val layoutManager = LinearLayoutManager(context)
                    binding.WorkRecyclerView.layoutManager = layoutManager
                    val adapter = WorkAdapter(worklist,requireActivity(),mainViewModel.auth)
                    binding.WorkRecyclerView.adapter = adapter

                }

                override fun onFailure(call: Call<List<WorkItem>>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        binding.swipefresh.setOnRefreshListener {

            appService.getWorks(mainViewModel.auth)
                .enqueue(object : Callback<List<WorkItem>> {
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
                        val layoutManager = LinearLayoutManager(context)
                        binding.WorkRecyclerView.layoutManager = layoutManager
                        val adapter = WorkAdapter(worklist,requireActivity(),mainViewModel.auth)
                        binding.WorkRecyclerView.adapter = adapter
                        binding.swipefresh.isRefreshing = false
                    }

                    override fun onFailure(call: Call<List<WorkItem>>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }

        binding.selfBtn.setOnClickListener {
            val intent = Intent(activity,WorkActivity::class.java)
            intent.putExtra("auth",mainViewModel.auth)
            intent.putExtra("username",mainViewModel.username)
            startActivity(intent)
        }
        binding.searchBtn.setOnClickListener {
            val intent = Intent(activity,SearchActivity::class.java)
            intent.putExtra("auth",mainViewModel.auth)
            intent.putExtra("username",mainViewModel.username)
            startActivity(intent)
        }
    }
}