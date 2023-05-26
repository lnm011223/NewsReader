package com.example.newsreader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Telephony.BaseMmsColumns
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.setPadding
import com.example.newsreader.databinding.ActivityAdminLoginBinding
import com.example.newsreader.databinding.ActivityPublishBinding
import com.example.newsreader.logic.getRealPathFromUri
import com.example.newsreader.logic.network.LoginService
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.logic.network.WorkService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Okio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URI


class PublishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPublishBinding
    private lateinit var imgUri: Uri
    private var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.selectImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) // 打开相册
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, 2)
        }
        binding.publishBtn.setOnClickListener {
            val authToken = intent.getStringExtra("auth").toString()
            val title = binding.titleField.editText?.text.toString()
            val content = binding.contentField.editText?.text.toString()
            val titlePart = MultipartBody.Part.createFormData("title", title)
            val contentPart = MultipartBody.Part.createFormData("content", content)
            if (flag){
                val file = File(getRealPathFromUri(this,imgUri).toString())

                val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
                val part: MultipartBody.Part =
                    MultipartBody.Part.createFormData("img", file.name, requestFile)
                val appService = UserServiceCreator.create<WorkService>()
                val call = appService.addPost(authToken, titlePart, contentPart, part)

                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            val post = response.body()
                            // 处理添加成功的情况
                            AlertDialog.Builder(this@PublishActivity).apply {
                                setTitle("提醒：")
                                setMessage("发布成功。")

                                setPositiveButton("是") { _, _ ->

                                    finish()

                                }


                                show()
                            }
                        } else {
                            // 处理添加失败的情况
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        // 处理网络请求异常的情况
                    }
                })
            }else{
                AlertDialog.Builder(this@PublishActivity).apply {
                    setTitle("提醒：")
                    setMessage("没有选择图片。")

                    setPositiveButton("是") { _, _ ->


                    }


                    show()
                }
            }

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            2 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        imgUri = uri
                        binding.selectImg.setPadding(0)
                        binding.selectImg.setImageURI(imgUri)
                        flag = true

                    }
                }
            }
        }
    }
}