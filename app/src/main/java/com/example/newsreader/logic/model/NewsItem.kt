package com.example.newsreader.logic.model

import com.google.gson.annotations.SerializedName

/**

 * @Author liangnuoming
 * @Date 2023/5/18-17:36

 */
data class NewsItem(
    val title: String,
    val digest: String,
    val imgsrc: String,
    val ptime: String,
    val url: String,
    val source: String
)

data class HeadlineListResponse(
    @SerializedName("T1348647853363")
    val newsList: List<NewsItem>
)

data class FeaturedListResponse(
    @SerializedName("T1467284926140")
    val newsList: List<NewsItem>
)

data class EntertainmentListResponse(
    @SerializedName("T1348648517839")
    val newsList: List<NewsItem>
)

data class SportsListResponse(
    @SerializedName("T1348649079062")
    val newsList: List<NewsItem>
)

data class LoginResponse(
    val access_token: String,
    val token_type: String
)

data class WorkListResponse(
    val workList: ArrayList<WorkItem>
//    val workList: String

)

data class WorkItem(
    val id: String,
    val username: String,
    val title: String,
    val content: String,
    val imgs: List<String>,
    val is_virify: Boolean
)

data class NoticeItem(
    val title: String,
    val content: String,
    val create_time: String
)

data class InfoItem(
    val username: String,
    val password: String,
    val phone: String,
    val email: String,
    val is_manager: Boolean
)