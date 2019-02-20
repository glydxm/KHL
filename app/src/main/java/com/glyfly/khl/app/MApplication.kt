package com.glyfly.khl.app

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.text.TextUtils
import com.baidu.mapapi.SDKInitializer
import com.glyfly.khl.app.entity.LocationEntity
import com.glyfly.khl.app.entity.User
import com.glyfly.khl.app.util.SharedPreferensesUtil
import com.google.gson.Gson
import com.tencent.bugly.beta.UpgradeInfo
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit

class MApplication : Application () {

    private val activitys = Stack<WeakReference<Activity>>()
    companion object {
        lateinit var mApp: MApplication
        lateinit var user: User
        lateinit var locationEntity: LocationEntity
        lateinit var upgradeInfo: UpgradeInfo
        fun getInstance(): MApplication {
            return mApp
        }
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        mApp = this

        MultiDex.install(applicationContext)

        //初始化user
        var userInfo = SharedPreferensesUtil.getStringValue("user", "")
        if (!TextUtils.isEmpty(userInfo)) {
            user = Gson().fromJson(userInfo, User::class.java)
        }

        //初始化百度地图
        SDKInitializer.initialize(applicationContext)

        //初始化网络请求
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(Constant.HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constant.HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
                .build()
        OkHttpUtils.initClient(okHttpClient)

    }

    /**
     * 获取版本名
     *
     * @return 当前应用的版本名
     */
    fun getVersionName(): String {
        try {
            val manager = this.packageManager
            val info = manager.getPackageInfo(this.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "can not find version name"
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    fun getVersionCode(): Int {
        try {
            val manager = this.packageManager
            val info = manager.getPackageInfo(this.packageName, 0)
            return info.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return -1
    }

    /**
     * 获取栈顶Activity
     */
    fun getTopTask(): WeakReference<Activity> {
        return activitys.peek()
    }

    /**
     * 将Activity压入Application栈
     * @param task 将要压入栈的Activity对象
     */
    fun pushTask(task: WeakReference<Activity>) {
        activitys.push(task)
    }

    /**
     * 将传入的Activity对象从栈中移除
     * @param task
     */
    fun removeTask(task: WeakReference<Activity>) {
        activitys.remove(task)
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    fun removeAll() {
        //finish所有的Activity
        for (task in activitys) {
            val mActivity = task.get()
            if (!mActivity!!.isFinishing()) {
                mActivity.finish()
            }
        }
    }

    /**
     * 退出整个APP，关闭所有activity/清除缓存等等
     *
     */
    fun exit() {
        try {
            //关闭所有Activity
            removeAll()
            //退出进程
            val manager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.killBackgroundProcesses(packageName)
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}