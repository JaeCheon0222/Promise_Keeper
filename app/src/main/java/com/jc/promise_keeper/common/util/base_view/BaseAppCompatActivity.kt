package com.jc.promise_keeper.common.util.base_view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

//region * BaseActivity
abstract class BaseAppCompatActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {

    protected lateinit var binding: T
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        mContext = this
        binding.onCreate()

    }

    abstract fun T.onCreate()
    protected open fun initViews() {}
    protected open fun setEvents() {}

    protected open fun showToast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    protected open fun getHandler(cls: Class<*>, delayMillis: Long, isFinish: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            goToActivityIsFinish(cls, isFinish)
        }, delayMillis)
    }

    fun goToActivityIsFinish(cls: Class<*>, isFinish: Boolean = false) {
        startActivity(Intent(mContext, cls))
        if (isFinish) {
            finish()
        }
    }

}
//endregion