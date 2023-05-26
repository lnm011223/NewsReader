package com.example.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.newsreader.databinding.ActivityUserInfoBinding
import com.example.newsreader.databinding.ActivityWorkDetailBinding
import com.example.newsreader.logic.model.InfoItem
import com.example.newsreader.logic.network.LoginService
import com.example.newsreader.logic.network.UserServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding
    private lateinit var info: InfoItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = intent.getStringExtra("auth")
        val username = intent.getStringExtra("username")

        val appService = UserServiceCreator.create<LoginService>()

        appService.getInfo(auth.toString(), username.toString())
            .enqueue(object : Callback<InfoItem> {
                override fun onResponse(call: Call<InfoItem>, response: Response<InfoItem>) {
//                        Log.d("newsresult",response.toString())
                    if (response.message() == "OK") {
                        info = response.body()!!
                        binding.accountField.editText?.setText(info!!.username)
                        binding.passwordField.editText?.setText(info!!.password)
                        binding.phoneField.editText?.setText(info!!.phone)
                        binding.emailField.editText?.setText(info!!.email)

                    }
                }

                override fun onFailure(call: Call<InfoItem>, t: Throwable) {

                    t.printStackTrace()
                }
            })
        binding.updateBtn.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.apply {
                putExtra("auth", auth)
                putExtra("password", info.password)
                putExtra("email",info.email)
                putExtra("phone",info.phone)
            }
            startActivity(intent)
        }
    }
}