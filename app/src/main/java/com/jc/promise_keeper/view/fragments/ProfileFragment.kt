package com.jc.promise_keeper.view.fragments

import android.content.Intent
import android.os.Bundle
import com.jc.promise_keeper.R
import com.jc.promise_keeper.view.activities.place.FrequentlyUsedPlaceActivity
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.FragmentProfileBinding

class ProfileFragment: UtilityBase.BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setEvents()

    }


    override fun setEvents() = with(binding) {
        super.setEvents()

        myStartPlaceListLayout.setOnClickListener {
            Intent(mContext, FrequentlyUsedPlaceActivity::class.java).run {
                startActivity(this)
            }
        }
    }

}