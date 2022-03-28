package com.jc.promise_keeper.view.activities.splash

import android.Manifest
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jc.promise_keeper.MainActivity
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivitySplashBinding
import com.jc.promise_keeper.view.activities.sign_in_out.SignInActivity
import kotlinx.coroutines.*


class SplashActivity : BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val scope = MainScope()
    private var isToken = false

    private var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            getMyInfo()
            getAutoLogin()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                mContext,
                "권한 획득에 실패했습니다.\n$deniedPermissions",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun ActivitySplashBinding.onCreate() {
        initViews()
    }

    override fun initViews() {
        super.initViews()

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("지도를 사용하기위해 위치 정보가 필요합니다.\n\n여기에서 권한을 설정해주세요. [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()

    }

    private fun getMyInfo() = scope.launch {

        val result = UserRepository.getMyInfo()
        if (result.isSuccessful) {

            val code = result.body()?.code
            isToken = (code == 200)

        }

    }

    private fun getAutoLogin() = scope.launch {

        delay(2500)

        val userAutoLogin = Preferences.getAutoLogin(mContext)
        val myIntent: Intent
        if (userAutoLogin && isToken) {
            // 둘다 OK 라면 메인화면으로 이동
            myIntent = Intent(mContext, MainActivity::class.java)

        } else {
            // 아니라면, 로그인 화면으로 이동
            myIntent = Intent(mContext, SignInActivity::class.java)
        }

        startActivity(myIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


}