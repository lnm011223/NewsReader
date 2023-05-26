package com.example.newsreader.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.FragmentEntertainmentBinding
import com.example.newsreader.logic.model.EntertainmentListResponse
import com.example.newsreader.logic.model.NewsItem
import com.example.newsreader.logic.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EntertainmentFragment : Fragment() {
    private lateinit var NewsList: List<NewsItem>

    private lateinit var binding: FragmentEntertainmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEntertainmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appService = ServiceCreator.create<EntertainmentNewsService>()
        appService.getNewsList("list", Api.ENTERTAINMENT_ID, 0)
            .enqueue(object : Callback<EntertainmentListResponse> {
                override fun onResponse(
                    call: Call<EntertainmentListResponse>,
                    response: Response<EntertainmentListResponse>
                ) {
                    NewsList = response.body()?.newsList!!
                    val layoutManager = LinearLayoutManager(context)
                    binding.NewsRecyclerView.layoutManager = layoutManager
                    val adapter = NewsAdapter(NewsList,requireActivity())

                    binding.NewsRecyclerView.adapter = adapter
                }

                override fun onFailure(call: Call<EntertainmentListResponse>, t: Throwable) {

                    t.printStackTrace()
                }
            })
    }
}