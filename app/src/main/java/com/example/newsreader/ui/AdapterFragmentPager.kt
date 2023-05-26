package com.example.newsreader.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**

 * @Author liangnuoming
 * @Date 2022/6/24-1:55 上午

 */

//之前使用了fragmentactivity作为参数，导致tab里的fragment生命周期归actvity管理，现在改成fragment
class AdapterFragmentPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            PAGE_Headline -> HeadlineFragment()
            PAGE_Featured -> FeaturedFragment()
            PAGE_Entertainment -> EntertainmentFragment()
            PAGE_Sports -> SportsFragment()
            PAGE_Userswork -> UsersWorkFragment()
            else -> Fragment()

        }
    }


    override fun getItemCount() = 5

    companion object {
        const val PAGE_Headline = 0
        const val PAGE_Featured = 1
        const val PAGE_Entertainment = 2
        const val PAGE_Sports = 3
        const val PAGE_Userswork = 4
    }
}
