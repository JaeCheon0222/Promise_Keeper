package com.jc.promise_keeper.activities.sign_in_out

import android.content.Intent
import com.jc.promise_keeper.MainActivity
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.UtilityBase
import com.jc.promise_keeper.databinding.ActivitySignInBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SignInActivity :
    UtilityBase.BaseAppCompatActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val scope = MainScope()

    private lateinit var email: String
    private lateinit var pw: String
    private val TOKEN = "token"

    override fun ActivitySignInBinding.onCreate() {
        setEvents()
    }

    override fun setEvents() {
        super.setEvents()

        binding.signInButton.setOnClickListener {
            email = binding.inputEmailEditText.text.toString()
            pw = binding.inputPasswordEditText.text.toString()
            postRequestUserSignUp(email, pw)
        }

    }

    private fun postRequestUserSignUp(email: String, pw: String) = scope.launch {

        val result = UserRepository.postRequestUserSignUp(email, pw)
        val message = result.body()?.message
        val data = result.body()?.data
        if (result.isSuccessful.not()) {
            showToast("실패")
            return@launch
        }

        showToast("$message ${data?.user?.nickname}님, 환영합니다!")

        data?.token?.let {
            Preferences.setUserToken(mContext, it)
            goToActivityIsFinish(MainActivity::class.java)
        }

    }

}