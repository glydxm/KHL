package com.glyfly.khl.app.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */

public class Result<T> {

    private String stat;
    private List<T> data;

    public List<T> getList() {
        if (data != null) {
            return data;
        }else {
            return new ArrayList<>();
        }
    }

    public void setList(List<T> data) {
        this.data = data;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
