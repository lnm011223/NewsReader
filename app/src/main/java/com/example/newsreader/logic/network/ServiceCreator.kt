package com.example.newsreader.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**

 * @Author liangnuoming
 * @Date 2023/5/22-15:54

 */
object ServiceCreator {
    private const val BASE_URL = "http://c.m.163.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}
object UserServiceCreator {
    private const val BASE_URL = "http://124.222.96.213:5100/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}