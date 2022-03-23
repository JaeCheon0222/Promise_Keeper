package com.jc.promise_keeper.view.fragments

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.MyFriendRecyclerAdapter
import com.jc.promise_keeper.common.api.repository.FriendRepository
import com.jc.promise_keeper.common.util.base_view.BaseFragment
import com.jc.promise_keeper.data.model.datas.User
import com.jc.promise_keeper.databinding.FragmentFriendsBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FriendsFragment: BaseFragment<FragmentFriendsBinding>(R.layout.fragment_friends) {

    val mMyFriendsList = ArrayList<User>()

    lateinit var mFriendAdapter : MyFriendRecyclerAdapter

    val scope = MainScope()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
    }

    override fun initViews() {

        mFriendAdapter = MyFriendRecyclerAdapter(mContext, mMyFriendsList)
        binding.myFriendListRecyclerView.adapter = mFriendAdapter
        binding.myFriendListRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    private fun getRequestFriendList() = scope.launch {

        val result = FriendRepository.getRequestFriendList("my")

        if (result.isSuccessful) {

            val body = result.body()!!

            mMyFriendsList.clear()
            mMyFriendsList.addAll(body.data?.friends!!)
            mFriendAdapter.notifyDataSetChanged()

        }


    }

    override fun onResume() {
        super.onResume()
        getRequestFriendList()
    }

}