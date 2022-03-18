package com.jc.promise_keeper

import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.ActivityMainBinding

class MainActivity : UtilityBase.BaseAppCompatActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun ActivityMainBinding.onCreate() {

        binding.test.text = Preferences.getUserToken(mContext)

    }

}