package com.ltbaogt.vocareminder.vocareminder.utils;

import com.ltbaogt.vocareminder.vocareminder.define.Define;

import java.util.HashMap;

/**
 * Created by MyPC on 04/10/2016.
 */
public class HashMapItem extends HashMap<String, String> {

//    public static int sCurrentMax;
    public HashMapItem nextItem;

    public void setNextItem(HashMapItem next) {
        nextItem = next;
    }

//    public void updateCurrentMax() {
//        int currentIndex = Integer.parseInt(get(Define.EXTRA_INDEX));
//        if (sCurrentMax < currentIndex) {
//            sCurrentMax = currentIndex;
//        }
//        if (nextItem != null) {
//            updateCurrentMax();
//        }
//    }

    public int getIndex() {
        return Integer.parseInt(get(Define.EXTRA_INDEX));
    }

    public void setIndex(int index) {
        put(Define.EXTRA_INDEX, String.valueOf(index));
    }

    /**
     * Increase choice to x value
     * @param x
     */
    public void inscreaseIndexTo(int x) {
        setIndex(x);
    }

    /**
     * Decrease choice from max value
     * @param maxValue
     */
    public void descreaseIndexFrom(int maxValue) {
        //Decrease current item
        int currentValue = getIndex();
        //Update current value of item if it's value is greater than max value
        if (currentValue >= maxValue) {
            setIndex(--currentValue);
        }

        //Decrease value of next item
        if (nextItem != null) {
            nextItem.descreaseIndexFrom(maxValue);
        }
    }
}
