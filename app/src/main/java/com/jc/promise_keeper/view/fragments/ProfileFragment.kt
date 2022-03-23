package com.jc.promise_keeper.view.fragments

import android.content.Intent
import android.os.Bundle
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.util.base_view.BaseFragment
import com.jc.promise_keeper.databinding.FragmentProfileBinding
import com.jc.promise_keeper.view.activities.place.FrequentlyPlaceListActivity
import com.jc.promise_keeper.view.activities.place.FrequentlyUsedPlaceActivity

class ProfileFragment: BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setEvents()

    }


    override fun setEvents() = with(binding) {
        super.setEvents()

        addMyStartPlaceLayout.setOnClickListener {

            Intent(mContext, FrequentlyUsedPlaceActivity::class.java).run {
                startActivity(this)
            }

        }


        myStartPlaceListLayout.setOnClickListener {
            Intent(mContext, FrequentlyPlaceListActivity::class.java).run {
                startActivity(this)
            }
        }
    }

}