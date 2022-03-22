package com.jc.promise_keeper.view.activities.sign_in_out

import com.jc.promise_keeper.MainActivity
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivitySignUpBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SignUpActivity : BaseAppCompatActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val scope = MainScope()

    override fun ActivitySignUpBinding.onCreate() {
        setEvents()
    }

    override fun setEvents() {
        super.setEvents()

        with(binding) {

            signUpButton.setOnClickListener {

                val email = inputEmailEditText.text.toString()
                val pw = inputPasswordEditText.text.toString()
                val nickname = inputNicknameEditText.text.toString()

                putRequestUserSignUp(email, pw, nickname)

            }

            isEmailDuplicateButton.setOnClickListener {

                val type = "EMAIL"
                val email = inputEmailEditText.text.toString()
                getRequestDuplicateEmailAndNickname(type, email)

            }

            isPasswordDuplicateButton.setOnClickListener {

                val pw = inputPasswordEditText.text.toString()
                val checkPw = inputChekcPasswordEditText.text.toString()

                if (!isDuplicatePw(pw, checkPw)) {
                    showToast("비밀번호가 틀립니다. 다시 확인해주세요")
                    return@setOnClickListener
                }

                showToast("비밀번호가 확인되었습니다.")
            }


            isNicknameDuplicateButton.setOnClickListener {
                val type = "NICK_NAME"
                val nickname = inputNicknameEditText.text.toString()
                getRequestDuplicateEmailAndNickname(type, nickname)

            }


        }

    }

    private fun isDuplicatePw(pw: String, checkPw: String): Boolean {
        return pw == checkPw
    }

    private fun putRequestUserSignUp(email: String, pw: String, nickname: String) = scope.launch {

        val result = UserRepository.putRequestUserSignUp(email, pw, nickname)
        val body = result.body()

        if (!result.isSuccessful) {
            showToast("회원가입에 실패했습니다.")
            return@launch
        } else {
            showToast("${body?.data?.user?.nickName}님, 가입을 환영합니다.")
            goToActivityIsFinish(MainActivity::class.java, true)
        }

    }

    private fun getRequestDuplicateEmailAndNickname(type: String, value: String) = scope.launch {

        val result = UserRepository.getRequestDuplicateEmailAndNickname(type, value)

        if (!result) {
            showToast("중복입니다. 다시 입력해주세요")
            return@launch
        }

        showToast("사용가능합니다.")

    }

}