package com.jc.promise_keeper.common.util

import android.content.Context

object Preferences {

    fun setUserToken(context: Context, token: String) =
        context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.LOGIN_USER_TOKEN, token)
            .apply()


    fun getUserToken(context: Context): String =
        context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE)
            .getString(Keys.LOGIN_USER_TOKEN, "")
            .toString()

    fun setAutoLogin(context: Context, isAuto: Boolean) {
        val pref = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(Keys.AUTO_LOGIN, isAuto).apply()
    }

    fun getAutoLogin(context: Context) : Boolean {
        val pref = context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(Keys.AUTO_LOGIN, true)
    }

    fun setUserEmail(context: Context, email: String) =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.USER_EMAIL, email)
            .apply()

    fun setUserNickname(context: Context, nickname: String) =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.NICK_NAME, nickname)
            .apply()

    fun setUserProvider(context: Context, provider: String) =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.PROVIDER, provider)
            .apply()

    fun setUserProfileImage(context: Context, image: String) =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.PROFILE_IMG, image)
            .apply()

    fun setUserCreatedAt(context: Context, createAt: String) =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.CREATED_AT, createAt)
            .apply()

    fun getUserEmail(context: Context): String =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .getString(Keys.USER_EMAIL, "")
            .toString()

    fun getUserNickname(context: Context): String =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .getString(Keys.NICK_NAME, "")
            .toString()

    fun getUserProvider(context: Context): String =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .getString(Keys.PROVIDER, "")
            .toString()

    fun getUserProfileImage(context: Context): String =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .getString(Keys.PROFILE_IMG, "")
            .toString()

    fun getUserCreatedAt(context: Context): String =
        context.getSharedPreferences(Keys.USER_INFO_NAME, Context.MODE_PRIVATE)
            .getString(Keys.CREATED_AT, "")
            .toString()




}