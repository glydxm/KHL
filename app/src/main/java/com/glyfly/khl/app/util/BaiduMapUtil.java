package com.glyfly.khl.app.util;


import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.glyfly.khl.app.MApplication;
import com.glyfly.khl.app.entity.LocationEntity;


/**
 * Created by Administrator on 2017/3/28.
 */

public class BaiduMapUtil {

    /**
     * 添加定位覆盖物Marker
     */
    public static void addMarker(BaiduMap mBaiduMap, LatLng position, int drawable){
        BitmapDescriptor bitmap = null;
        try {
            bitmap = BitmapDescriptorFactory.fromResource(drawable);
            MarkerOptions option = new MarkerOptions()
                    .position(position)
                    .icon(bitmap)
                    .zIndex(11);
            mBaiduMap.addOverlay(option);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 将position移到屏幕中心并缩放地图到level级别
     *
     * @param position 地理坐标点
     */
    public static void scaleLocation(BaiduMap mBaiduMap, LatLng position){
        MapStatus.Builder builder = new MapStatus.Builder();
        if (position != null) {
            builder.target(position);
        }
        MapStatus mapStatus = builder.build();
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        if (mBaiduMap!=null) {
            mBaiduMap.animateMapStatus(statusUpdate);
        }
    }

    /**
     * 将position移到屏幕中心并缩放地图到level级别
     *
     * @param position 地理坐标点
     * @param level 地图缩放级别
     */
    public static void scaleLocation(BaiduMap mBaiduMap, LatLng position,int level){
        MapStatus.Builder builder = new MapStatus.Builder();
        if (level != 0) {
            builder.zoom(level);
        }
        if (position != null) {
            builder.target(position);
        }
        MapStatus mapStatus = builder.build();
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        if (mBaiduMap!=null) {
            mBaiduMap.animateMapStatus(statusUpdate);
        }
    }

    private static LocationClient mLocationClient = null;
    private static void initLocation(final LocationListener locationListener){
        mLocationClient = new LocationClient(MApplication.Companion.getInstance());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        //注册监听函数
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                mLocationClient.stop();
                LocationEntity entity = new LocationEntity();
                entity.mAddrStr = bdLocation.getAddrStr();
                entity.mLocType = bdLocation.getLocType();
                entity.mLatitude = bdLocation.getLatitude();
                entity.mLongitude = bdLocation.getLongitude();
                entity.mAddress = bdLocation.getAddress();
                entity.mPoiList = bdLocation.getPoiList();
                if (entity.mAddress != null){
                    String district = entity.mAddress.district;
                    String street = entity.mAddress.street;
                    if (TextUtils.isEmpty(street)) {
                        entity.mSimpleAddr = district;
                    }else {
                        entity.mSimpleAddr = street;
                    }
                }

                MApplication.Companion.getInstance().locationEntity = entity;
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                    // GPS定位结果
                    if (locationListener != null){
                        locationListener.success(entity);
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                    // 网络定位结果
                    if (locationListener != null){
                        locationListener.success(entity);
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    if (locationListener != null){
                        locationListener.success(entity);
                    }
                } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                    //服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                    if (locationListener != null){
                        locationListener.fail("服务端网络定位失败", bdLocation.getLocType());
                    }
                    return;
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                    //网络不同导致定位失败，请检查网络是否通畅
                    if (locationListener != null){
                        locationListener.fail("网络不同导致定位失败", bdLocation.getLocType());
                    }
                    return;
                } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    if (locationListener != null){
                        locationListener.fail("无法获取有效定位依据导致定位失败", bdLocation.getLocType());
                    }
                    return;
                }
            }
        });
    }

    public static void location(){
        if (mLocationClient == null){
            initLocation(null);
        }
        mLocationClient.start();
    }

    public static void location(LocationListener locationListener){
        if (mLocationClient == null){
            initLocation(locationListener);
        }
        mLocationClient.start();
    }

    public interface LocationListener{
        void success(LocationEntity locationEntity);
        void fail(String error, int type);
    }
}
