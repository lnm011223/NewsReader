package com.example.newsreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsreader.databinding.ActivityForgetBinding
import com.example.newsreader.databinding.ActivitySignUpBinding
import com.example.newsreader.logic.model.HeadlineListResponse
import com.example.newsreader.logic.network.*
import com.example.newsreader.ui.NewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signupBtn.setOnClickListener {
            val appService = UserServiceCreator.create<RegisterService>()
            val username = binding.accountField.editText?.text.toString()
            val password = binding.passwordField.editText?.text.toString()
            val email = binding.emailField.editText?.text.toString()
            val phone = binding.phoneField.editText?.text.toString()
            appService.register(username, password, email, phone)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
//                        Log.d("newsresult",response.toString())
                        if (response.message() == "OK") {
                            AlertDialog.Builder(this@SignUpActivity).apply {
                                setTitle("提醒：")
                                setMessage("已成功创建账号。")

                                setPositiveButton("是") { _, _ ->
                                    finish()

                                }


                                show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {

                        t.printStackTrace()
                    }
                })
        }
    }
}