package com.jc.promise_keeper.view.fragments

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.jc.promise_keeper.databinding.FragmentProfileBinding
import com.jc.promise_keeper.view.activities.place.FrequentlyPlaceListActivity
import com.jc.promise_keeper.view.activities.place.FrequentlyUsedPlaceActivity
import com.jc.promise_keeper.view.activities.sign_in_out.SignInActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val scope = MainScope()

    val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {

            val intent = Intent().apply {
                action = Intent.ACTION_PICK
                type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
            }
            startActivityForResult(intent, REQ_RES_CODE.PROFILE)

        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(mContext, "사진 조회 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }

    }


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

        setProfile()

    }


    private fun setProfile() {

        binding.profileImageView.setOnClickListener {

            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_RES_CODE.PROFILE) {

            if (resultCode == Activity.RESULT_OK) {

                val selectedImageUri = data?.data!!
                val file = File(URIPathHelper().getPath(mContext, selectedImageUri))
                val fileReqBody = RequestBody.create("image/*".toMediaType(), file)
                val multiPartBody = MultipartBody.Part
                    .createFormData("profile_image", "myProfile.jpg", fileReqBody)

                putRequestSetProfile(multiPartBody, selectedImageUri)

            }

        }

    }

    private fun putRequestSetProfile(multipartBody: MultipartBody.Part, imageUrl: Uri) =
        scope.launch {

            val result = UserRepository.putRequestSetProfile(multipartBody)

            if (result.isSuccessful) {

                Glide.with(mContext)
                    .load(imageUrl)
                    .into(binding.profileImageView)

                Toast.makeText(mContext, "프로필 변경을 완료했습니다.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(mContext, "프로필 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        }


}