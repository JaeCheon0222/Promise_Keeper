package com.jc.promise_keeper

import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.gun0912.tedpermission.PermissionListener
import com.jc.promise_keeper.adapter.MainViewPagerAdapter
import com.jc.promise_keeper.adapter.StartPlaceSpinnerAdapter
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivityMainBinding
import com.jc.promise_keeper.view.activities.promise.add.AddPromiseActivity

class MainActivity : BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {

    lateinit var mStartPlaceAdapter: StartPlaceSpinnerAdapter


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
        goToAddAppointment()
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
                R.id.nav_friends -> {
                    mainViewPager.currentItem = 2
                }
                R.id.nav_profile -> {
                    mainViewPager.currentItem = 3
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
                        addAppointmentButton.visibility = View.VISIBLE
                        R.id.nav_home
                    }
                    1 -> {
                        addAppointmentButton.visibility = View.VISIBLE
                        R.id.nav_appointments
                    }
                    2 -> {
                        addAppointmentButton.visibility = View.GONE
                        R.id.nav_friends
                    }
                    else -> {
                        addAppointmentButton.visibility = View.GONE
                        R.id.nav_profile
                    }
                }

            }

        })

    }

    private fun goToAddAppointment() {

        binding.addAppointmentButton.setOnClickListener {
            goToActivityIsFinish(AddPromiseActivity::class.java)
        }

    }

}