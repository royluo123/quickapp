package com.roy.quickapp.framework.event;

/**
 * Created by Administrator on 2017/8/11.
 */

public class BaseEvent {
    public int id;
    public Object args;

    public BaseEvent(int id){
        this.id = id;
    }
}
