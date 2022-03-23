package com.jc.promise_keeper.view.fragments

import android.content.Intent
import android.os.Bundle
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.FriendViewPagerAdapter
import com.jc.promise_keeper.common.util.base_view.BaseFragment
import com.jc.promise_keeper.databinding.FragmentFriendsBinding
import com.jc.promise_keeper.view.activities.friend.SearchUserActivity

class FriendsFragment: BaseFragment<FragmentFriendsBinding>(R.layout.fragment_friends) {

    lateinit var mAdapter: FriendViewPagerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        setEvents()
    }

    override fun initViews() {
        super.initViews()
        mAdapter = FriendViewPagerAdapter(requireActivity().supportFragmentManager)
        binding.friendViewPager.adapter = mAdapter
        binding.friendsTabLayout.setupWithViewPager(binding.friendViewPager)

    }

    override fun setEvents() {
        super.setEvents()
        binding.btnAddFriend.setOnClickListener {
            val myIntent = Intent(mContext, SearchUserActivity::class.java)
            startActivity(myIntent)
        }

    }




}