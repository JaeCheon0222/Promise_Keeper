package com.jc.promise_keeper.common.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.jc.promise_keeper.R
import com.jc.promise_keeper.view.map.ShowNaverMapActivity

sealed class UtilityBase {

    //region * BaseActivity
    @RequiresApi(Build.VERSION_CODES.M)
    abstract class BaseAppCompatActivity<T : ViewDataBinding>(
        @LayoutRes private val layoutRes: Int
    ) : AppCompatActivity() {

        protected lateinit var binding: T
        protected lateinit var mContext: Context

        // 필요한 위험 권한
        private val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        protected val map: ShowNaverMapActivity by lazy {
            requestLocationPermission()
            ShowNaverMapActivity(binding.root, R.id.naverMap)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.setContentView(this, layoutRes)
            mContext = this
            binding.onCreate()

        }

        abstract fun T.onCreate()
        protected open fun initViews() {}
        protected open fun setEvents() {}

        protected open fun showToast(message: String) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }

        protected open fun getHandler(cls: Class<*>, delayMillis: Long, isFinish: Boolean) {
            Handler(Looper.getMainLooper()).postDelayed({
                goToActivityIsFinish(cls, isFinish)
            }, delayMillis)
        }

        fun goToActivityIsFinish(cls: Class<*>, isFinish: Boolean = false) {
            startActivity(Intent(mContext, cls))
            if (isFinish) {
                finish()
            }
        }

        //region * 권한
        @RequiresApi(Build.VERSION_CODES.M)
        private fun requestLocationPermission() {
            return requestPermissions(requiredPermissions, REQUEST_LOCATION_PERMISSION)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            // 권한 부여 여부
            val locationPermissionGranted =
                requestCode == REQUEST_LOCATION_PERMISSION &&
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
        //endregion


    }
    //endregion

    //region * BaseFragment
    abstract class BaseFragment<T : ViewDataBinding>(
        @LayoutRes private val layoutRes: Int
    ) : Fragment() {

        protected lateinit var binding: T
        protected lateinit var mContext: Context

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
            return binding.root
        }

        // 추후 변경해볼 예정
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            mContext = requireContext()
        }

        protected open fun initViews() {}
        protected open fun setEvents() {}

    }
    //endregion


    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 201
    }

}