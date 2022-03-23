package com.jc.promise_keeper.view.activities.place

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jc.promise_keeper.R
import com.jc.promise_keeper.adapter.PlaceRecyclerAdapter
import com.jc.promise_keeper.common.api.repository.PlaceRepository
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.data.model.datas.PlaceData
import com.jc.promise_keeper.databinding.ActivityFrequentlyPlaceListBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FrequentlyPlaceListActivity : BaseAppCompatActivity<ActivityFrequentlyPlaceListBinding>(R.layout.activity_frequently_place_list) {

    private lateinit var placeRecyclerAdapter: PlaceRecyclerAdapter
    private val mPlaceList = ArrayList<PlaceData>()

    private val scope = MainScope()

    override fun ActivityFrequentlyPlaceListBinding.onCreate() {

        txtTitle.text = "출발지 목록"
        btnAdd.visibility = View.VISIBLE    // 숨겨져있던 추가 버튼 보이게 설정.

        initViews()

    }

    override fun initViews() {
        super.initViews()
        placeRecyclerAdapter = PlaceRecyclerAdapter(mContext, mPlaceList)
        binding.frequentlyPlaceRecyclerView.adapter = placeRecyclerAdapter
        binding.frequentlyPlaceRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    private fun getFrequentlyPlace() = scope.launch {

        val result = PlaceRepository.getRequestFrequentlyPlaceList()

        if (result.isSuccessful) {

            val body = result.body()!!
            mPlaceList.clear()
            mPlaceList.addAll(body.data?.places!!)
            Log.d("TAG", "getFrequentlyPlace: $mPlaceList")
            placeRecyclerAdapter.notifyDataSetChanged()
        } else {
            Log.d("TAG", "getFrequentlyPlace: 11")
        }

    }

    override fun onResume() {
        super.onResume()
        getFrequentlyPlace()
    }

}