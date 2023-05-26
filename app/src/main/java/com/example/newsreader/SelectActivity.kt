package com.example.newsreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsreader.databinding.ActivityMainBinding
import com.example.newsreader.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.AdminBtn.setOnClickListener {
            val intent = Intent(this,AdminLoginActivity::class.java)
            startActivity(intent)

        }
        binding.UserBtn.setOnClickListener {
            val intent = Intent(this,UserLoginActivity::class.java)
            startActivity(intent)
        }
        binding.SignUPBtn.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}