package com.jc.promise_keeper.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

sealed class UtilityBase {

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

        fun goToActivityIsFinish(cls: Class<*>, isFinish: Boolean = false) {
            startActivity(Intent(mContext, cls))
            if (isFinish) {
                finish()
            }
        }

    }
    //endregion

    //region * BaseFragment
    abstract class BaseFragment<T : ViewDataBinding>(
        @LayoutRes private val layoutRes: Int
    ) : Fragment() {

        protected lateinit var binding: T
        protected lateinit var mContext: Context

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
            return binding.root
        }

        // 추후 변경해볼 예정
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            mContext = requireContext()
            initViews()
        }

        protected open fun initViews() {}
        protected open fun setEvents() {}

    }
    //endregion

}