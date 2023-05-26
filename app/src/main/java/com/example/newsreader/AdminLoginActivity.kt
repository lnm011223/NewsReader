package com.example.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.newsreader.databinding.ActivityAdminLoginBinding
import com.example.newsreader.databinding.ActivitySelectBinding
import com.example.newsreader.logic.model.LoginResponse
import com.example.newsreader.logic.network.LoginService
import com.example.newsreader.logic.network.UserServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.LoginBtn.setOnClickListener {
            val username = binding.accountField.editText?.text.toString()
            val password = binding.passwordField.editText?.text.toString()
            val appService = UserServiceCreator.create<LoginService>()

            appService.login(username, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
//                        Log.d("newsresult",response.toString())
                        if (response.message() == "OK") {
                            val auth = "Bearer " + response.body()?.access_token.toString()

                            AlertDialog.Builder(this@AdminLoginActivity).apply {
                                setTitle("提醒：")
                                setMessage("登录成功。")

                                setPositiveButton("是") { _, _ ->
                                    val intent =
                                        Intent(this@AdminLoginActivity, AdminActivity::class.java)
                                    intent.putExtra("auth", auth)
                                    startActivity(intent)
                                    finish()

                                }


                                show()
                            }
                        } else {
                            AlertDialog.Builder(this@AdminLoginActivity).apply {
                                setTitle("提醒：")
                                setMessage("登录失败。请检查账号密码是否正确。")

                                setPositiveButton("是") { _, _ ->

                                }


                                show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                        t.printStackTrace()
                    }
                })
        }
    }
}