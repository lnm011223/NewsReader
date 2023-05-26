package com.example.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.newsreader.databinding.ActivityAdminLoginBinding
import com.example.newsreader.databinding.ActivityUserLoginBinding
import com.example.newsreader.logic.model.LoginResponse
import com.example.newsreader.logic.network.LoginService
import com.example.newsreader.logic.network.RegisterService
import com.example.newsreader.logic.network.UserServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
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

                            AlertDialog.Builder(this@UserLoginActivity).apply {
                                setTitle("提醒：")
                                setMessage("登录成功。")

                                setPositiveButton("是") { _, _ ->
                                    val intent =
                                        Intent(this@UserLoginActivity, MainActivity::class.java)
                                    intent.putExtra("auth", auth)
                                    intent.putExtra("username",username)
                                    startActivity(intent)
                                    finish()

                                }


                                show()
                            }
                        } else {
                            AlertDialog.Builder(this@UserLoginActivity).apply {
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
//            Toast.makeText(this,"$account $password", Toast.LENGTH_SHORT).show()

        }
        binding.forgetBtn.setOnClickListener {
            val intent = Intent(this, ForgetActivity::class.java)
            startActivity(intent)
        }
    }
}