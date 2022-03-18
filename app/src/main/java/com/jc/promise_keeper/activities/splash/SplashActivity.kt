package com.jc.promise_keeper.activities.splash

import com.jc.promise_keeper.R
import com.jc.promise_keeper.activities.sign_in_out.SignUpActivity
import com.jc.promise_keeper.common.UtilityBase
import com.jc.promise_keeper.databinding.ActivitySplashBinding

class SplashActivity : UtilityBase.BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun ActivitySplashBinding.onCreate() {
        getHandler(SignUpActivity::class.java, 2500, true)
    }

}