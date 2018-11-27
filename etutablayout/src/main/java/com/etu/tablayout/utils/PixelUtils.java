package com.etu.tablayout.utils;

import android.content.Context;

public class PixelUtils {

    public static  int dp2px(Context context,float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public  static int sp2px(Context context,float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

}
