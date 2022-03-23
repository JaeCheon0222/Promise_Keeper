package com.jc.promise_keeper.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jc.promise_keeper.view.fragments.FriendsFragment
import com.jc.promise_keeper.view.fragments.HomeFragment
import com.jc.promise_keeper.view.fragments.ProfileFragment
import com.jc.promise_keeper.view.fragments.PromiseFragment

class MainViewPagerAdapter(
    fm: FragmentActivity
): FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> PromiseFragment()
            2 -> FriendsFragment()
            else -> ProfileFragment()
        }
    }

}