package com.glyfly.khl.app.activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.glyfly.khl.app.MApplication
import com.glyfly.khl.app.view.LauncherUI
import com.glyfly.khl.app.view.MDialog
import com.glyfly.khl.app.util.BaiduMapUtil
import com.glyfly.khl.app.util.PermissionsUtil
import org.jetbrains.anko.setContentView

class LauncherActivity : AppCompatActivity() {

    lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        LauncherUI().setContentView(this)

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }

        //请求权限
        requestPermissions()

        //定位
        BaiduMapUtil.location()

    }

    private fun requestPermissions() {
        PermissionsUtil.requestPermissions(this, permissions, permissionListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.requestPermissionsResult(PermissionsUtil.PERMISSION_REQUEST_CODE, permissions, grantResults, permissionListener)
    }

    private val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val permissionListener = object : PermissionsUtil.PermissionListener {
        override fun onGranted() {
            toMainActivity()
        }

        override fun onDenied(deniedPermissions: List<String>) {
            val stringBuilder = StringBuilder()
            val size = deniedPermissions.size
            for (i in 0 until size) {
                val permission = deniedPermissions[i]
                when {
                    permission.contains("READ_PHONE_STATE") -> stringBuilder.append("电话，")
                    permission.contains("ACCESS_COARSE_LOCATION") -> stringBuilder.append("位置，")
                    permission.contains("WRITE_EXTERNAL_STORAGE") -> stringBuilder.append("存储，")
                }
                if (i == size - 1) {
                    stringBuilder.deleteCharAt(stringBuilder.length - 1)
                }
            }
            MDialog.Builder.instance.setTitle("提示")
                    .setMessage("应用缺少必要权限($stringBuilder)，请到设置中打开所需权限")
                    .setPositiveButton("去设置", DialogInterface.OnClickListener { dialog, _ ->
                        PermissionsUtil.toCustomSettings(mContext)
                        dialog?.dismiss()
                    })
                    .setNegativeButton("退出", DialogInterface.OnClickListener { _, _ -> MApplication.Companion.getInstance().exit() }).create(mContext).show()
        }
    }

    //跳转首页
    fun toMainActivity () {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }, 2000)
    }
}