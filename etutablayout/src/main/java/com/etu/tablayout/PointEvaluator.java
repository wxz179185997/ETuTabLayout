package com.etu.tablayout;

import android.animation.TypeEvaluator;

public class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
    @Override
    public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
        float left = startValue.getLeft() + fraction * (endValue.getLeft() - startValue.getLeft());
        float right = startValue.getRight() + fraction * (endValue.getRight() - startValue.getRight());
        IndicatorPoint point = new IndicatorPoint();
        point.setLeft(left);
        point.setRight(right);
        return point;
    }
}
