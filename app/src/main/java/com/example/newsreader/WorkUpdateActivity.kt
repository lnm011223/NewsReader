package com.example.newsreader

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.example.newsreader.databinding.ActivityWorkUpdateBinding
import com.example.newsreader.logic.getRealPathFromUri
import com.example.newsreader.logic.network.UserServiceCreator
import com.example.newsreader.logic.network.WorkService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class WorkUpdateActivity : AppCompatActivity() {
    private lateinit var imgUri: Uri
    private var flag = false

    private lateinit var binding: ActivityWorkUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val authToken = intent.getStringExtra("auth").toString()
        var title = intent.getStringExtra("title").toString()
        var content = intent.getStringExtra("content").toString()
        val id = intent.getStringExtra("id").toString()
        val del_img = intent.getStringExtra("img").toString()
        binding.titleField.editText!!.setText(title)
        binding.contentField.editText!!.setText(content)

        val appService = UserServiceCreator.create<WorkService>()
        appService.getImage(authToken, del_img)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful()) {
                        Log.d("newsresult", response.body().toString())

                        val bytes = response.body()?.bytes()
                        // 将字节数组转换成 Bitmap 等类型进行处理
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
                        Glide.with(binding.selectImg)
                            .load(bitmap)
                            .into(binding.selectImg)
                        binding.selectImg.setPadding(0)
                    } else {
                        // 处理请求失败的情况
                    }
//                        Glide.with(holder.work_img)
//                            .load(response)
//                            .into(holder.work_img)

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Error", t.message!!)

                    t.printStackTrace()
                }
            })
        binding.selectImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) // 打开相册
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, 2)
        }
        binding.reviseBtn.setOnClickListener {
            title = binding.titleField.editText?.text.toString()
            content = binding.contentField.editText?.text.toString()
            val idPart = MultipartBody.Part.createFormData("id", id)
            val titlePart = MultipartBody.Part.createFormData("title", title)
            val contentPart = MultipartBody.Part.createFormData("content", content)
            val delimgPart = MultipartBody.Part.createFormData("del_imgs", del_img)
            if (flag){
                val file = File(getRealPathFromUri(this, imgUri).toString())
                val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
                val part: MultipartBody.Part =
                    MultipartBody.Part.createFormData("img", file.name, requestFile)
                val appService = UserServiceCreator.create<WorkService>()

                val call =
                    appService.editPost(authToken, idPart, titlePart, contentPart, delimgPart, part)
                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            val post = response.body()
                            Log.d("newsresult", response.message())
                            // 处理添加成功的情况
                            AlertDialog.Builder(this@WorkUpdateActivity).apply {
                                setTitle("提醒：")
                                setMessage("修改成功。")

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
                        Log.d("newsresult", t.message.toString())

                    }
                })
            }else{
                AlertDialog.Builder(this@WorkUpdateActivity).apply {
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