package com.jc.promise_keeper.activities.promise.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.ActivityAddPromiseBinding

class AddPromiseActivity : UtilityBase.BaseAppCompatActivity<ActivityAddPromiseBinding>(R.layout.activity_add_promise) {

    // 필요한 위험 권한
    private val requiredPermissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun ActivityAddPromiseBinding.onCreate() {

        initViews()
        setEvents()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViews() {
        super.initViews()
        initMap()
    }


    override fun setEvents() {
        super.setEvents()
        touchScroll()
        buttonAction()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun initMap() {
        requestLocationPermission()
        ShowNaverMap(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun touchScroll() {
        // 스크롤보조용 텍스트뷰를 손이 닿으면 => 스크롤뷰의 이벤트 일시 정지 (지도만 움직이게)
        binding.txtScrollHelp.setOnTouchListener { view, motionEvent ->

            // 스크롤뷰의 이벤트 정지
            // Disallow -> 허가 하지 않겠다.
            binding.scrollView.requestDisallowInterceptTouchEvent(true)

            // 텍스트뷰의 터치 이벤트만 실행 할 것인지 => 뒤에 가려져있는 지도도 터치를 허용해줘야한다.
            return@setOnTouchListener false

        }
    }

    private fun buttonAction() = with(binding) {

        


    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestLocationPermission() {
        requestPermissions(requiredPermissions, REQUEST_LOCATION_PERMISSION)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 권한 부여 여부
        val locationPermissionGranted = requestCode == REQUEST_LOCATION_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!locationPermissionGranted) {
            if (!shouldShowRequestPermissionRationale(permissions.first())) {
                showPermissionExplanationDialog()
            } else {
                finish()
            }
        }

    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setMessage("위치 정보를 허락해주셔야 이용이 가능합니다. 권한을 획득 해주세요.")
            .setPositiveButton("권한 변경하러 가기") { _, _ -> navigateToAppSetting() }
            .setNegativeButton("앱 종료하기") { _, _ -> finish() }
            .show()
    }

    private fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 201
    }

}