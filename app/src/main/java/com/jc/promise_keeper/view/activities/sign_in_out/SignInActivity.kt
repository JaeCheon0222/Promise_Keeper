package com.jc.promise_keeper.view.activities.sign_in_out

import com.jc.promise_keeper.MainActivity
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivitySignInBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SignInActivity : BaseAppCompatActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val scope = MainScope()

    private lateinit var email: String
    private lateinit var pw: String

    override fun ActivitySignInBinding.onCreate() {
        setEvents()
        initViews()
    }

    override fun initViews() {
        super.initViews()
        binding.autoLoginCheckBox.isChecked = Preferences.getAutoLogin(mContext)

    }

    override fun setEvents() {
        super.setEvents()

        with(binding) {

//            inputEmailEditText.setText("testGuest0311@test.com")
//            inputPasswordEditText.setText("Test!123")

            signInButton.setOnClickListener {
                email = inputEmailEditText.text.toString()
                pw = inputPasswordEditText.text.toString()
                postRequestUserSignIn(email, pw)
            }

            signUpButton.setOnClickListener {
                goToActivityIsFinish(SignUpActivity::class.java)
            }

            autoLoginCheckBox.setOnCheckedChangeListener { _, isChecked ->
                Preferences.setAutoLogin(mContext, isChecked)
            }

        }

    }

    private fun postRequestUserSignIn(email: String, pw: String) = scope.launch {

        val result = UserRepository.postRequestUserSignIn(email, pw)
        val message = result.body()?.message
        val data = result.body()?.data
        if (result.isSuccessful.not()) {
            showToast("이메일 또는 비밀번호가 다릅니다. 다시 시도해주세요.")
            return@launch
        } else {
            showToast("$message ${data?.user?.nickName}님, 환영합니다!")
            data?.let {
                Preferences.setUserToken(mContext, it.token)
                Preferences.setUserEmail(mContext, it.user?.email!!)
                Preferences.setUserProvider(mContext, it.user.provider!!)
                Preferences.setUserProfileImage(mContext, it.user.profileImg!!)
                Preferences.setUserCreatedAt(mContext, it.user.createdAt!!)
                Preferences.setUserNickname(mContext, it.user.nickName!!)
                Preferences.setUserPw(mContext, pw)
                goToActivityIsFinish(MainActivity::class.java)
            }

        }

    }

}