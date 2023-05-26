package com.example.newsreader.logic.network

import android.graphics.Bitmap
import android.media.Image
import com.example.newsreader.logic.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**

 * @Author liangnuoming
 * @Date 2023/5/18-17:14

 */
interface HeadlineNewsService {
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    fun getNewsList(
        @Path("type") type: String,
        @Path("id") id: String,
        @Path("startPage") startIndex: Int,
    ): Call<HeadlineListResponse>

}

interface FeaturedNewsService {
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    fun getNewsList(
        @Path("type") type: String,
        @Path("id") id: String,
        @Path("startPage") startIndex: Int,
    ): Call<FeaturedListResponse>

}

interface EntertainmentNewsService {
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    fun getNewsList(
        @Path("type") type: String,
        @Path("id") id: String,
        @Path("startPage") startIndex: Int,
    ): Call<EntertainmentListResponse>

}

interface SportsNewsService {
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    fun getNewsList(
        @Path("type") type: String,
        @Path("id") id: String,
        @Path("startPage") startIndex: Int,
    ): Call<SportsListResponse>

}

interface RegisterService {
    @FormUrlEncoded
    @POST("user/register/")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Call<String>
}

interface LoginService {
    @FormUrlEncoded
    @POST("user/login/")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/reset/")
    fun reset(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("phone") phone: String
    ): Call<String>


    @FormUrlEncoded
    @POST("user/update/")
    fun update(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<String>

    @GET("user/info/")
    fun getInfo(
        @Header("Authorization") authorization: String,
        @Query("username") username: String

    ): Call<InfoItem>
}

interface WorkService {
    @GET("post/list/")
    fun getWorks(
        @Header("Authorization") authorization: String
    ): Call<List<WorkItem>>

    @GET("user/list/")
    fun getUserWorks(
        @Header("Authorization") authorization: String,
        @Query("username") username: String

    ): Call<List<WorkItem>>

    @GET("post/img/{img}")
    fun getImage(
        @Header("Authorization") authorization: String,
        @Path("img") img: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("post/delete/")
    fun delete(
        @Header("Authorization") authorization: String,
        @Field("id") id: String,
    ): Call<String>

    @FormUrlEncoded
    @POST("post/search/")
    fun searchWorks(
        @Header("Authorization") authorization: String,
        @Field("text") text: String

    ): Call<List<WorkItem>>

    @FormUrlEncoded
    @POST("post/verify/")
    fun verify(
        @Header("Authorization") authorization: String,
        @Field("id") id: String,
    ): Call<String>

    @POST("post/add/")
    @Multipart
    fun addPost(
        @Header("Authorization") authHeader: String,
        @Part title: MultipartBody.Part,
        @Part content: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Call<String>

    @POST("post/edit/")
    @Multipart
    fun editPost(
        @Header("Authorization") authHeader: String,
        @Part id: MultipartBody.Part,
        @Part title: MultipartBody.Part,
        @Part content: MultipartBody.Part,
        @Part del_imgs: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Call<String>
}

interface NoticeService {
    @FormUrlEncoded
    @POST("notice/add/")
    fun add(
        @Header("Authorization") authorization: String,
        @Field("title") title: String,
        @Field("content") content: String,
        ): Call<String>

    @GET("notice/list/")
    suspend fun get(
        @Header("Authorization") authorization: String,

    ): Call<List<NoticeItem>>

    @GET("notice/list/")
    fun menuget(
        @Header("Authorization") authorization: String,

        ): Call<List<NoticeItem>>
}