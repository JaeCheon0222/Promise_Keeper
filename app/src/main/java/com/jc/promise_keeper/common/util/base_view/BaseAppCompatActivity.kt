package com.jc.promise_keeper.common.util.base_view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jc.promise_keeper.R

//region * BaseActivity
abstract class BaseAppCompatActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {

    protected lateinit var binding: T
    protected lateinit var mContext: Context

    // 액션바의 UI 요소들을 멤버 변수로 => 상속 받은 화면들이 각자 컨트롤 가능.
    lateinit var txtTitle: TextView
    lateinit var btnAdd: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        mContext = this
        // 실체가 있을 때만 실행
        supportActionBar?.let {
            setCustomActionBar()
        }
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


    private fun setCustomActionBar() {

        val defaultActionBar = supportActionBar!!
//        defaultActionBar.setDisplayShowCustomEnabled(true) // 아래 코드와 같은 기능
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.custom_appbar_layout)

        val toolbar = defaultActionBar.customView.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)

        // UI 요소들 실제 값 대입
        txtTitle = defaultActionBar.customView.findViewById(R.id.txtTitle)
        btnAdd = defaultActionBar.customView.findViewById(R.id.btnAdd)

    }


}
//endregion