package com.jc.promise_keeper.view.activities.splash

import android.content.Intent
import android.util.Log
import com.jc.promise_keeper.MainActivity
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivitySplashBinding
import com.jc.promise_keeper.view.activities.sign_in_out.SignInActivity
import com.jc.promise_keeper.view.activities.sign_in_out.SignUpActivity
import kotlinx.coroutines.*
import org.json.JSONObject

class SplashActivity : BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    val scope = MainScope()
    var isToken = false


    override fun ActivitySplashBinding.onCreate() {
        getHandler(SignInActivity::class.java, 2500, true)

        initViews()

    }

    override fun initViews() {
        super.initViews()
        getMyInfo()
        getAutoLogin()

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