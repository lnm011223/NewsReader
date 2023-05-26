package com.example.newsreader

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newsreader.databinding.ActivityMainBinding
import com.example.newsreader.logic.model.NoticeItem
import com.example.newsreader.logic.network.NoticeService
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.ui.AdapterFragmentPager
import com.example.newsreader.ui.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: String
    private lateinit var username: String

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = intent.getStringExtra("auth").toString()
        username = intent.getStringExtra("username").toString()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.auth = auth
        mainViewModel.username = username
        getPermission(this)
        binding.viewPager.adapter = AdapterFragmentPager(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "热门"
                }
                1 -> {
                    tab.text = "精选"
                }
                2 -> {
                    tab.text = "娱乐"
                }
                3 -> {
                    tab.text = "体育"
                }
                4 -> {
                    tab.text = "作品"
                }
            }
        }.attach()

        val REQUEST_INTERVAL = 2 * 60 * 1000L // 2 分钟

        fun CoroutineScope.startRequest(period: Long) = launch {
            while (isActive) {
                try {
                    Log.d("newsresult", "get")
                    val appService = UserServiceCreator.create<NoticeService>()
                    val response = appService.menuget(auth).await()
                    // 处理响应结果
                    if (response.isNotEmpty()) {

                        Log.d("newsresult", response.last().title)
                        val notice = response.last()
                        val notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val channel =
                                NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
                            notificationManager.createNotificationChannel(channel)
                        }


                        val notification = NotificationCompat.Builder(this@MainActivity, "normal")
                            .setContentTitle(notice.title)
                            .setContentText(notice.content)
                            .setSmallIcon(R.drawable.baseline_notifications_24)
                            .build()

                        notificationManager.notify(1, notification)
                    } else {
                        Log.e("newsresult", "获取数据失败！")
                    }
//                    val appService = UserServiceCreator.create<NoticeService>()
//                    appService.get(auth)
//                        .enqueue(object : Callback<List<NoticeItem>> {
//                            override fun onResponse(
//                                call: Call<List<NoticeItem>>,
//                                response: Response<List<NoticeItem>>
//                            ) {
//                                Log.d("newsresult",response.message())
//
//                                val noticelist = response.body()!!
//                                if (noticelist.isNotEmpty()){
//                                    Log.d("newsresult",noticelist.last().title)
//                                }
//
//                            }
//
//                            override fun onFailure(call: Call<List<NoticeItem>>, t: Throwable) {
//                                t.printStackTrace()
//                            }
//                        })
                    delay(period)
                } catch (e: Exception) {
                    // 处理异常
                    println("Exception: $e")
                }


            }
        }

        // 开始循环请求
        GlobalScope.startRequest(REQUEST_INTERVAL)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update_item -> {
                val intent = Intent(this, UserInfoActivity::class.java)
                intent.putExtra("auth", auth)
                intent.putExtra("username",username)
                startActivity(intent)
            }
            R.id.notice_item -> {
                val appService = UserServiceCreator.create<NoticeService>()
                appService.menuget(auth)
                    .enqueue(object : Callback<List<NoticeItem>> {
                        override fun onResponse(
                            call: Call<List<NoticeItem>>,
                            response: Response<List<NoticeItem>>
                        ) {
                            val noticelist = response.body()!!
                            if (noticelist.isNotEmpty()) {
                                val notice = noticelist.last()
                                Log.d("newsresult",notice.title+" "+notice.content)
                                val notificationManager =
                                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val channel =
                                        NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
                                    notificationManager.createNotificationChannel(channel)
                                }


                                val notification = NotificationCompat.Builder(this@MainActivity, "normal")
                                    .setContentTitle(notice.title)
                                    .setContentText(notice.content)
                                    .setSmallIcon(R.drawable.baseline_notifications_24)
                                    .build()

                                notificationManager.notify(1, notification)
                            }

                        }

                        override fun onFailure(call: Call<List<NoticeItem>>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            }
        }
        return true
    }
    private fun getPermission(activity: FragmentActivity) {
        PermissionX.init(activity)
            .permissions(
                PermissionX.permission.POST_NOTIFICATIONS,
                Manifest.permission.INTERNET,

                )
            .onExplainRequestReason { scope, deniedList ->
                val message = "PermissionX需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
//                    Toast.makeText(activity, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }


    }

}