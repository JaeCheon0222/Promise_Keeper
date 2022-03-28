package com.jc.promise_keeper.view.activities.sign_in_out

import android.util.Log
import com.jc.promise_keeper.MainActivity
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivitySignInBinding
import com.kakao.sdk.user.UserApiClient
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

            kakaoLogin.setOnClickListener {

                if (UserApiClient.instance.isKakaoTalkLoginAvailable((mContext))) {
                    UserApiClient.instance.loginWithKakaoTalk(mContext) { token, error ->

                        postKakaoUserInfo()


                    }
                } else {
                    // 카톡 앱이 없는 상황. 로그인 창 띄워주기
                    UserApiClient.instance.loginWithKakaoAccount(mContext) { token, error ->
                        Log.d("카카오 로그인", "카톡 웹으로 로그인")
                        Log.d("카카오 로그인", "받아온 토큰 : ${token.toString()}")
                        postKakaoUserInfo()
                    }
                }
            }


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

    private fun postKakaoUserInfo() {

        UserApiClient.instance.me { user, error ->

            if (error != null) {
                showToast("사용자 정보 요청 실패")
            } else if (user != null) {

                scope.launch {

                    val result = UserRepository.postSocialLogin(
                        "kakao",
                        user.id.toString(),
                        user.kakaoAccount?.profile?.nickname!!
                    )

                    if (result.isSuccessful) {
                        val body = result.body()
                        Preferences.setUserToken(mContext, body?.data?.token!!)
                        goToActivityIsFinish(MainActivity::class.java)
                        showToast("${body.data.user?.nickName}님, 카톡 로그인을 환영합니다!")

                    } else {
                        showToast("정보 전달 실패")
                    }


                }

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
                goToActivityIsFinish(MainActivity::class.java)
            }

        }

    }


}