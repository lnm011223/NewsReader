package com.example.newsreader.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.newsreader.logic.model.NewsItem
import com.example.newsreader.logic.network.NewsReaderNetwork
import kotlinx.coroutines.Dispatchers

/**

 * @Author liangnuoming
 * @Date 2023/5/22-16:17

 */
object Repository {
    fun getNews(type: String, id: String, startIndex: Int) = liveData(Dispatchers.IO) {
        val result = try {
            val newsResponse = NewsReaderNetwork.getNews(type, id, startIndex)
            if (newsResponse.newsList != null) {
                val news = newsResponse.newsList
                Result.success(news)
            }else{
                Result.failure(RuntimeException("response status is $newsResponse.status"))
            }

        }catch (e:Exception) {
            Result.failure<List<NewsItem>>(e)
        }
        emit(result)
    }
}