package com.example.newsreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.webkit.WebViewClient

import com.example.newsreader.databinding.ActivityNewsWebBinding

class NewsWebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webview.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(intent.getStringExtra("newsUrl").toString())
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}