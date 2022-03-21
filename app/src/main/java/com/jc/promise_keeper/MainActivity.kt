package com.jc.promise_keeper

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.jc.promise_keeper.activities.adapter.MainViewPagerAdapter
import com.jc.promise_keeper.activities.promise.add.AddPromiseActivity
import com.jc.promise_keeper.activities.promise.detail.PromiseDetailActivity
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
            goToActivityIsFinish(AddPromiseActivity::class.java)
        }
    }

    private fun setUpBottomNavSelected() = with(binding) {

        mainBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    mainViewPager.currentItem = 0
                }
                R.id.nav_appointments -> {
                    mainViewPager.currentItem = 1
                }
                R.id.nav_profile -> {
                    mainViewPager.currentItem = 2
                }

            }
            return@setOnItemSelectedListener true
        }

    }

    private fun moveViewPager() = with(binding) {

        mainViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                mainBottomNav.selectedItemId = when (position) {
                    0 -> {
                        floatingButton.visibility = View.VISIBLE
                        R.id.nav_home
                    }
                    1 -> {
                        floatingButton.visibility = View.VISIBLE
                        R.id.nav_appointments
                    }
                    else -> {
                        floatingButton.visibility = View.GONE
                        R.id.nav_profile
                    }
                }

            }

        })

    }

}