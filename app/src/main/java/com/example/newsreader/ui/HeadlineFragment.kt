package com.example.newsreader.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.FragmentHeadlineBinding
import com.example.newsreader.logic.model.HeadlineListResponse
import com.example.newsreader.logic.model.NewsItem
import com.example.newsreader.logic.network.Api
import com.example.newsreader.logic.network.HeadlineNewsService
import com.example.newsreader.logic.network.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HeadlineFragment : Fragment() {
    private lateinit var NewsList: List<NewsItem>
    private lateinit var binding: FragmentHeadlineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeadlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val appService = ServiceCreator.create<HeadlineNewsService>()
        appService.getNewsList("headline", Api.HEADLINE_ID, 0)
            .enqueue(object : Callback<HeadlineListResponse> {
                override fun onResponse(
                    call: Call<HeadlineListResponse>,
                    response: Response<HeadlineListResponse>
                ) {
                    NewsList = response.body()?.newsList!!
                    val layoutManager = LinearLayoutManager(context)
                    binding.HeadlineNewsRecyclerView.layoutManager = layoutManager
                    val adapter = NewsAdapter(NewsList,requireActivity())
                    binding.HeadlineNewsRecyclerView.adapter = adapter
                }

                override fun onFailure(call: Call<HeadlineListResponse>, t: Throwable) {

                    t.printStackTrace()
                }
            })
    }
}