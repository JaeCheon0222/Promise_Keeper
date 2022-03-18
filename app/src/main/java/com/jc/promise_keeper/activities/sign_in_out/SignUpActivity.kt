package com.jc.promise_keeper.activities.sign_in_out

import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.UtilityBase
import com.jc.promise_keeper.databinding.ActivitySignUpBinding

class SignUpActivity : UtilityBase.BaseAppCompatActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    override fun ActivitySignUpBinding.onCreate() {

        initViews()
        setEvents()

    }

    override fun initViews() {
        super.initViews()
        binding.inputEmailEditText.text.toString()
        binding.inputPasswordEditText.text.toString()
    }

    override fun setEvents() {
        super.setEvents()



    }

}