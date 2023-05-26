package com.example.newsreader.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.FragmentFeaturedBinding
import com.example.newsreader.logic.model.FeaturedListResponse
import com.example.newsreader.logic.model.NewsItem
import com.example.newsreader.logic.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeaturedFragment : Fragment() {
    private lateinit var NewsList: List<NewsItem>

    private lateinit var binding: FragmentFeaturedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeaturedBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appService = ServiceCreator.create<FeaturedNewsService>()
        appService.getNewsList("list", Api.FEATURED_ID, 0)
            .enqueue(object : Callback<FeaturedListResponse> {
                override fun onResponse(
                    call: Call<FeaturedListResponse>,
                    response: Response<FeaturedListResponse>
                ) {

                    NewsList = response.body()?.newsList!!
                    val layoutManager = LinearLayoutManager(context)
                    binding.FeaturedNewsRecyclerView.layoutManager = layoutManager
                    val adapter = NewsAdapter(NewsList,requireActivity())

                    binding.FeaturedNewsRecyclerView.adapter = adapter
                }

                override fun onFailure(call: Call<FeaturedListResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}