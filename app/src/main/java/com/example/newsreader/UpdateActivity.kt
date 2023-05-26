package com.example.newsreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.newsreader.databinding.ActivitySignUpBinding
import com.example.newsreader.databinding.ActivityUpdateBinding
import com.example.newsreader.logic.network.LoginService
import com.example.newsreader.logic.network.RegisterService
import com.example.newsreader.logic.network.UserServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = intent.getStringExtra("auth").toString()
        var password = intent.getStringExtra("password").toString()
        var email = intent.getStringExtra("email").toString()
        var phone = intent.getStringExtra("phone").toString()
        binding.apply {
            passwordField.editText?.setText(password)
            emailField.editText?.setText(email)
            phoneField.editText?.setText(phone)

        }
        binding.updateBtn.setOnClickListener {
            val appService = UserServiceCreator.create<LoginService>()
            email = binding.emailField.editText?.text.toString()
            phone = binding.phoneField.editText?.text.toString()
            password = binding.passwordField.editText?.text.toString()
            appService.update(auth, email, phone,password)
                .enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
//                        Log.d("newsresult",response.toString())
                        if (response.message() == "OK") {
                            AlertDialog.Builder(this@UpdateActivity).apply {
                                setTitle("提醒：")
                                setMessage("已成功更新信息。")

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