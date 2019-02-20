package com.glyfly.khl.app.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2017/8/2.
 */

public class RecentUseEntity<E> {

    private int size;

    Vector<E> vector;

    public RecentUseEntity(){
        vector = new Vector<>();
    }

    public RecentUseEntity(int size){
        this.size = size;
        vector = new Vector<>(size);
    }

    public void add(E e){

        for (E ee : vector){
            if (e.equals(ee)){
                vector.remove(ee);
                break;
            }
        }

        if (vector.size() == size && size > 0) {
            vector.remove(0);
        }
        vector.add(e);
    }

    public void remove(E e){
        vector.remove(e);
    }

    public List<E> toList(){
        List<E> list = new ArrayList<>();
        for (E e : vector) {
            list.add(0, e);
        }
        return list;
    }

}
