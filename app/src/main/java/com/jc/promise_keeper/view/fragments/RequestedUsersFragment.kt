package com.jc.promise_keeper.view.fragments

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.RequestUserRecyclerAdapter
import com.jc.promise_keeper.common.api.repository.FriendRepository
import com.jc.promise_keeper.common.util.base_view.BaseFragment
import com.jc.promise_keeper.data.model.datas.User
import com.jc.promise_keeper.databinding.FragmentRequestedUsersBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RequestedUsersFragment: BaseFragment<FragmentRequestedUsersBinding>(R.layout.fragment_requested_users) {


    val mRequestedUserList = ArrayList<User>()
    lateinit var  requestUserRecyclerAdapter: RequestUserRecyclerAdapter
    private val scope = MainScope()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()

    }


    override fun initViews() {
        super.initViews()

        getRequestFriendList()

        requestUserRecyclerAdapter = RequestUserRecyclerAdapter(mContext, mRequestedUserList)
        binding.requestUsersRecyclerView.adapter = requestUserRecyclerAdapter
        binding.requestUsersRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }


    private fun getRequestFriendList() = scope.launch {

        val result = FriendRepository.getRequestFriendList("requested")

        if (result.isSuccessful) {

            val body = result.body()!!

            mRequestedUserList.clear()
            mRequestedUserList.addAll(body.data?.friends!!)
            requestUserRecyclerAdapter.notifyDataSetChanged()

        }

    }

    override fun onResume() {
        super.onResume()
        getRequestFriendList()
    }

}