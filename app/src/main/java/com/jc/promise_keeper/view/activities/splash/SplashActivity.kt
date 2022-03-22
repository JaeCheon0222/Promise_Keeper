package com.jc.promise_keeper.view.activities.splash

import com.jc.promise_keeper.R
import com.jc.promise_keeper.view.activities.sign_in_out.SignInActivity
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.ActivitySplashBinding

class SplashActivity : UtilityBase.BaseAppCompatActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun ActivitySplashBinding.onCreate() {
        getHandler(SignInActivity::class.java, 2500, true)
    }

}