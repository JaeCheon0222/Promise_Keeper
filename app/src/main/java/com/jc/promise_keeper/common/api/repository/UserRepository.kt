package com.jc.promise_keeper.common.api.repository

import com.jc.promise_keeper.common.api.Connect
import com.jc.promise_keeper.data.model.basic_response.BasicResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field

object UserRepository {

    // 유저 로그인
    suspend fun postRequestUserSignIn(email: String, pw: String): Response<BasicResponse> =
        Connect.promiseKeepService.postRequestUserSignIn(email, pw)

    // 이메일, 닉네임 중복 체크
    suspend fun getRequestDuplicateEmailAndNickname(type: String, value: String): Boolean {
        val result = Connect.promiseKeepService.getRequestDuplicateEmailAndNickname(type, value)
        return result.isSuccessful
    }

    // 회원가입
    suspend fun putRequestUserSignUp(
        email: String,
        pw: String,
        nickname: String
    ): Response<BasicResponse> =
        Connect.promiseKeepService.putRequestUserSignUp(email, pw, nickname)

    // 내 정보 조회
    suspend fun getMyInfo(): Response<BasicResponse> =
        Connect.promiseKeepService.getMyInfo()

    // 프로필 사진
    suspend fun putRequestSetProfile(multiPart: MultipartBody.Part): Response<BasicResponse> =
        Connect.promiseKeepService.putRequestSetProfile(multiPart)

    // 비밀번호
    suspend fun patchRequestChangePassword(currentPw: String, newPw: String): Response<BasicResponse> =
        Connect.promiseKeepService.patchRequestChangePassword(currentPw, newPw)

    // 소셜 로그인
    suspend fun postSocialLogin(provider: String, uid: String, nickname: String): Response<BasicResponse> =
        Connect.promiseKeepService.postSocialLogin(provider, uid, nickname)

}