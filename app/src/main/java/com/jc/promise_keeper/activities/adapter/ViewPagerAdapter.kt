package com.jc.promise_keeper.activities.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jc.promise_keeper.fragments.HomeFragment
import com.jc.promise_keeper.fragments.ProfileFragment
import com.jc.promise_keeper.fragments.PromiseFragment

class MainViewPagerAdapter(
    fm: FragmentActivity
): FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> PromiseFragment()
            else -> ProfileFragment()
        }
    }

}