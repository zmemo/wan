package com.memo.iframe.tools.utils

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import java.util.*

/**
 * title:Fragment展示帮助类
 * describe:
 *
 * @author zhou
 * @date 2019-03-29 16:09
 */
class FragmentHelper constructor(containerResId: Int, fragmentManager: FragmentManager) {

    /*** 布局容器id ***/
    private val mContainerResId: Int = containerResId

    /*** Fragment管理 ***/
    private val mFragmentManager: FragmentManager = fragmentManager

    /*** 存放Fragment容器 ***/
    private val mStack: Stack<Fragment> by lazy { Stack<Fragment>() }

    /*** 最后一个显示的下标 ***/
    private var mLastIndex: Int = 0

    /**
     *  添加一个Fragment
     */
    @SuppressLint("CommitTransaction")
    fun add(fragment: Fragment): FragmentHelper {
        if (!mStack.contains(fragment)) {
            mStack.add(fragment)
        }
        return this
    }

    /**
     * 添加Fragment列表
     */
    fun add(vararg fragments: Fragment): FragmentHelper {
        for (fragment in fragments) {
            add(fragment)
        }
        return this
    }

    /**
     * 显示界面
     */
    fun show() {
        val beginTransaction = mFragmentManager.beginTransaction()
        for ((index, fragment) in mStack.withIndex()) {
            beginTransaction.add(mContainerResId, fragment)
            if (0 != index) {
                beginTransaction.hide(fragment)
            } else {
                beginTransaction.show(fragment)
            }
        }
        beginTransaction.commitAllowingStateLoss()
    }

    /**
     * 改变Fragment
     */
    fun change(index: Int) {
        if (mLastIndex != index) {
            val beginTransaction = mFragmentManager.beginTransaction()
            for ((position, fragment) in mStack.withIndex()) {
                if (position != index) {
                    if (fragment.isAdded) {
                        beginTransaction.hide(fragment)
                    }
                } else {
                    if (!fragment.isAdded) {
                        beginTransaction.add(mContainerResId, fragment)
                    }
                    beginTransaction.show(fragment)
                }
            }
            beginTransaction.commitAllowingStateLoss()
            mLastIndex = index
        }
    }


    /*** lazy ***/
    fun lazyShow() {
        mFragmentManager.beginTransaction()
                .add(mContainerResId, mStack[0])
                .commitAllowingStateLoss()
    }
}