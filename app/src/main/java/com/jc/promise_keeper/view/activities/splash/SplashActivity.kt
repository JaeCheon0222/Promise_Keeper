package com.jc.promise_keeper.view.activities.splash

import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivitySplashBinding
import com.jc.promise_keeper.view.activities.sign_in_out.SignInActivity

class SplashActivity : BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun ActivitySplashBinding.onCreate() {
        getHandler(SignInActivity::class.java, 2500, true)
    }

}