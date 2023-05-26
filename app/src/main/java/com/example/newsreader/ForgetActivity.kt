package com.example.newsreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.newsreader.databinding.ActivityForgetBinding
import com.example.newsreader.logic.network.LoginService
import com.example.newsreader.logic.network.RegisterService
import com.example.newsreader.logic.network.UserServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.reviseBtn.setOnClickListener {
            val appService = UserServiceCreator.create<LoginService>()
            val username = binding.accountField.editText?.text.toString()
            val password = binding.passwordField.editText?.text.toString()
            val phone = binding.phoneField.editText?.text.toString()
            appService.reset(username, password, phone)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
//                        Log.d("newsresult",response.toString())
                        if (response.message() == "OK") {
                            AlertDialog.Builder(this@ForgetActivity).apply {
                                setTitle("提醒：")
                                setMessage("已成功重置密码。")

                                setPositiveButton("是") { _, _ ->
                                    finish()

                                }


                                show()
                            }
                        } else {
                            AlertDialog.Builder(this@ForgetActivity).apply {
                                setTitle("提醒：")
                                setMessage("请检查账号电话号码是否匹配。")

                                setPositiveButton("是") { _, _ ->
                        

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