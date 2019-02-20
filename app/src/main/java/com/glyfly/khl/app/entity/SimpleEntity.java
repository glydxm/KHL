package com.glyfly.khl.app.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/28.
 */

public class SimpleEntity extends Object implements Serializable {

    public String text;
    public Object imgUrl;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimpleEntity){
            SimpleEntity entity = (SimpleEntity)obj;
            return equals(text, entity.text) && equals(imgUrl, entity.imgUrl);
        }else {
            return false;
        }
    }

    private boolean equals(Object o1, Object o2){
        if (o1 == null){
            if (o2 == null) {
                return true;
            }else {
                return false;
            }
        }else {
            return o1.equals(o2);
        }
    }
}
