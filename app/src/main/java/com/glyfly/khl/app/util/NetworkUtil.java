package com.glyfly.khl.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.glyfly.khl.app.MApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017/4/1.
 */

public class NetworkUtil {

    public final static String NETWORK_CMNET = "CMNET";
    public final static String NETWORK_CMWAP = "CMWAP";
    public final static String NETWORK_WIFI = "WIFI";

    private static NetworkInfo getNetworkInfo(){
        ConnectivityManager manager = (ConnectivityManager) MApplication.Companion.getInstance().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (null == manager) {
            return null;
        }
        return manager.getActiveNetworkInfo();
    }

    /**
     * 判断网络是否可用
     *
     * @return 是/否
     */
    public static boolean isAvailable() {

        if (null == getNetworkInfo() || !getNetworkInfo().isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 判断网络是否已连接
     *
     * @return 是/否
     */
    public static boolean isConnected() {
        if (getNetworkInfo() == null || !getNetworkInfo().isConnected()) {
            return false;
        }
        return true;
    }

    /**
     * 检查当前环境网络是否可用，不可用跳转至开启网络界面,不设置网络强制关闭当前Activity
     */
    public static void setNetWork(final Activity mContext) {

        if (!isConnected()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
            dialogBuilder.setTitle("网络设置");
            dialogBuilder.setMessage("网络不可用，是否现在设置网络？");
            dialogBuilder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mContext.startActivityForResult(new Intent(
                                            Settings.ACTION_SETTINGS), which);
                        }
                    });
            dialogBuilder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            dialogBuilder.create();
            dialogBuilder.show();
        }
    }

    /**
     * 获取网络连接信息</br> 无网络：</br> WIFI网络：WIFI</br> WAP网络：CMWAP</br>
     * NET网络：CMNET</br>
     *
     * @return
     */
    public static String getNetworkType() {
        if (isConnected()) {
            int type = getNetworkInfo().getType();
            if (ConnectivityManager.TYPE_MOBILE == type) {
                if (NETWORK_CMWAP.equals(getNetworkInfo().getExtraInfo()
                        .toLowerCase())) {
                    return NETWORK_CMWAP;
                } else {
                    return NETWORK_CMNET;
                }
            } else if (ConnectivityManager.TYPE_WIFI == type) {
                return NETWORK_WIFI;
            }
        }

        return "";
    }

    /**
     * WIFI是否连接
     * @return
     */
    public static boolean isWIFIConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) MApplication.Companion.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 移动网络是否打开
     * @return
     */
    public static boolean isMobileConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) MApplication.Companion.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取网络连接类型
     * ConnectivityManager.TYPE_MOBILE
     * ConnectivityManager.TYPE_WIFI
     * @return
     */
    public static int getConnectedType() {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MApplication.Companion.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        return -1;
    }

    public static String getIp(){
        if (isWIFIConnected()){
            WifiManager wifiManager = (WifiManager) MApplication.Companion.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return (ipAddress & 0xff) + "."
                    +((ipAddress >> 8) & 0xff) + "."
                    +((ipAddress >> 16) & 0xff) + "."
                    +((ipAddress >> 24) & 0xff);
        }else if (isMobileConnected()){
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                    NetworkInterface networkInterface = en.nextElement();
                    for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements();){
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address){
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
