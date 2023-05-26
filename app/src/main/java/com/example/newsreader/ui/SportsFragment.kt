package com.example.newsreader.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.FragmentSportsBinding
import com.example.newsreader.logic.model.NewsItem
import com.example.newsreader.logic.model.SportsListResponse
import com.example.newsreader.logic.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportsFragment : Fragment() {
    private lateinit var NewsList: List<NewsItem>

    private lateinit var binding: FragmentSportsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appService = ServiceCreator.create<SportsNewsService>()
        appService.getNewsList("list", Api.SPORTS_ID, 0)
            .enqueue(object : Callback<SportsListResponse> {
                override fun onResponse(
                    call: Call<SportsListResponse>,
                    response: Response<SportsListResponse>
                ) {
                    NewsList = response.body()?.newsList!!
                    val layoutManager = LinearLayoutManager(context)
                    binding.NewsRecyclerView.layoutManager = layoutManager
                    val adapter = NewsAdapter(NewsList,requireActivity())

                    binding.NewsRecyclerView.adapter = adapter

                }

                override fun onFailure(call: Call<SportsListResponse>, t: Throwable) {

                    t.printStackTrace()
                }
            })
    }


}