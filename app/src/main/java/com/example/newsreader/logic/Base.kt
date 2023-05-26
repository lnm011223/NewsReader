package com.example.newsreader.logic

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

/**

 * @Author liangnuoming
 * @Date 2023/5/26-01:52

 */

fun getRealPathFromUri(context: Context, uri: Uri): String? {
    var realPath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    if (cursor != null) {
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (cursor.moveToFirst()) {
            realPath = cursor.getString(columnIndex)
        }
        cursor.close()
    }
    return realPath
}
