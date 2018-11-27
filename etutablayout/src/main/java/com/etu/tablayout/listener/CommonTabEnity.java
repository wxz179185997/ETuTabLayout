package com.etu.tablayout.listener;

import android.support.annotation.DrawableRes;

/**
 * Tab Enity Listener
 */
public interface CommonTabEnity {

    String getTabTitle();

    @DrawableRes
    int getSeletedIcon();

    @DrawableRes
    int getUnSelectedIcon();
}
