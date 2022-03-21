package com.jc.promise_keeper

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.jc.promise_keeper.activities.adapter.MainViewPagerAdapter
import com.jc.promise_keeper.activities.detail.PromiseDetailActivity
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.ActivityMainBinding

class MainActivity :
    UtilityBase.BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun ActivityMainBinding.onCreate() {

        initViews()
        setEvents()

    }

    override fun initViews() {
        super.initViews()
        binding.mainViewPager.adapter = MainViewPagerAdapter(this)

    }

    override fun setEvents() {
        super.setEvents()

        setUpBottomNavSelected()
        moveViewPager()
        addPromise()
    }

    private fun addPromise() = with(binding) {

        floatingButton.setOnClickListener {
            goToActivityIsFinish(PromiseDetailActivity::class.java)
        }

    }

    private fun setUpBottomNavSelected() {

        binding.mainBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    binding.mainViewPager.currentItem = 0
                }
                R.id.nav_appointments -> {
                    binding.mainViewPager.currentItem = 1
                }
                R.id.nav_profile -> {
                    binding.mainViewPager.currentItem = 2
                }

            }
            return@setOnItemSelectedListener true
        }

    }

    private fun moveViewPager() {

        binding.mainViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.mainBottomNav.selectedItemId = when (position) {
                    0 -> {
                        binding.floatingButton.visibility = View.VISIBLE
                        R.id.nav_home
                    }
                    1 -> {
                        binding.floatingButton.visibility = View.VISIBLE
                        R.id.nav_appointments
                    }
                    else -> {
                        binding.floatingButton.visibility = View.GONE
                        R.id.nav_profile
                    }
                }

            }

        })

    }

}