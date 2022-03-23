package com.jc.promise_keeper.view.activities.friend

import androidx.recyclerview.widget.LinearLayoutManager
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.SearchedUserRecyclerAdapter
import com.jc.promise_keeper.common.api.repository.FriendRepository
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.User
import com.jc.promise_keeper.databinding.ActivitySearchUserBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchUserActivity :
    BaseAppCompatActivity<ActivitySearchUserBinding>(R.layout.activity_search_user) {

    private val mSearchedUserList = ArrayList<User>()
    lateinit var searchedUserRecyclerAdapter: SearchedUserRecyclerAdapter

    private val scope = MainScope()


    override fun ActivitySearchUserBinding.onCreate() {

        initViews()
        setEvents()

    }


    override fun initViews() {
        super.initViews()
        searchedUserRecyclerAdapter = SearchedUserRecyclerAdapter(mContext, mSearchedUserList)
        binding.userListRecyclerView.adapter = searchedUserRecyclerAdapter
        // 리사이클러뷰는 어떤 모양으로 목록을 표현할지도 설정해야 화면에 데이터가 나온다.
        binding.userListRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    override fun setEvents() {
        super.setEvents()

        binding.btnSearch.setOnClickListener {

            val inputKeyword = binding.edtNickname.text.toString()
            getRequestSearchUser(inputKeyword)

        }

    }

    private fun getRequestSearchUser(inputKeyword: String) = scope.launch {

        val result = FriendRepository.getRequestSearchUser(inputKeyword)

        if (result.isSuccessful) {

            mSearchedUserList.clear()

            val body = result.body()!!
            mSearchedUserList.addAll(body.data?.users!!)
            searchedUserRecyclerAdapter.notifyDataSetChanged()

        }


    }


}