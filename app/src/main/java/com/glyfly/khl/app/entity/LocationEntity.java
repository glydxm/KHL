package com.glyfly.khl.app.entity;

import com.baidu.location.Address;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */

public class LocationEntity {

    public int mLocType;
    public String mSimpleAddr;
    public String mAddrStr;
    public Address mAddress;
    public List<Poi> mPoiList;
    public double mLatitude;
    public double mLongitude;
}
