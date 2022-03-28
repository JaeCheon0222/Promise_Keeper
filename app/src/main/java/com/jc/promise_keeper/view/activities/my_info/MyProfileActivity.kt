package com.jc.promise_keeper.view.activities.my_info

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.REQ_RES_CODE
import com.jc.promise_keeper.common.util.URIPathHelper
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivityMyProfileBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class MyProfileActivity :
    BaseAppCompatActivity<ActivityMyProfileBinding>(R.layout.activity_my_profile) {

    private val scope = MainScope()

    private val permissionListener = object : PermissionListener {
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


    override fun ActivityMyProfileBinding.onCreate() {
        initViews()
        setEvents()
    }

    override fun initViews() {
        super.initViews()
//        Glide.with(mContext)
//            .load(Preferences.getUserProfileImage(mContext))
//            .into(binding.profileImageView)

    }

    override fun setEvents() {
        super.setEvents()
        getProfile()
        passwordChange()

    }

    private fun passwordChange() {

        with(binding) {

            profileChangeCheckBox.setOnCheckedChangeListener { compoundButton, isChange ->

                if (isChange) {
                    userInfoLayout.visibility = View.GONE
                    passwordChangeLayout.visibility = View.VISIBLE
                    passwordChangeButton.visibility = View.VISIBLE
                } else {
                    userInfoLayout.visibility = View.VISIBLE
                    passwordChangeLayout.visibility = View.GONE
                    passwordChangeButton.visibility = View.GONE
                }

            }


            passwordChangeButton.setOnClickListener {

                val newPw = newPasswordEditText.text.toString()
                changePw(newPw)

            }

        }

    }


    private fun changePw(newPw: String) = scope.launch{

        val currentPw = Preferences.getUserPw(mContext)
        val inputCurrentPw = binding.currentPasswordEditText.text.toString()

        Log.d("TAG", "current: ${currentPw}, input: $inputCurrentPw")

        if (currentPw != inputCurrentPw) {
            showToast("기존 비밀번호가 틀립니다.")
            return@launch
        }

        val result = UserRepository.patchRequestChangePassword(currentPw, newPw)

        if (result.isSuccessful) {

            binding.userInfoLayout.visibility = View.VISIBLE
            binding.passwordChangeLayout.visibility = View.GONE
            binding.passwordChangeButton.visibility = View.GONE
            binding.currentPasswordEditText.setText("")
            binding.newPasswordEditText.setText("")
            binding.profileChangeCheckBox.isChecked = false

            showToast("비밀번호 변경에 성공했습니다.")

        } else {
            showToast("비밀번호 변경에 실패했습니다.")
        }

    }

    private fun getProfile() {
        binding.changeProfileButton.setOnClickListener {
            setProfileImage()
        }
    }


    private fun setProfileImage() {

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("프로필", "들어옴")


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

            Log.d("프로필", "${result.body()?.data?.user?.profileImg}")

            if (result.isSuccessful) {

                Glide.with(mContext)
                    .load(imageUrl)
                    .into(binding.profileImageView)

                Preferences.setUserProfileImage(mContext, imageUrl.toString())

                Toast.makeText(mContext, "프로필 변경을 완료했습니다.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(mContext, "프로필 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        }

}