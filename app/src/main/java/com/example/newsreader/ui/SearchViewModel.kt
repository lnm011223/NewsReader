package com.example.newsreader.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsreader.logic.model.NewsItem
import com.example.newsreader.logic.model.WorkItem

/**

 * @Author liangnuoming
 * @Date 2023/5/22-16:40

 */
class SearchViewModel : ViewModel() {
    var worklist = MutableLiveData<List<WorkItem>>(arrayListOf())
    var auth = ""
    var username = ""

}