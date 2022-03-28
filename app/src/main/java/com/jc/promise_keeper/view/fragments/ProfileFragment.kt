package com.jc.promise_keeper.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.REQ_RES_CODE
import com.jc.promise_keeper.common.util.URIPathHelper
import com.jc.promise_keeper.common.util.base_view.BaseFragment
import com.jc.promise_keeper.data.model.datas.User
import com.jc.promise_keeper.databinding.FragmentProfileBinding
import com.jc.promise_keeper.view.activities.my_info.MyProfileActivity
import com.jc.promise_keeper.view.activities.place.FrequentlyPlaceListActivity
import com.jc.promise_keeper.view.activities.place.FrequentlyUsedPlaceActivity
import com.jc.promise_keeper.view.activities.sign_in_out.SignInActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

// activity 로 데이터를 보내기 위한 인터페이스
class ProfileFragment() : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val scope = MainScope()
    private lateinit var userInfo: User


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        setEvents()

    }


    override fun initViews() {
        super.initViews()
//        getProfile()

//        Glide.with(mContext)
//            .load(Preferences.getUserProfileImage(mContext))
//            .into(binding.profileImageView)

//        binding.nickNameTextView.text = Preferences.getUserNickname(mContext)


    }

    override fun setEvents() = with(binding) {
        super.setEvents()

        myProfileLayout.setOnClickListener {
            Intent(mContext, MyProfileActivity::class.java).run {
                startActivity(this)
            }
        }


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

        logoutLayout.setOnClickListener {

            AlertDialog.Builder(mContext)
                .setTitle("로그아웃 경고창")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { _, _ ->
                    // 토큰 초기화한다.
                    Preferences.setUserToken(mContext, "")

                    // 로딩화면으로 복귀
                    val myIntent = Intent(mContext, SignInActivity::class.java)
                    // flag 로 부가적인 옵션 추가가 가능하다. 프래그먼트에는 종료가 없어 CLEAR_TASK 실행
                    myIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(myIntent)

                })
                .setNegativeButton("취소", null)
                .show()

        }

    }





}