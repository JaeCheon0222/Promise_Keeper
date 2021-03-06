package com.jc.promise_keeper.view.activities.sign_in_out

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.jc.promise_keeper.MainActivity
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.UserRepository
import com.jc.promise_keeper.common.util.Preferences
import com.jc.promise_keeper.common.util.base_view.BaseAppCompatActivity
import com.jc.promise_keeper.databinding.ActivitySignInBinding
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class SignInActivity : BaseAppCompatActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    private val scope = MainScope()

    private lateinit var email: String
    private lateinit var pw: String

    lateinit var mCallbackManager: CallbackManager


    val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
//            binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
//            binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
//            binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
//            binding.tvType.text = NaverIdLoginSDK.getTokenType()
//            binding.tvState.text = NaverIdLoginSDK.getState().toString()

            // 프로필 가져오기
            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(response: NidProfileResponse) {
                    Toast.makeText(mContext,"$response",Toast.LENGTH_SHORT).show()
//                    binding.tvApiResult.text = response.toString()
                    Log.d("TAG", "onSuccess: ${response.profile?.nickname}")
                    if (response != null) {

                        scope.launch {
                            val profile = response.profile
                            var nickname = ""

                            if (profile?.nickname == null) {
                                nickname = "이름없음"
                            }


                            val result = UserRepository.postSocialLogin("naver", profile?.id!!, nickname)

                            if (result.isSuccessful) {

                                val body = result.body()!!

                                Preferences.setUserToken(mContext, NaverIdLoginSDK.getAccessToken()!!)

                                showToast("${body.data?.user?.nickName}님, 네이버 로그인을 환영합니다!")

                                goToActivityIsFinish(MainActivity::class.java)
                                finish()

                            } else {
                                showToast("실패")
                            }
                        }
                    }

                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

//                    Toast.makeText(context, "errorCode: $errorCode, errorDesc: $errorDesc", Toast.LENGTH_SHORT).show()
//                    binding.tvApiResult.text = ""
                }
                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            })

        }
        override fun onFailure(httpStatus: Int, message: String) {
//            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
//            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
//            Toast.makeText(context,"errorCode:$errorCode, errorDesc:$errorDescription",Toast.LENGTH_SHORT).show()
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }


    override fun ActivitySignInBinding.onCreate() {
        setEvents()
        initViews()
    }

    override fun initViews() {
        super.initViews()
        mCallbackManager = CallbackManager.Factory.create()
        binding.naverRealLogin.setOAuthLoginCallback(oauthLoginCallback)
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

            facebookLogin.setOnClickListener {

                LoginManager.getInstance().registerCallback(mCallbackManager, object: FacebookCallback<LoginResult> {
                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException) {

                    }

                    override fun onSuccess(result: LoginResult) {

                        // 1. 정보를 받아오면 뭘 할건지? 인터페이스 설정
                        val graphRequest = GraphRequest.newMeRequest(result?.accessToken, object: GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(jsonObj: JSONObject?, response: GraphResponse?) {

                                Log.d("받아온정보", jsonObj!!.toString())

                                scope.launch {
                                    val result = UserRepository.postSocialLogin("facebook", jsonObj.getString("id"), jsonObj.getString("name"))

                                    if (result.isSuccessful) {

                                        val body = result.body()!!

                                        Preferences.setUserToken(mContext, body.data?.token!!)
                                        showToast("${body.data.user?.nickName}님, 페북 로그인을 환영합니다!")

                                        goToActivityIsFinish(MainActivity::class.java)
                                        finish()

                                    } else {
                                        showToast("실패")
                                    }
                                }
                            }

                        })

                        // 2. 실제로 요청 호출
                        graphRequest.executeAsync()

                    }

                })

                // 2. 실제로 페북 로그인 실행
                // 공개 프로필 / 이메일 주소를 받아와 달라
                LoginManager.getInstance().logInWithReadPermissions(this@SignInActivity, Arrays.asList("public_profile", "email"))

            }

            naverLogin.setOnClickListener {

                val OAUTH_CLIENT_ID = resources.getString(R.string.OAUTH_CLIENT_ID)
                val OAUTH_CLIENT_SECRET = resources.getString(R.string.OAUTH_CLIENT_SECRET)
                val OAUTH_CLIENT_NAME = resources.getString(R.string.OAUTH_CLIENT_NAME)

                NaverIdLoginSDK.initialize(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)
                NaverIdLoginSDK.authenticate(mContext, oauthLoginCallback)
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
                        finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}